package vn.ute.utescore.service;

import jakarta.servlet.http.HttpServletRequest;

public interface CustomerVnPayService {
    String createPaymentUrl(HttpServletRequest request, long amount, String orderInfo);
    String handleReturn(HttpServletRequest request);
}
