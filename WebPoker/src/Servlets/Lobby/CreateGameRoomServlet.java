package Servlets.Lobby;

import Common.ActionResult;
import ServletContexts.GameRoomManager;
import ServletUtils.ServletContextUtils;
import ServletUtils.SessionUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

@WebServlet(name = "CreateGameRoomsServlet", urlPatterns = {"/Lobby/CreateGameRoom"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class CreateGameRoomServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String userName = SessionUtils.getUsername(req);
        if(userName == null){
            //todo: redirect to error page
        }
        GameRoomManager lobby = ServletContextUtils.getServerLobby(getServletContext());
        Collection<Part> parts = req.getParts();
        StringBuilder roomSettings = new StringBuilder();

        for (Part part : parts) {
            //to write the content of the file to a string
            roomSettings.append(readFromInputStream(part.getInputStream()));
        }
        ActionResult result = lobby.addNewRoom(roomSettings.toString(),userName);

        Gson jsonParser = new Gson();
        String resultJson = jsonParser.toJson(result);
        try (PrintWriter out = resp.getWriter()) {
            out.write(resultJson);
            out.flush();
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
