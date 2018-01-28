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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String userName = SessionUtils.getUsername(req);
        UsersManager usersManager = ServletContextUtils.getServerUserManager(getServletContext());

        if(userName == null){
            PlayerSignInDto playerSignInDto = getPlayerSignInDtoFromRequest(req);
            if(usersManager.isExist(playerSignInDto.getPlayerName())){
                // user name is taken.
                //todo: redirect to relevant page
            }
            else
            {
              usersManager.addUser(playerSignInDto);
              SessionUtils.initNewSession(req,playerSignInDto.getPlayerName());
            }
        }
        else // user already exist
        {
            // redirect to lobby
        }
    }

    private PlayerSignInDto getPlayerSignInDtoFromRequest(HttpServletRequest req)
    {
        PlayerSignInDto playerSignInDto = new PlayerSignInDto();
        playerSignInDto.setPlayerName(req.getParameter("playerName"));
        playerSignInDto.setComputer(req.getParameter("isComputer")== null? false:true);
        playerSignInDto.setHuman(req.getParameter("isHuman")== null? false:true);
        return playerSignInDto;
    }
}
