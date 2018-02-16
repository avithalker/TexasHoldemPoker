package Servlets;

import PokerDtos.PlayerSignInDto;
import ServletContexts.UsersManager;
import ServletUtils.ServletContextUtils;
import ServletUtils.SessionUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {

    private final String ERROR_USERNAME_EXIST_PAGE_URL ="../Pages/ErrorUserAlreadyExistPage.html";
    private final String LOBBY_PAGE_URL ="../Pages/LobbyPage.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String userName = SessionUtils.getUsername(req);
        UsersManager usersManager = ServletContextUtils.getServerUserManager(getServletContext());

        if(userName == null){
            PlayerSignInDto playerSignInDto = getPlayerSignInDtoFromRequest(req);
            if(usersManager.isExist(playerSignInDto.getPlayerName())){
                // user name is taken.
                resp.sendRedirect(ERROR_USERNAME_EXIST_PAGE_URL);
                return;
            }
            else
            {
              usersManager.addUser(playerSignInDto);
              SessionUtils.initNewSession(req,playerSignInDto.getPlayerName());
            }
        }
        resp.sendRedirect(LOBBY_PAGE_URL);
    }

    private PlayerSignInDto getPlayerSignInDtoFromRequest(HttpServletRequest req) {
        PlayerSignInDto playerSignInDto = new PlayerSignInDto();
        playerSignInDto.setPlayerName(req.getParameter("playerName"));
        String type = req.getParameter("playerType");
        if (type == "computer") {
            playerSignInDto.setComputer(true);
        } else {
            playerSignInDto.setComputer(false);
        }
        return playerSignInDto;
    }
}
