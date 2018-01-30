package Servlets.Lobby;

import ServletContexts.UsersManager;
import ServletUtils.ServletContextUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "OnlineUsersServlet", urlPatterns = {"/Lobby/OnlineUsers"})
public class OnlineUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        UsersManager usersManager= ServletContextUtils.getServerUserManager(getServletContext());
        List<String> onlineUsers = usersManager.getAllUsers();
        Gson gson = new Gson();
        String onlineUsersJsonData = gson.toJson(onlineUsers);
        try (PrintWriter out = resp.getWriter()) {
            out.write(onlineUsersJsonData);
            out.flush();
        }
    }
}
