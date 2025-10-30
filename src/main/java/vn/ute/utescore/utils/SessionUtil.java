package vn.ute.utescore.utils;

import jakarta.servlet.http.HttpSession;
import vn.ute.utescore.model.KhachHang;

public class SessionUtil {

    private static final String EMAIL_KEY = "customerEmail";
    private static final String ROLE_KEY = "currentRole";
    private static final String KHACHHANG_KEY = "currentKhachHang";

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
    public static KhachHang getKhachHang(HttpSession session) {
        Object kh = session.getAttribute(KHACHHANG_KEY);
        if (kh instanceof KhachHang) {
            return (KhachHang) kh;
        }
        return null;
    }
    public static void clearSession(HttpSession session) {
        session.removeAttribute(EMAIL_KEY);
        session.removeAttribute(ROLE_KEY);
    }
    public static String getEmail(HttpSession session) {
        Object o = session.getAttribute("email");
        return o == null ? null : o.toString();
    }
    public static String getRole(HttpSession session) {
        Object o = session.getAttribute("role");
        return o == null ? null : o.toString();
    }
    public static String getEmployeeEmail(HttpSession session){
        Object v = session.getAttribute("EMPLOYEE_EMAIL");
        return v == null ? null : v.toString();
    }
}
