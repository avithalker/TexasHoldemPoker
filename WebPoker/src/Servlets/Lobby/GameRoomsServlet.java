package Servlets.Lobby;

import PokerDtos.GameRoomDto;
import ServletContexts.GameRoomManager;
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

@WebServlet(name = "GameRoomsServlet", urlPatterns = {"/Lobby/gameRooms"})
public class GameRoomsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        GameRoomManager lobby = ServletContextUtils.getServerLobby(getServletContext());
        List<GameRoomDto> roomsDetails = lobby.getAllRoomsDetails();

        Gson jsonParser = new Gson();
        String roomsDetailsJson = jsonParser.toJson(roomsDetails);
        try (PrintWriter out = resp.getWriter()) {
            out.write(roomsDetailsJson);
            out.flush();
        }
    }
}
