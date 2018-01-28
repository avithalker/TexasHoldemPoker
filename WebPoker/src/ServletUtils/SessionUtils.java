package ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    private static final String sessionIdentityAtrr = "userName";

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(sessionIdentityAtrr) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void initNewSession(HttpServletRequest request, String userName){
        request.getSession(true).setAttribute(sessionIdentityAtrr,userName);
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
