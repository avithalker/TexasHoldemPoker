package Servlets.GameRoom;

import Common.gameExceptions.InvalidOperationException;
import PokerDtos.PlayerGameStatusDto;
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

@WebServlet(name = "PlayersGameStatusServlet", urlPatterns = {"/GameRoom/PlayersGameStatus"})
public class PlayersGameStatusServlet extends HttpServlet {

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
            PlayerGameStatusDto[] playersGameStatus = gameRoom.getPlayersGameStatus();
            hidePlayersCards(userName,playersGameStatus);
            Gson jsonParser = new Gson();
            String playersGameStatusJson = jsonParser.toJson(playersGameStatus);

            try (PrintWriter out = resp.getWriter()) {
                out.write(playersGameStatusJson);
                out.flush();
            }
        }catch (InvalidOperationException e){
            System.out.println(e.getMessage());
            throw new ServletException("Error- failed to get players game statuses");

        }

    }

    private void hidePlayersCards(String currPlayerName,PlayerGameStatusDto[] playersGameStatus){
        String [] hiddenCards ={"card","card"};
        for(int i = 0;i < playersGameStatus.length;i++){
            if(!playersGameStatus[i].isActive())
                continue;
            if(playersGameStatus[i].getPlayerName().compareTo(currPlayerName) == 0)
                continue;

            playersGameStatus[i].setPlayerCards(hiddenCards);
        }
    }
}
