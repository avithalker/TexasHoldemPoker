package Servlets.GameRoom;

import Common.ActionResult;
import Common.GlobalDefines.PokerAction;
import PokerDtos.PokerActionDto;
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

@WebServlet(name = "MakePokerActionServlet", urlPatterns = {"/GameRoom/makeAction"})
public class MakePokerActionServlet extends HttpServlet {

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

        PokerActionDto pokerActionDto = getPokerActionFromRequest(req);
        ActionResult result = gameRoom.MakePokerAction(userName,pokerActionDto.getPokerAction(),pokerActionDto.getValue());

        Gson jsonParser = new Gson();
        String resultJson = jsonParser.toJson(result);
        try (PrintWriter out = resp.getWriter()) {
            out.write(resultJson);
            out.flush();
        }

    }

    private PokerActionDto getPokerActionFromRequest(HttpServletRequest req){
        PokerActionDto pokerActionDto = new PokerActionDto();
        pokerActionDto.setPokerAction(Integer.parseInt(req.getParameter("action")));
        pokerActionDto.setValue(Integer.parseInt(req.getParameter("value")));
        return pokerActionDto;
    }

}
