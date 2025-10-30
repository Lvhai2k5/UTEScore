package vn.ute.utescore.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.repository.CustomerThanhToanRepository;
import vn.ute.utescore.repository.CustomerThueSanRepository;
import vn.ute.utescore.service.CustomerVnPayService;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CustomerVnPayServiceImpl implements CustomerVnPayService {

    private final CustomerThanhToanRepository thanhToanRepo;
    private final CustomerThueSanRepository thueSanRepo;
    
    public CustomerVnPayServiceImpl(CustomerThanhToanRepository thanhToanRepo,
            CustomerThueSanRepository thueSanRepo) {
this.thanhToanRepo = thanhToanRepo;
this.thueSanRepo = thueSanRepo;
}

    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.url}")
    private String vnp_Url;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    @Value("${vnpay.version}")
    private String vnp_Version;

    /* ======================================================
       🧾 TẠO URL THANH TOÁN VNPay
    ====================================================== */
    @Override
    public String createPaymentUrl(HttpServletRequest request, long cocAmount, String orderInfo) {
        try {
            if (orderInfo == null || !orderInfo.contains("_")) {
                throw new IllegalArgumentException("orderInfo phải có dạng 'sdt_madon'");
            }

            String[] parts = orderInfo.split("_");
            String sdt = parts[0].trim();
            String maDon = parts[1].trim();

            String vnp_CreateDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String vnp_IpAddr = getClientIp(request);
            String vnp_Amount = String.valueOf(cocAmount * 100);
            
            String vnp_TxnRef = "UTES" + System.currentTimeMillis();
            
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", vnp_Amount);
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", maDon);
            vnp_Params.put("vnp_OrderInfo", "Coc_" + sdt + "_" + maDon);
            vnp_Params.put("vnp_OrderType", "deposit");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            List<String> keys = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(keys);

            StringJoiner hashData = new StringJoiner("&");
            StringJoiner query = new StringJoiner("&");

            for (String key : keys) {
                String value = vnp_Params.get(key);
                if (value != null && !value.isBlank()) {
                    String encoded = URLEncoder.encode(value, StandardCharsets.UTF_8);
                    hashData.add(key + "=" + encoded);
                    query.add(key + "=" + encoded);
                }
            }

            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            query.add("vnp_SecureHashType=HMACSHA512");
            query.add("vnp_SecureHash=" + vnp_SecureHash);

            return vnp_Url + "?" + query;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi tạo URL thanh toán VNPay: " + e.getMessage());
        }
    }

    /* ======================================================
       💳 XỬ LÝ KẾT QUẢ TRẢ VỀ TỪ VNPay
    ====================================================== */
    @Override
    public String handleReturn(HttpServletRequest request) {
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode"); // 00 = success
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");             // Mã tham chiếu giao dịch
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");// Mã giao dịch VNPay
        String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");       // Dạng: Coc_SDT_MaDon
        String vnp_Amount = request.getParameter("vnp_Amount");             // Đơn vị: x100
        long amount = Long.parseLong(vnp_Amount) / 100;

        System.out.println("\n========== 📥 VNPay CALLBACK ==========");
        System.out.println("ResponseCode : " + vnp_ResponseCode);
        System.out.println("OrderInfo    : " + vnp_OrderInfo);
        System.out.println("TxnRef       : " + vnp_TxnRef);
        System.out.println("TransactionNo: " + vnp_TransactionNo);
        System.out.println("Amount       : " + amount);
        System.out.println("=======================================");

        try {
            if ("00".equals(vnp_ResponseCode)) { // ✅ Thanh toán thành công
                String[] parts = vnp_OrderInfo.split("_");
                if (parts.length < 3) {
                    System.err.println("❌ OrderInfo không hợp lệ: " + vnp_OrderInfo);
                    return "redirect:/customer/home?error=invalid-order";
                }

                int maDonDat = Integer.parseInt(parts[2]); // ✅ Lấy mã đơn đúng
                Optional<ThueSan> thueOpt = thueSanRepo.findById(maDonDat);

                if (thueOpt.isPresent()) {
                    ThueSan thueSan = thueOpt.get();

                    // ✅ Tạo mới bản ghi thanh toán
                    ThanhToan thanhToan = new ThanhToan();
                    thanhToan.setThueSan(thueSan);
                    thanhToan.setPhuongThuc("VNPay");
                    thanhToan.setLoaiThanhToan("Đặt cọc");
                    thanhToan.setSoTienNhan(BigDecimal.valueOf(amount));
                    thanhToan.setTrangThaiThanhToan("Thành công");
                    thanhToan.setNgayThanhToan(LocalDateTime.now());
                    thanhToan.setMaGiaoDich(vnp_TransactionNo);
                    thanhToan.setGhiChu("Đặt cọc thành công qua VNPay");
                    thanhToanRepo.save(thanhToan);

                    // ✅ Cập nhật đơn thuê sân
                    thueSan.setGhiChu("Đã đặt cọc thành công");
                    thueSan.setHanGiuCho(LocalDateTime.now().plusMinutes(10));
                    thueSanRepo.save(thueSan);

                    System.out.println("✅ Đã cập nhật thành công đơn #" + maDonDat);
                    return "customer/payment-success";
                } else {
                    System.err.println("❌ Không tìm thấy đơn #" + vnp_OrderInfo);
                    return "customer/payment-failed";
                }
            } else {
                System.out.println("⚠️ Thanh toán thất bại hoặc bị hủy.");
                return "customer/payment-failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "customer/payment-failed";
        }
    }

    /* ======================================================
       ⚙️ HÀM HỖ TRỢ
    ====================================================== */
    private static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    private static String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) hash.append(String.format("%02x", b));
        return hash.toString();
    }
}
