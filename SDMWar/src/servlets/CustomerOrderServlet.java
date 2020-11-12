package servlets;

import SDM.*;
import SDM.DTO.DiscountDTO;
import SDM.DTO.OneStoreOrderDTO;
import SDM.DTO.OrderDTO;
import SDM.DTO.StoreItemDTO;
import SDM.Exception.NegativeAmountOfItemInException;
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
import java.util.Map;

public class CustomerOrderServlet extends HttpServlet {
    //Request type (aka reqType) constants
    private static final String INITIAL_ORDER_DATA = "inital-order-data";
    private static final String ALL_ITEMS_FOR_SALE = "all-items-for-sale";
    private static final String ADD_ITEMS_TO_ORDER = "add-items-to-order";
    private static final String DYNAMIC_ORDER_MID_INFO = "dynamic-order-mid-info";
    private static final String GET_DISCOUNTS = "get-discounts";
    private static final String ADD_DISCOUNT = "add-discount";
    private static final String ORDER_SUMMARY_INFO = "order-summary-info";
    private static final String ACCEPT_ORDER = "accept-order";
    private static final String CANCEL_ORDER = "cancel-order";
    private static final String GIVE_FEEDBACK_TO_STORE = "give-feedback-to-store";
    private static final String DONE_FEEDBACK = "done-feedback";


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
            case ALL_ITEMS_FOR_SALE:
                synchronized (customer) {
                    jsonRes = getStoreItemForSale(customer, req);
                }
                break;
            case DYNAMIC_ORDER_MID_INFO:
                synchronized (customer) {
                    jsonRes = getListOfOneStoreOrder(customer);
                }
                break;
            case GET_DISCOUNTS:
                synchronized (customer) {
                    jsonRes = getDiscountsOfCurrentOrder(customer);
                }
                break;
            case ORDER_SUMMARY_INFO:
                synchronized (customer) {
                    jsonRes = getOneStoreOrderSummary(customer);
                }
                break;
        }

        return jsonRes;
    }

    private String getOneStoreOrderSummary(Customer customer) {
        Gson gson = new Gson();
        LinkedList<OneStoreOrderDTO> oneStoreOrderDTOS = new LinkedList<>();

        for(OneStoreOrder oneStoreOrder : customer.getListOfOneStoreOrdersOfCurrentOrder()) {
            oneStoreOrderDTOS.add(new OneStoreOrderDTO(oneStoreOrder));
        }

        return gson.toJson(oneStoreOrderDTOS);
    }

    private String getDiscountsOfCurrentOrder(Customer customer) {
        Gson gson = new Gson();
        LinkedList<DiscountDTO> discountDTOS = new LinkedList<>();

        for(Discount discount :customer.getListOfDiscountsOfCurrentOrder()) {
            discountDTOS.add(new DiscountDTO(discount));
        }

        return gson.toJson(discountDTOS);
    }

    private String getListOfOneStoreOrder(Customer customer) {
        Gson gson = new Gson();
        LinkedList<OneStoreOrderDTO> oneStoreOrderDTOS = new LinkedList<>();

        for(OneStoreOrder oneStoreOrder : customer.getListOfOneStoreOrdersOfCurrentOrder()) {
            oneStoreOrderDTOS.add(new OneStoreOrderDTO(oneStoreOrder));
        }

        return gson.toJson(oneStoreOrderDTOS);
    }

    private String getStoreItemForSale(Customer customer, HttpServletRequest req) {
        Gson gson = new Gson();
        Map<Integer, StoreItem> integerStoreItemMap = null;
        Store storeBoughtFrom = customer.getCurrentOrder() instanceof OneStoreOrder ?
                ((OneStoreOrder) customer.getCurrentOrder()).getStoreOrderMadeFrom()
                :
                null;
        integerStoreItemMap = SessionUtils.getCurrentZone(req).getAllStoreItemsForSaleInCurrentStoreForOrder(storeBoughtFrom);
        LinkedList<StoreItemDTO> storeItemDTOList = new LinkedList<>();

        for(StoreItem storeItem : integerStoreItemMap.values()) {
            storeItemDTOList.add(new StoreItemDTO(storeItem));
        }

        return gson.toJson(storeItemDTOList);
    }

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
                synchronized (customer) {
                    startNewOrderForCustomer(customer, req);
                }
                break;
            case ADD_ITEMS_TO_ORDER:
                synchronized (customer) {
                    addItemsToOrder(customer, req);
                }
                break;
            case ADD_DISCOUNT:
                synchronized (customer) {
                    addDiscountToOrder(customer, req);
                }
                break;
            case ACCEPT_ORDER:
                synchronized (customer) {
                    try {
                        customer.completeCurrentOrder();
                    } catch (NegativeAmountOfItemInException ignored) { }
                }
                break;
            case CANCEL_ORDER:
                synchronized (customer) {
                    customer.cancelCurrentOrder();
                }
                break;
            case GIVE_FEEDBACK_TO_STORE:
                synchronized (customer) {
                    giveFeedbackToStore(customer, req);
                }
                break;
            case DONE_FEEDBACK:
                synchronized (customer) {
                    customer.doneGiveFeedBack();
                }
                break;
        }

        return jsonRes;
    }

    private void giveFeedbackToStore(Customer customer, HttpServletRequest req) {
        String STORE_ID_PARAM = "storeId";
        String RATE_PARAM = "rate";
        String DESCRIPTION_PARAM = "description";

        int storeId = Integer.parseInt(req.getParameter(STORE_ID_PARAM));
        int rate = Integer.parseInt(req.getParameter(RATE_PARAM));
        String description = req.getParameter(DESCRIPTION_PARAM);

        customer.giveFeedBackToStore(storeId, rate, description);
    }

    private void addDiscountToOrder(Customer customer, HttpServletRequest req) {
        String DISCOUNT_ID_PARAM = "discountId";
        String OFFER_ID_PARAM = "offerId";
        int discountId = Integer.parseInt(req.getParameter(DISCOUNT_ID_PARAM));
        Offer offerChoose = null;
        Discount discountToUse = customer.getDiscountById(discountId);

        if(discountToUse.getThenGet().getOperator().equals("ONE-OF")) {
            int offerId = Integer.parseInt(req.getParameter(OFFER_ID_PARAM));
            offerChoose = discountToUse.getOfferById(offerId);
        }

        try {
            customer.useDiscountOfCurrentOrder(discountToUse, offerChoose);
        } catch (NegativeAmountOfItemInException ignored) { }
    }

    private void addItemsToOrder(Customer customer, HttpServletRequest req) {
        String CART_PARAM = "cart";
        Gson gson = new Gson();
        String itemsAndAmountStr = req.getParameter(CART_PARAM);
        ItemIdAndAmountData[] itemIdAndAmountDataArr = gson.fromJson(itemsAndAmountStr, ItemIdAndAmountData[].class);
        for(ItemIdAndAmountData itemIdAndAmountData : itemIdAndAmountDataArr) {
            try {
                customer.addItemToCurrentOrder(itemIdAndAmountData.getId(), itemIdAndAmountData.getAmount());
            } catch (NegativeAmountOfItemInException ignored) { }
        }
        try {
            customer.continueCurrentOrderToDiscounts();
        } catch (NegativeAmountOfItemInException ignored) {
        }
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
        String orderTypeStr = req.getParameter(ORDER_TYPE_PARAM).toUpperCase();
        OrderType orderType;
        if(orderTypeStr.equals("DYNAMIC")) {
            orderType = OrderType.DYNAMIC_ORDER;
        }
        else {
            orderType = OrderType.ONE_STORE_ORDER;
        }

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

    private static class ItemIdAndAmountData{
        private int id;
        private double amount;

        public int getId() {
            return id;
        }

        public double getAmount() {
            return amount;
        }
    }
}
