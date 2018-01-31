package Servlets.Lobby;

import Common.ActionResult;
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

@WebServlet(name = "CreateGameRoomsServlet", urlPatterns = {"/Lobby/CreateGameRoom"})
public class CreateGameRoomServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String userName = SessionUtils.getUsername(req);
        if(userName == null){
            //todo: redirect to error page
        }
        GameRoomManager lobby = ServletContextUtils.getServerLobby(getServletContext());
        String roomSettings = getRoomSettingsParam(req);
        ActionResult result = lobby.addNewRoom(roomSettings,userName);

        Gson jsonParser = new Gson();
        String resultJson = jsonParser.toJson(result);
        try (PrintWriter out = resp.getWriter()) {
            out.write(resultJson);
            out.flush();
        }
    }

    private String getRoomSettingsParam(HttpServletRequest req){
        return req.getParameter("settingsContent");
    }
}
