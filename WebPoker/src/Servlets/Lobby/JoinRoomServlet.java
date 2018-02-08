package Servlets.Lobby;

import Common.ActionResult;
import Common.gameExceptions.InvalidOperationException;
import PokerDtos.PlayerSignInDto;
import ServletContexts.GameRoom;
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
        GameRoomManager lobby = ServletContextUtils.getServerLobby(getServletContext());
        ActionResult result;
        String resultJson;

        String userName = SessionUtils.getUsername(req);
        if(userName == null){
            result = new ActionResult(false,"error- user is not logged in");
            resultJson = jsonParser.toJson(result);
        }
        else {
            UsersManager usersManager = ServletContextUtils.getServerUserManager(getServletContext());

            PlayerSignInDto userDetails = usersManager.getUser(userName);
            result = lobby.joinRoom(getRoomNameFromRequest(req), userDetails);
            resultJson = jsonParser.toJson(result);
        }

        try (PrintWriter out = resp.getWriter()) {
            out.write(resultJson);
            out.flush();
        }

        try {

            if (result.isSucceed()) {
                GameRoom gameRoom = lobby.getRoomByName(getRoomNameFromRequest(req));
                if (gameRoom.isGameRoomFull())
                    gameRoom.startGame();
            }
        }catch (InvalidOperationException e)
        {
            throw new ServletException(e.getMessage());
        }
    }

    private String getRoomNameFromRequest(HttpServletRequest req)
    {
        String roomName = req.getParameter("gameName");
        return roomName;
    }
}
