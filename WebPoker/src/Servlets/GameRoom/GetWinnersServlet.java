package Servlets.GameRoom;

import Common.gameExceptions.InvalidOperationException;
import PokerDtos.WinnerInfoDto;
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
import java.util.List;

@WebServlet(name = "GetWinnersServlet", urlPatterns = {"/GameRoom/winners"})
public class GetWinnersServlet extends HttpServlet {

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

        try {
            List<WinnerInfoDto> winners = gameRoom.getWinners();

            Gson jsonParser = new Gson();
            String winnersJson = jsonParser.toJson(winners);

            try (PrintWriter out = resp.getWriter()) {
                out.write(winnersJson);
                out.flush();
            }
        }catch (InvalidOperationException e){
            System.out.println(e.getMessage());
            throw new ServletException(e.getMessage());
        }

    }
}
