package servlets;

import SDM.*;
import SDM.DTO.UserDTO;
import SDM.DTO.ZoneDTO;
import SDM.Exception.*;
import com.google.gson.Gson;
import constants.Constants;
import utils.DateUtils;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class DashboardServlet extends HttpServlet {

    //Request type (aka reqType) constants
    private static final String USER_INFO = "user-info";
    private static final String ALL_USERS = "all-users";
    private static final String ALL_ZONES = "all-zones";
    private static final String TO_ZONE = "to-zone";
    private static final String MONEY_AMOUNT = "money-amount";
    private static final String TRANSACTIONS_INFO = "transactions-info";
    private static final String DEPOSIT_MONEY = "deposit-money";
    private static final String UPLOAD_FILE = "upload-file";

    //Request parameters constants
    private static final String DEPOSIT_AMOUNT = "depositAmount";
    private static final String DEPOSIT_DATE = "date";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String reqType = req.getParameter(Constants.REQTYPE);
        String userName = SessionUtils.getUsername(req);
        User user = userManager.getUserByName(userName);
        String response = null;
        if(user != null) {
            response = handleGetRequestType(reqType, user, req);
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(response);
            out.flush();
        }
    }

    private String handleGetRequestType(String reqType, User user, HttpServletRequest req) {
        String jsonRes = null;

        switch (reqType) {
            case USER_INFO:
                jsonRes = userInfoMaker(user);
                break;
            case ALL_USERS:
                jsonRes = allUsersInfoMaker();
                break;
            case ALL_ZONES:
                jsonRes = allZoneInfoMaker();
                break;
            case TO_ZONE:
                jsonRes = changeToZoneScreen(req);
                break;
            case MONEY_AMOUNT:
                jsonRes = Double.toString(user.getMoneyAccount().getAmount());
                break;
            case TRANSACTIONS_INFO:
                jsonRes = allTransactionsMaker(user);
                break;
        }

        return jsonRes;
    }

    private String allTransactionsMaker(User user) {
        Gson gson = new Gson();
        return gson.toJson(user.getMoneyAccount().getTransactions());
    }

    private String changeToZoneScreen(HttpServletRequest req) {
        SDMEngine sdmEngine = ServletUtils.getSDMEngine(getServletContext());
        Zone currentZone = sdmEngine.getZoneByName(req.getParameter(Constants.CURRENT_ZONE));

        req.getSession(true).setAttribute(Constants.CURRENT_ZONE, currentZone);

        return null;
    }

    private String allZoneInfoMaker() {
        Gson gson = new Gson();
        SDMEngine sdmEngine =  ServletUtils.getSDMEngine(getServletContext());
        LinkedList<ZoneDTO> zonesDTOs = new LinkedList<>();

        for(Zone zone : sdmEngine.getAllZones().values()) {
            zonesDTOs.add(new ZoneDTO(zone));
        }

        return gson.toJson(zonesDTOs);
    }

    private String allUsersInfoMaker() {
        Gson gson  = new Gson();
        LinkedList<UserDTO> usersDTOs = new LinkedList<>();

        for(User user : ServletUtils.getUserManager(getServletContext()).getUsers()) {
            usersDTOs.add(new UserDTO(user));
        }

        return gson.toJson(usersDTOs);
    }

    private String userInfoMaker(User user) {
        Gson gson = new Gson();

        return gson.toJson(new UserDTO(user));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String reqType = req.getParameter(Constants.REQTYPE);
        String userName = SessionUtils.getUsername(req);
        User user = userManager.getUserByName(userName);

        String response = handlePostRequestType(user, reqType, req, resp);

        try(PrintWriter out = resp.getWriter()) {
            out.println(response);
            out.flush();
        }
    }

    private String handlePostRequestType(User user, String reqType, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String response = null;

        switch (reqType) {
            case DEPOSIT_MONEY:
                double amountToDeposit = Double.parseDouble(req.getParameter(DEPOSIT_AMOUNT));
                Date dateOfDeposit = DateUtils.jsonDateToJavaDate(req.getParameter(DEPOSIT_DATE));
                depositMoneyToUser(user, amountToDeposit, dateOfDeposit);
                break;
            case UPLOAD_FILE:
                response = handleFileUpload(user, req, resp);
                break;

        }

        return response;
    }

    private void depositMoneyToUser(User user, double amountToDeposit, Date dateOfDeposit) {
        synchronized (user) {
            user.getMoneyAccount().LoadingMoneyInMyAccount(amountToDeposit, dateOfDeposit);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = SessionUtils.getUsername(req);
        User user = userManager.getUserByName(userName);

        String response = null;

        response = handleFileUpload(user, req, resp);

        try(PrintWriter out = resp.getWriter()) {
            out.println(response);
            out.flush();
        }
    }

    private String handleFileUpload(User user, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Gson gson = new Gson();
        String jsonRes = null;

        Collection<Part> parts = req.getParts();

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {

            fileContent.append(readFromInputStream(part.getInputStream()));
        }

        if(!(user.getType() == User.Type.OWNER)){
            return gson.toJson(new xmlLoadingResponse(false,"You are not allowed to upload XML files to the system! Only Owner can"));
        }

        try {
            ServletUtils.getSDMEngine(getServletContext()).loadXMLToZone(fileContent.toString(), (Owner)user);
            jsonRes = gson.toJson(new xmlLoadingResponse(true,""));
        } catch (Exception ex) {
            jsonRes = gson.toJson(new xmlLoadingResponse(false,makeErrorMessageOutOfException(ex)));
        }

        return jsonRes;
    }

    private String makeErrorMessageOutOfException(Exception ex) {
        String errorMsg = null;
        
        if (ex instanceof FileNotFoundException) {
            errorMsg =("\nERROR: The file does not exist in the path given, please try again.");
        } else if (ex instanceof FileNotEndWithXMLException) {
            errorMsg =("\nERROR: The file you given extension is " + ((FileNotEndWithXMLException) ex).getFileType() + " and not an XML file, please make sure it ends with .xml and try again.");
        } else if (ex instanceof LocationIsOutOfBorderException) {
            errorMsg =("\nERROR: The object of type " + ((LocationIsOutOfBorderException) ex).getLocatableType() +
                    " with id of: " + ((LocationIsOutOfBorderException) ex).getId() + " is located out of allowed borders which are between "
                    + ((LocationIsOutOfBorderException) ex).getMinBorder() + " to " + ((LocationIsOutOfBorderException) ex).getMaxBorder() + ". Please fix this.");
        } else if (ex instanceof DuplicateStoreItemException) {
            errorMsg =("\nERROR: The store item with ID of " + ((DuplicateStoreItemException) ex).getId() + " appears more than once in the XML file.");
        } else if (ex instanceof DuplicateItemException) {
            errorMsg =("\nERROR: The item with ID of " + ((DuplicateItemException) ex).getId() + " appears more than once in the XML file.");
        } else if (ex instanceof TryingToGiveDifferentPricesForSameStoreItemException) {
            errorMsg =("\nERROR: The file has store with ID " + ((TryingToGiveDifferentPricesForSameStoreItemException) ex).getStoreId() + " that try to give an item price multiple time. ");
        } else if (ex instanceof TryingToGivePriceOfItemWhichIDNotExistException) {
            errorMsg =("\nERROR: The file has store which trying to give a price of item which is ID " + ((TryingToGivePriceOfItemWhichIDNotExistException)ex).getId() + " does not exist");
        } else if (ex instanceof DuplicateStoreIDException) {
            errorMsg =("\nERROR: The store with ID of " + ((DuplicateStoreIDException)ex).getId() + " appears more than once in the XML file");
        } else if(ex instanceof ItemNoOneSellException) {
            errorMsg =("\nERROR: The item with ID " + ((ItemNoOneSellException)ex).getId() + " doesnt sold by any store.");
        } else if(ex instanceof StoreWithNoItemException) {
            errorMsg =("\nERROR: The store with ID " + ((StoreWithNoItemException) ex).getId() + " doesnt sell any items");
        }else if(ex instanceof DuplicateCustomerIdException) {
            errorMsg =("\nERROR: The customer with ID of " + ((DuplicateCustomerIdException) ex).getId() + " appears more than once in the XML file");
        }else if(ex instanceof DuplicatedLocationException) {
            errorMsg =("\nERROR: There are more than one object in the location of " + ((DuplicatedLocationException) ex).getDuplicateLocation().toString());
        }else if(ex instanceof DiscountWithItemNotSoldByStoreException) {
            errorMsg =("\nERROR: There is a discount of store " + ((DiscountWithItemNotSoldByStoreException)ex).getStoreWithDiscount().getName() +
                    " that try to have an Item with ID of " +
                    ((DiscountWithItemNotSoldByStoreException) ex).getIdOfItemInDiscount() + " that this store doesnt sell");
        }else if(ex instanceof DuplicateZoneNameException) {
            errorMsg =("\nERROR: Your are trying to load zone " + ((DuplicateZoneNameException) ex).getZoneName() + " which already exists");
        }else {
            errorMsg =("\nERROR: Unknown error has happen, the error message is: " + ex.getMessage());
        }

        return errorMsg;
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private static class xmlLoadingResponse {
        private boolean succeeded;
        private String message;

        public xmlLoadingResponse(boolean succeeded, String message) {
            this.succeeded = succeeded;
            this.message = message;
        }
    }

}
