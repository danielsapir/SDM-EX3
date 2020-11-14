package servlets;

import SDM.*;
import com.google.gson.Gson;
import com.sun.org.apache.xml.internal.serializer.utils.SerializerMessages_es;
import constants.Constants;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;

public class OwnerOpenStoreServlet extends HttpServlet {

    //Request type (aka reqType) constants
    private static final String OPEN_STORE = "open-store";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String reqType = req.getParameter(Constants.REQTYPE);
        User userWhoRequest = userManager.getUserByName(SessionUtils.getUsername(req));
        String response = null;
        Zone currentZone = SessionUtils.getCurrentZone(req);

        if(userWhoRequest.getType() == User.Type.OWNER) {
            response = handlePostRequestType(reqType, (Owner)userWhoRequest, currentZone, req);
        }

        try(PrintWriter out = resp.getWriter()) {
            out.println(response);
            out.flush();
        }
    }

    private String handlePostRequestType(String reqType, Owner owner, Zone zone, HttpServletRequest req) {
        String jsonRes = null;
        Gson gson = new Gson();

        switch (reqType) {
            case OPEN_STORE:
                synchronized (zone) {
                    jsonRes = handleOpenNewStore(owner, zone, req);
                }
                break;
        }

        return jsonRes;
    }

    private String handleOpenNewStore(Owner owner, Zone zone, HttpServletRequest req) {
        Gson gson = new Gson();

        String X_CORDINATE_PARAM = "xCor";
        String Y_CORDINATE_PARAM = "yCor";
        String STORE_NAME_PARAM = "storeName";
        String PPK_PARAM = "ppk";
        String ITEMS_FOR_STORE_PARAM = "itemsForStore";

        Location storeLocation = new Location(
                new Point(
                        Integer.parseInt(req.getParameter(X_CORDINATE_PARAM)),
                        Integer.parseInt(req.getParameter(Y_CORDINATE_PARAM))));

        for(Store store : zone.getAllStores()) {
            if(store.getLocation().getXLocation() == storeLocation.getXLocation() && store.getLocation().getYLocation() == storeLocation.getYLocation()) {
                return gson.toJson(new OpenResult(false));
            }
        }

        String storeName = req.getParameter(STORE_NAME_PARAM);
        int storePPK = Integer.parseInt(req.getParameter(PPK_PARAM));

        Store newOpenedStore = zone.openNewStore(storeName, storeLocation, storePPK);

        ItemIdAndPriceData[] itemIdAndPriceDataArr = gson.fromJson(req.getParameter(ITEMS_FOR_STORE_PARAM), ItemIdAndPriceData[].class);

        for(ItemIdAndPriceData itemIdAndPriceData : itemIdAndPriceDataArr) {
            if(itemIdAndPriceData.getPrice() > 0) {
                newOpenedStore.addNewItem(zone.getItemById(itemIdAndPriceData.getId()), Integer.toString(itemIdAndPriceData.getPrice()));
            }
        }

        return gson.toJson(new OpenResult(true));
    }

    private static class OpenResult {
        boolean result;

        public OpenResult(boolean result) {
            this.result = result;
        }
    }

    private static class ItemIdAndPriceData{
        private int id;
        private int price;

        public int getId() {
            return id;
        }

        public int getPrice() {
            return price;
        }
    }
}
