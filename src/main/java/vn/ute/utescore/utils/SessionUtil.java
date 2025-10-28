package vn.ute.utescore.utils;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    private static final String EMAIL_KEY = "customerEmail";
    private static final String ROLE_KEY = "currentRole";

    public static void setCustomer(HttpSession session, String email, String roleName) {
        session.setAttribute(EMAIL_KEY, email);
        session.setAttribute(ROLE_KEY, roleName);
    }

    public static String getCustomerEmail(HttpSession session) {
        Object email = session.getAttribute(EMAIL_KEY);
        return email != null ? email.toString() : null;
    }

    public static String getCurrentRole(HttpSession session) {
        Object role = session.getAttribute(ROLE_KEY);
        return role != null ? role.toString() : null;
    }

    public static void clearSession(HttpSession session) {
        session.removeAttribute(EMAIL_KEY);
        session.removeAttribute(ROLE_KEY);
    }
}
