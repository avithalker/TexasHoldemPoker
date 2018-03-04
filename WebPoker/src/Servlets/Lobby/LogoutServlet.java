package Servlets.Lobby;

import ServletContexts.UsersManager;
import ServletUtils.ServletContextUtils;
import ServletUtils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "LogoutServlet", urlPatterns = {"/Lobby/Logout"})
public class LogoutServlet extends HttpServlet {

    private final String HOME_PAGE_URL="/index.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String userName= SessionUtils.getUsername(req);
        if(userName!=null)
        {
            UsersManager usersManager = ServletContextUtils.getServerUserManager(getServletContext());
            usersManager.removeUser(userName);
            SessionUtils.clearSession(req);
        }
        resp.sendRedirect(req.getContextPath() + HOME_PAGE_URL);
    }
}
