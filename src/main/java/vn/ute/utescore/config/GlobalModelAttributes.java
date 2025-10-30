package vn.ute.utescore.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @ModelAttribute("sessionEmail")
    public String sessionEmail(HttpSession session) {
        Object v = session.getAttribute("email");
        return v == null ? null : v.toString();
    }

    @ModelAttribute("sessionRole")
    public String sessionRole(HttpSession session) {
        Object v = session.getAttribute("role");
        return v == null ? null : v.toString();
    }
}
