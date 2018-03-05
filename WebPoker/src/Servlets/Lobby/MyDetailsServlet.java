package Servlets.Lobby;

import Common.ActionResult;
import PokerDtos.PlayerSignInDto;
import ServletContexts.GameRoomManager;
import ServletContexts.UsersManager;
import ServletUtils.ServletContextUtils;
import ServletUtils.SessionUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MyDetailsServlet", urlPatterns = {"/Lobby/MyDetails"})
public class MyDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String userName = SessionUtils.getUsername(req);
        if (userName == null) {
            throw new ServletException("error- user is not logged in");
        }

        UsersManager usersManager = ServletContextUtils.getServerUserManager(getServletContext());
        PlayerSignInDto userDetails = usersManager.getUser(userName);

        Gson jsonParser = new Gson();
        String userDetailsJson = jsonParser.toJson(userDetails);

        try (PrintWriter out = resp.getWriter()) {
            out.write(userDetailsJson);
            out.flush();
        }
    }
}
