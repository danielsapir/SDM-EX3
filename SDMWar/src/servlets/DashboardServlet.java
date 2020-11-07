package servlets;

import SDM.SDMEngine;
import SDM.User;
import SDM.UserManager;
import SDM.Zone;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;
import constants.Constants;
import org.omg.CORBA.DATA_CONVERSION;
import utils.DateUtils;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class DashboardServlet extends HttpServlet {

    //Request type (aka reqType) constants
    private static final String USER_INFO = "user-info";
    private static final String ALL_USERS = "all-users";
    private static final String ALL_ZONES = "all-zones";
    private static final String TO_ZONE = "to-zone";
    private static final String MONEY_AMOUNT = "money-amount";
    private static final String TRANSACTIONS_INFO = "transactions-info";
    private static final String DEPOSIT_MONEY = "deposit-money";

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

        String response = handleGetRequestType(reqType, user, req);

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

        return "zoneDashBoard";
    }

    private String allZoneInfoMaker() {
        Gson gson = new Gson();

        //TODO get all zonesDTO from engine and serialize them to json

        return null;
    }

    private String allUsersInfoMaker() {
        Gson gson  = new Gson();

        return gson.toJson(ServletUtils.getUserManager(getServletContext()).getUsers());
    }

    private String userInfoMaker(User user) {
        Gson gson = new Gson();

        //TODO change to userDTO
        return gson.toJson(user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String reqType = req.getParameter(Constants.REQTYPE);
        String userName = SessionUtils.getUsername(req);
        User user = userManager.getUserByName(userName);

        String response = handlePostRequestType(user, reqType, req);
    }

    private String handlePostRequestType(User user, String reqType, HttpServletRequest req) {
        String response = null;

        switch (reqType) {
            case DEPOSIT_MONEY:
                double amountToDeposit = Double.parseDouble(req.getParameter(DEPOSIT_AMOUNT));
                Date dateOfDeposit = DateUtils.jsonDateToJavaDate(req.getParameter(DEPOSIT_DATE));
                depositMoneyToUser(user, amountToDeposit, dateOfDeposit);
                break;
        }

        return response;
    }

    private void depositMoneyToUser(User user, double amountToDeposit, Date dateOfDeposit) {
        synchronized (user) {
            user.getMoneyAccount().LoadingMoneyInMyAccount(amountToDeposit, dateOfDeposit);
        }
    }
}
