package vn.ute.utescore.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ute.utescore.service.CustomerVnPayService;

import java.math.BigDecimal;

@Controller
@RequestMapping("/api/vnpay")
public class CustomerVnPayController {

    private final CustomerVnPayService vnPayService;
    public CustomerVnPayController(CustomerVnPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    /** ✅ API tạo thanh toán và tự redirect sang VNPay */
    @GetMapping("/create-payment")
    public String createPayment(
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request) {

        // 💰 Ép kiểu an toàn sang long (VNPay yêu cầu số nguyên)
        long cocAmount = amount.longValue();

        // ✅ Tạo URL thanh toán
        String paymentUrl = vnPayService.createPaymentUrl(request, cocAmount, orderInfo);

        // ✅ Redirect tới trang VNPay
        return "redirect:" + paymentUrl;
    }

    /** ✅ Callback từ VNPay */
    @GetMapping("/return")
    public String handleReturn(HttpServletRequest request, Model model) {
        // ✅ Xử lý callback và cập nhật DB
        vnPayService.handleReturn(request);

        String txnRef = request.getParameter("vnp_TxnRef");
        String responseCode = request.getParameter("vnp_ResponseCode");

        model.addAttribute("txnRef", txnRef);

        // ✅ Nếu thanh toán thành công
        if ("00".equals(responseCode)) {
            return "customer/payment-success";
        }

        // ❌ Nếu thất bại thì quay lại home
        return "redirect:/customer/home?error=payment-failed";
    }
}
