package Servlets.GameRoom;

import Common.GlobalDefines.RoomStatuses;
import ServletContexts.GameRoom;
import ServletContexts.GameRoomManager;
import ServletUtils.ServletContextUtils;
import ServletUtils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ExitRoomServlet", urlPatterns = {"/GameRoom/exitRoom"})
public class ExitRoomServlet extends HttpServlet {

    private final String LOBBY_PAGE_URL ="../../Pages/LobbyPage.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String userName = SessionUtils.getUsername(req);
        if(userName == null){
            throw new ServletException("Error- player is not logged in!");
        }

        GameRoomManager lobby = ServletContextUtils.getServerLobby(getServletContext());
        GameRoom gameRoom = lobby.getRoomByPlayer(userName);
        if(gameRoom == null) {
            resp.sendRedirect(LOBBY_PAGE_URL);
            return;
        }

        RoomStatuses roomStatus = gameRoom.getGameStatus();

        if(roomStatus == RoomStatuses.Pending || roomStatus == RoomStatuses.End ||
                (roomStatus == RoomStatuses.Active && gameRoom.isHandRoundEnded())) {
            lobby.leaveRoom(userName); // will leave also from the room itself....
            resp.sendRedirect(LOBBY_PAGE_URL);
            return;
        }

        throw new ServletException("Error- Exit action is not allowed right now!");
    }
}
