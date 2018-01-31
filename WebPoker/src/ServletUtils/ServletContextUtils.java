package ServletUtils;

import ServletContexts.GameRoomManager;
import ServletContexts.UsersManager;

import javax.servlet.ServletContext;

public class ServletContextUtils {

    private final static String USER_MANAGER_ATRR = "userManager";
    private final static String GAME_ROOM_MANAGER_ATRR = "gameRoomManager";

    public static UsersManager getServerUserManager(ServletContext servletContext)
    {
        if(servletContext.getAttribute(USER_MANAGER_ATRR) == null)
        {
            servletContext.setAttribute(USER_MANAGER_ATRR,new UsersManager());
        }
        return (UsersManager)servletContext.getAttribute(USER_MANAGER_ATRR);
    }

    public static GameRoomManager getServerLobby(ServletContext servletContext){

        if(servletContext.getAttribute(GAME_ROOM_MANAGER_ATRR)==null){
            servletContext.setAttribute(GAME_ROOM_MANAGER_ATRR,new GameRoomManager());
        }
        return (GameRoomManager) servletContext.getAttribute(GAME_ROOM_MANAGER_ATRR);
    }
}
