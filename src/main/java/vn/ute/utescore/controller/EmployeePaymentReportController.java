package vn.ute.utescore.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.repository.ThanhToanRepository;
import vn.ute.utescore.service.PaymentExportService;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/employee/report")
public class EmployeePaymentReportController {

    private final ThanhToanRepository thanhToanRepository;
    private final PaymentExportService paymentExportService;

    public EmployeePaymentReportController(ThanhToanRepository thanhToanRepository,
                                           PaymentExportService paymentExportService) {
        this.thanhToanRepository = thanhToanRepository;
        this.paymentExportService = paymentExportService;
    }

    @GetMapping
    public String viewPayments(@RequestParam(value = "date", required = false) String dateStr, Model model) {
        LocalDate date = (dateStr != null && !dateStr.isEmpty())
                ? LocalDate.parse(dateStr)
                : LocalDate.now();
        String today = LocalDate.now().toString();
        List<ThanhToan> payments = thanhToanRepository.findAllByStatusAndDate("Đã thanh toán", "Hoàn tiền",date.toString());
        // ✅ Tính tổng: cộng "Đã thanh toán", trừ "Hoàn tiền"
        BigDecimal total = payments.stream()
                .map(p -> {
                    BigDecimal value = p.getSoTienNhan() != null ? p.getSoTienNhan() : BigDecimal.ZERO;
                    // Nếu là hoàn tiền → trừ
                    return p.getLoaiThanhToan().equalsIgnoreCase("Hoàn tiền")
                            ? value.negate()
                            : value;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ✅ Định dạng tiền Việt Nam
        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        vnFormat.setMaximumFractionDigits(0);
        String totalFormatted = vnFormat.format(total) + " ₫";

        model.addAttribute("selectedDate", date);
        model.addAttribute("payments", payments);
        model.addAttribute("totalAmount", totalFormatted);

        return "employee/report";
    }


    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportPayments(@RequestParam("date") String dateStr) throws Exception {
        LocalDate date = LocalDate.parse(dateStr);
        String today = LocalDate.now().toString();
        List<ThanhToan> list = thanhToanRepository.findAllByStatusAndDate("Đã thanh toán","Hoàn tiền",date.toString());

        ByteArrayInputStream in = paymentExportService.exportToExcel(list);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ThanhToan_" + date + ".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
