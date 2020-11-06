package utils;

import SDM.Zone;
import constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static Zone getCurrentZone (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (Zone)session.getAttribute(Constants.CURRENT_ZONE);
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}