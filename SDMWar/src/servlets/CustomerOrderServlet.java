package servlets;

import SDM.*;
import SDM.DTO.OrderDTO;
import com.google.gson.Gson;
import constants.Constants;
import utils.DateUtils;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;

public class CustomerOrderServlet extends HttpServlet {
    //Request type (aka reqType) constants
    private static final String INITIAL_ORDER_DATA = "inital-order-data";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String reqType = req.getParameter(Constants.REQTYPE);
        User userWhoRequest = userManager.getUserByName(SessionUtils.getUsername(req));
        String response = null;

        if(userWhoRequest.getType() == User.Type.CUSTOMER) {
            response = handlePostRequestType(reqType, (Customer)userWhoRequest, req);
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(response);
            out.flush();
        }
    }

    private String handlePostRequestType(String reqType, Customer customer, HttpServletRequest req) {
        String jsonRes = null;
        Gson gson = new Gson();

        switch (reqType) {
            case INITIAL_ORDER_DATA:
                startNewOrderForCustomer(customer, req);
                break;
        }

        return jsonRes;
    }

    private void startNewOrderForCustomer(Customer customer, HttpServletRequest req) {
        String X_CORDINATE_PARAM = "xCor";
        String Y_CORDINATE_PARAM = "yCor";
        String DATE_PARAM = "date";
        String ORDER_TYPE_PARAM = "orderType";
        String STORE_CHOSE_ID = "storeChoose";

        Location destinationLocation = new Location(
                new Point(
                        Integer.parseInt(req.getParameter(X_CORDINATE_PARAM)),
                        Integer.parseInt(req.getParameter(Y_CORDINATE_PARAM))));
        Date dateOfOrder = DateUtils.jsonDateToJavaDate(req.getParameter(DATE_PARAM));
        OrderType orderType = OrderType.valueOf(req.getParameter(ORDER_TYPE_PARAM).toUpperCase());
        Zone currentZone = SessionUtils.getCurrentZone(req);
        Store storeOrderMadeFrom = null;

        if(req.getParameter(STORE_CHOSE_ID) != null) {
            Integer storeId = Integer.parseInt(req.getParameter(STORE_CHOSE_ID));
            storeOrderMadeFrom = currentZone.getAllStoresMap().get(storeId);
        }
        if(orderType == OrderType.ONE_STORE_ORDER) {
            customer.createNewOneStoreOrder(customer, dateOfOrder, storeOrderMadeFrom ,currentZone, destinationLocation);
        }
        if(orderType == OrderType.DYNAMIC_ORDER) {
            customer.createNewDynamicOrder(customer, dateOfOrder ,currentZone, destinationLocation);
        }
    }
}
