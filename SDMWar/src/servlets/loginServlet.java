package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import SDM.SDMEngine;
import com.google.gson.Gson;
import constants.Constants;
import utils.ServletUtils;
import utils.SessionUtils;

public class loginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(req);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        String usernameFromParameter = req.getParameter(Constants.USERNAME);
        usernameFromParameter = usernameFromParameter.trim();

        synchronized (this) {
            LoginResult loginResult = new LoginResult();
            if (userManager.isUserExists(usernameFromParameter)) {
                loginResult.setLoginRes(false);
                loginResult.setLoginName(usernameFromParameter);
            }
            else {
                userManager.addUser(usernameFromParameter);
                req.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                loginResult.setLoginRes(true);
                loginResult.setLoginName(usernameFromParameter);
            }

            Gson gson = new Gson();
            String loginResJson = gson.toJson(loginResult);
            resp.getWriter().println(loginResJson);
        }
    }

    public static class LoginResult {
        private boolean loginRes;
        private String loginName;

        public boolean isLoginRes() {
            return loginRes;
        }

        public void setLoginRes(boolean loginRes) {
            this.loginRes = loginRes;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }
    }
}
