package servlets;

import SDM.*;
import SDM.DTO.OrderDTO;
import com.google.gson.Gson;
import constants.Constants;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class OwnerZoneInfoServlet extends HttpServlet {
    //Request type (aka reqType) constants
    private static final String GET_ALL_FEEDBACKS = "get-all-feedbacks";
    private static final String OWNER_NOTIFICATIONS = "owner-notifications";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String reqType = req.getParameter(Constants.REQTYPE);
        User userWhoRequest = userManager.getUserByName(SessionUtils.getUsername(req));
        String response = null;

        if(userWhoRequest.getType() == User.Type.OWNER) {
            response = handleGetRequestType(reqType, (Owner)userWhoRequest, req);
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(response);
            out.flush();
        }
    }

    private String handleGetRequestType(String reqType, Owner owner, HttpServletRequest req) {
        String jsonRes = null;
        Gson gson = new Gson();

        switch (reqType) {
            case GET_ALL_FEEDBACKS:
                synchronized (owner) {

                    jsonRes = gson.toJson(owner.feedBacksOfOwnerInTheZone(SessionUtils.getCurrentZone(req)));
                }
                break;
            case OWNER_NOTIFICATIONS:
                synchronized (owner) {
                    jsonRes = gson.toJson(owner.getAndRemoveAllNotifications());
                }
                break;
        }

        return jsonRes;
    }
}
