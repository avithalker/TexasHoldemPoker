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


@WebServlet(name = "ExitEndGameServlet", urlPatterns = {"/GameRoom/exitEndGame"})
public class ExitEndGameServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SimpleResultDto resultDto;

        String userName = SessionUtils.getUsername(req);
        if (userName == null) {
            throw new ServletException("Error- player is not logged in!");
        }

        GameRoomManager lobby = ServletContextUtils.getServerLobby(getServletContext());
        GameRoom gameRoom = lobby.getRoomByPlayer(userName);
        if (gameRoom == null) {
            resultDto = new SimpleResultDto(true);
        } else {
            RoomStatuses roomStatus = gameRoom.getGameStatus();

            if (roomStatus == RoomStatuses.Pending || roomStatus == RoomStatuses.End ||
                    (roomStatus == RoomStatuses.Active && gameRoom.isHandRoundEnded())) {
                lobby.leaveRoom(userName); // will leave also from the room itself....
                resultDto = new SimpleResultDto(true);
            } else {
                resultDto = new SimpleResultDto(false);
            }
        }
        Gson jsonParser = new Gson();
        String resultJson = jsonParser.toJson(resultDto);

        try (PrintWriter out = resp.getWriter()) {
            out.write(resultJson);
            out.flush();
        }
    }
}
