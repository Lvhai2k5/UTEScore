package vn.ute.utescore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.repository.ThanhToanRepository;
import vn.ute.utescore.utils.SessionUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee/payment")
public class PaymentController {

	@Autowired
	private NhanVienRepository nhanVienRepository;
	
	@Autowired
    private ThanhToanRepository thanhToanRepository;

    // ✅ Khi vào /employee/payment → load tất cả đơn "Đặt cọc" có ghiChu = "1"
    @GetMapping
    public String listDeposits(HttpSession session,Model model) {
    	String email = SessionUtil.getCustomerEmail(session);

        if (email != null) {
            Optional<NhanVien> optionalNV = nhanVienRepository.findByEmail(email);

            if (optionalNV.isPresent()) {
                NhanVien nhanVien = optionalNV.get();
                model.addAttribute("nhanVienDangNhap", nhanVien);
            } else {
                model.addAttribute("nhanVienDangNhap", null);
            }
        } else {
                return "redirect:/login";
        }
        List<ThanhToan> payments = thanhToanRepository.findDepositsCheckedIn("Hoàn tất", "1");
        model.addAttribute("payments", payments);
        return "employee/payment";
    }

 // ✅ Hiển thị QR để khách quét
    @GetMapping("/vnpay/{id}")
    public String showVNPay(@PathVariable("id") Integer id, Model model) {
        Optional<ThanhToan> opt = thanhToanRepository.findById(id);
        if (opt.isPresent()) {
            ThanhToan p = opt.get();
            model.addAttribute("paymentId", p.getMaThanhToan());
            model.addAttribute("customer", p.getThueSan() != null && p.getThueSan().getKhachHang() != null
                    ? p.getThueSan().getKhachHang().getHoTen()
                    : "Khách lẻ");
            BigDecimal tongTien = p.getThueSan().getTongTien();
            BigDecimal tienCoc = p.getThueSan().getTienCocBatBuoc();
            BigDecimal soTienConLai = tongTien.subtract(tienCoc);

            model.addAttribute("amount", soTienConLai);


            // ✅ QR chứa hostname thật
            String hostname = "LeVuHai"; // theo yêu cầu của bạn
            String qrLink = "http://" + hostname + ":8080/employee/payment/vnpay/confirm/" + p.getMaThanhToan();
            model.addAttribute("qrData", qrLink);

            return "employee/payment_qr";
        }
        model.addAttribute("msg", "❌ Không tìm thấy đơn thanh toán #" + id);
        return "employee/payment_result";
    }

    // ✅ Khi khách quét QR → hiển thị trang xác nhận
    @GetMapping("/vnpay/confirm/{id}")
    public String confirmPage(@PathVariable("id") Integer id, Model model) {
        Optional<ThanhToan> opt = thanhToanRepository.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("payment", opt.get());
            return "employee/payment_confirm";
        }
        model.addAttribute("msg", "❌ Không tìm thấy đơn thanh toán #" + id);
        return "employee/payment_result";
    }

    // ✅ Khi khách bấm nút xác nhận → lưu xuống DB
    @PostMapping("/vnpay/confirm/{id}")
    public String confirmPayment(@PathVariable("id") Integer id, Model model) {
        Optional<ThanhToan> opt = thanhToanRepository.findById(id);
        if (opt.isPresent()) {
            ThanhToan p = opt.get();
            p.setLoaiThanhToan("Đã thanh toán");
            p.setPhuongThuc("Chuyển khoản");
            p.setNgayThanhToan(LocalDateTime.now());
            thanhToanRepository.save(p);
            model.addAttribute("msg", "✅ Thanh toán thành công cho đơn #" + p.getMaThanhToan());
        } else {
            model.addAttribute("msg", "❌ Không tìm thấy đơn cần thanh toán");
        }
        return "employee/payment_result";
    }
    
    @PostMapping("/method")
    public String handlePaymentMethod(
            @RequestParam("paymentId") Integer paymentId,
            @RequestParam("method") String method,
            Model model) {

        if ("Tiền mặt".equals(method)) 
        {
        	Optional<ThanhToan> opt = thanhToanRepository.findById(paymentId);
            if (opt.isPresent()) {
                ThanhToan p = opt.get();
                p.setLoaiThanhToan("Đã thanh toán");
                p.setPhuongThuc("Tiền mặt");
                p.setNgayThanhToan(LocalDateTime.now());
                thanhToanRepository.save(p);
                model.addAttribute("msg", "✅ Thanh toán thành công cho đơn #" + p.getMaThanhToan());
                return "employee/payment";
            } 
        } 
        else
        {
            model.addAttribute("message", "Vui lòng thanh toán chuyển khoản cho đơn #" + paymentId);
        }
        return "employee/payment"; 
    }

}
