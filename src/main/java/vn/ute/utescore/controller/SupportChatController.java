package vn.ute.utescore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.model.ChatMessage;
import vn.ute.utescore.repository.ChatMessageRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/support")
public class SupportChatController {

    private final ChatMessageRepository chatRepo;

    public SupportChatController(ChatMessageRepository chatRepo) {
        this.chatRepo = chatRepo;
    }

    // ========== KHÁCH HÀNG ==========
    // Khách hàng bấm "Chat" -> mở khung chat với nhân viên mặc định (hoặc ?emp=emailNV)
    @GetMapping("/customer")
    public String customerChat(@RequestParam(value = "emp", required = false) String empEmail,
                               HttpSession session, Model model) {
        String customer = (String) session.getAttribute("email"); // đã đăng nhập
        if (customer == null) return "redirect:/login";

        String employee = (empEmail != null && !empEmail.isBlank())
                ? empEmail
                : "employee@ute.vn"; // TODO: chọn theo ca trực/nhân viên online

        model.addAttribute("sender", customer);
        model.addAttribute("receiver", employee);
        return "customer/chat";
    }

    // ========== NHÂN VIÊN ==========
    // Trang inbox liệt kê khách đã/đang chat (theo thời gian mới nhất)
    @GetMapping("/employee/inbox")
    public String employeeInbox(HttpSession session, Model model) {
        String employee = (String) session.getAttribute("email");
        if (employee == null) return "redirect:/login";

        List<Object[]> rows = chatRepo.findThreadsForEmployeeOrdered(employee);
        List<ThreadRow> threads = new ArrayList<>();
        for (Object[] r : rows) {
            ThreadRow tr = new ThreadRow();
            tr.otherEmail = (String) r[0];
            tr.lastTime = String.valueOf(r[1]);
            threads.add(tr);
        }
        model.addAttribute("threads", threads);
        return "employee/chat_list";
    }

    // Nhân viên bấm vào 1 khách cụ thể để vào phòng chat
    @GetMapping("/employee/chat/{customerEmail}")
    public String employeeChat(@PathVariable String customerEmail,
                               HttpSession session, Model model) {
        String employee = (String) session.getAttribute("email");
        if (employee == null) return "redirect:/login";

        model.addAttribute("sender", employee);
        model.addAttribute("receiver", customerEmail);
        return "employee/chat";
    }

    // ========== API: lịch sử hội thoại ==========
    @GetMapping("/history")
    @ResponseBody
    public List<ChatMessage> history(@RequestParam String a, @RequestParam String b) {
        return chatRepo.findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(a, b, b, a);
    }

    // DTO nội bộ để đổ dữ liệu ra inbox
    public static class ThreadRow {
        public String otherEmail;
        public String lastTime;
    }
}
