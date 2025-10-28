package vn.ute.utescore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerPaymentResultController {

    @GetMapping("/customer/payment-success")
    public String paymentSuccess(@RequestParam String vnp_TxnRef, Model model) {
        model.addAttribute("txnRef", vnp_TxnRef);
        return "Views/customer/payment-success";
    }

    @GetMapping("/customer/payment-fail")
    public String paymentFail(@RequestParam(required = false) String vnp_TxnRef, Model model) {
        model.addAttribute("txnRef", vnp_TxnRef);
        return "Views/customer/payment-fail";
    }
}
