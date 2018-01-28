package ServletUtils;

import ServletContexts.UsersManager;

import javax.servlet.ServletContext;

public class ServletContextUtils {

    private final static String USER_MANAGER_ATRR = "userManager";

    public static UsersManager getServerUserManager(ServletContext servletContext)
    {
        if(servletContext.getAttribute(USER_MANAGER_ATRR) == null)
        {
            servletContext.setAttribute(USER_MANAGER_ATRR,new UsersManager());
        }
        return (UsersManager)servletContext.getAttribute(USER_MANAGER_ATRR);
    }
}
