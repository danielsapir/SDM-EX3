package servlets;

import SDM.User;
import SDM.UserManager;
import com.google.gson.Gson;
import constants.Constants;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DashboardServlet extends HttpServlet {

    //Request type (aka reqType) constants
    private static final String USER_INFO = "user-info";
    private static final String ALL_USERS = "all-users";
    private static final String ALL_ZONES = "all-zones";
    private static final String TO_ZONE = "to-zone";
    private static final String MONEY_AMOUNT = "money-amount";
    private static final String TRANSACTIONS_INFO = "transactions-info";
    private static final String DEPOSIT_MONEY = "deposit-money";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String reqType = req.getParameter(Constants.REQTYPE);
        String userName = SessionUtils.getUsername(req);
        User user = userManager.getUserByName(userName);

        String response = handleGetRequestType(reqType, user);
    }

    private String handleGetRequestType(String reqType, User user) {
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

        }
        return reqType;
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

        String response = handlePostRequestType(user, reqType);
    }

    private String handlePostRequestType(User user, String reqType) {

        return reqType;
    }
}
