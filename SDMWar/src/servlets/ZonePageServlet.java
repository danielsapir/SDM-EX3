package servlets;

import SDM.*;
import SDM.DTO.ItemDTO;
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

public class ZonePageServlet extends HttpServlet {

    //Request type (aka reqType) constants
    private static final String ZONE_INFO = "zone-info";
    private static final String ALL_STORES = "all-stores";
    private static final String ALL_ITEMS = "all-items";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String reqType = req.getParameter(Constants.REQTYPE);
        Zone currentZone = SessionUtils.getCurrentZone(req);

        String response = handleGetRequestType(reqType, currentZone, req);

        try(PrintWriter out = resp.getWriter()) {
            out.println(response);
            out.flush();
        }
    }

    private String handleGetRequestType(String reqType, Zone currentZone, HttpServletRequest req) {
        String jsonRes = null;
        Gson gson = new Gson();

        switch (reqType) {
            case ZONE_INFO:
                jsonRes = gson.toJson(new ZoneDTO(currentZone));
                break;
            case ALL_STORES:
                LinkedList<storeDTO> storesDTOs = new LinkedList<>();
                for(Store store : currentZone.getAllStores()) {
                    storesDTOs.add(new storeDTO(store));
                }

                jsonRes = gson.toJson(storesDTOs);
                break;
            case ALL_ITEMS:
                LinkedList<ItemDTO> itemsDTOs = new LinkedList<>();
                for(Item item : currentZone.getAllItems()) {
                    itemsDTOs.add(new ItemDTO(item));
                }

                jsonRes = gson.toJson(itemsDTOs);
                break;
        }

        return jsonRes;
    }
}
