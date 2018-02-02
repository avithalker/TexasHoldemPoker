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

@WebServlet(name = "JoinRoomServlet", urlPatterns = {"/Lobby/joinRoom"})
public class JoinRoomServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Gson jsonParser = new Gson();
        String resultJson;

        String userName = SessionUtils.getUsername(req);
        if(userName == null){
            ActionResult result = new ActionResult(false,"error- user is not logged in");
            resultJson = jsonParser.toJson(result);
        }
        else {
            GameRoomManager lobby = ServletContextUtils.getServerLobby(getServletContext());
            UsersManager usersManager = ServletContextUtils.getServerUserManager(getServletContext());

            PlayerSignInDto userDetails = usersManager.getUser(userName);
            ActionResult result = lobby.joinRoom(getRoomNameFromRequest(req), userDetails);
            resultJson = jsonParser.toJson(result);
        }

        try (PrintWriter out = resp.getWriter()) {
            out.write(resultJson);
            out.flush();
        }
    }

    private String getRoomNameFromRequest(HttpServletRequest req)
    {
        String roomName = req.getParameter("gameName");
        return roomName;
    }
}
