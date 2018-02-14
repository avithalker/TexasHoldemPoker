package Servlets.GameRoom;

import Common.GlobalDefines.RoomStatuses;
import PokerDtos.SimpleResultDto;
import ServletContexts.GameRoom;
import ServletContexts.GameRoomManager;
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

@WebServlet(name = "IsGameStartedServlet", urlPatterns = {"/GameRoom/isGameStarted"})
public class IsGameStartedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String userName = SessionUtils.getUsername(req);
        if(userName == null){
            throw new ServletException("Error- player is not logged in!");
        }

        GameRoomManager lobby = ServletContextUtils.getServerLobby(getServletContext());
        GameRoom gameRoom = lobby.getRoomByPlayer(userName);
        if(gameRoom == null)
            throw new ServletException("Error- player didn't join to a game room!");

        RoomStatuses gameStatus = gameRoom.getGameStatus();
        SimpleResultDto result;

        if(gameStatus == RoomStatuses.Active)
            result = new SimpleResultDto(true);
        else
            result = new SimpleResultDto(false);

        Gson jsonParser = new Gson();
        String resultJson = jsonParser.toJson(result);

        try (PrintWriter out = resp.getWriter()) {
            out.write(resultJson);
            out.flush();
        }

    }
}
