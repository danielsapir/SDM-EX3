package servlets;

import SDM.*;
import SDM.DTO.ItemDTO;
import SDM.DTO.OrderDTO;
import SDM.DTO.ZoneDTO;
import SDM.DTO.storeDTO;
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

public class CustomerZoneInfoServlet extends HttpServlet {
    //Request type (aka reqType) constants
    private static final String ALL_CUSTOMER_ORDERS = "all-customer-orders";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String reqType = req.getParameter(Constants.REQTYPE);
        User userWhoRequest = userManager.getUserByName(SessionUtils.getUsername(req));
        String response = null;

        if(userWhoRequest.getType() == User.Type.CUSTOMER) {
            response = handleGetRequestType(reqType, (Customer)userWhoRequest, req);
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(response);
            out.flush();
        }
    }

    private String handleGetRequestType(String reqType, Customer customer, HttpServletRequest req) {
        String jsonRes = null;
        Gson gson = new Gson();

        switch (reqType) {
            case ALL_CUSTOMER_ORDERS:
                synchronized (customer) {
                    LinkedList<OrderDTO> orderDTOs = new LinkedList<>();
                    for (Order order : customer.getAllOrders()) { //TODO get all orders of this zone
                        orderDTOs.add(new OrderDTO(order));
                    }
                    jsonRes = gson.toJson(orderDTOs);
                }
                break;
        }

        return jsonRes;
    }
}
