package Servlets.GameRoom;

import Common.ActionResult;
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

@WebServlet(name = "ReadyToStartServlet", urlPatterns = {"/GameRoom/readyToStart"})
public class ReadyToStartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String userName = SessionUtils.getUsername(req);
        if(userName == null){
            throw new ServletException("Error- player is not logged in!");
        }

        GameRoomManager lobby = ServletContextUtils.getServerLobby(getServletContext());
        GameRoom gameRoom = lobby.getRoomByPlayer(userName);
        if(gameRoom == null)
            throw new ServletException("Error- player didn't join to a game room!");

        boolean isReady = getIsReadyFromRequest(req);
        boolean result = gameRoom.setPlayerIsReadyStatus(userName,isReady);
        ActionResult actionResult;
        if(result)
            actionResult = new ActionResult(true,"");
        else
            actionResult = new ActionResult(false,"Error in updating player's isReady status");

        Gson jsonParser = new Gson();
        String resultJson = jsonParser.toJson(actionResult);

        try (PrintWriter out = resp.getWriter()) {
            out.write(resultJson);
            out.flush();
        }
    }

    private boolean getIsReadyFromRequest(HttpServletRequest req){
        String isReadyStr = req.getParameter("isReady");
        if(isReadyStr.compareTo("true") == 0  || isReadyStr.compareTo("True") == 0)
            return true;
        return false;
    }
}
