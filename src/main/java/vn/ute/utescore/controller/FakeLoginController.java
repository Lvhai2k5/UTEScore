package vn.ute.utescore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import vn.ute.utescore.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class FakeLoginController {

	@GetMapping("/fake-login-customer")
	public String fakeLoginCustomer(HttpServletRequest request) {
	    // 🧹 Xóa session cũ (nếu có)
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	        System.out.println("🧹 Session cũ đã bị xóa");
	    }

	    // 🔄 Tạo session mới
	    session = request.getSession(true);

	    String email = "levuhai@example.com"; // 📨 Email mới
	    SessionUtil.setCustomer(session, email, "CUSTOMER");

	    System.out.println("✅ Giả lập đăng nhập thành công với email: " + email);
	    //return "employee/chat";
	    return "customer/home";
	}
}
