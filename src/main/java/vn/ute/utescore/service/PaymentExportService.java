package vn.ute.utescore.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import vn.ute.utescore.model.ThanhToan;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PaymentExportService {

    public ByteArrayInputStream exportToExcel(List<ThanhToan> list) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Báo cáo Thanh toán");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            nf.setMaximumFractionDigits(0);

            // ===== HEADER =====
            String[] headers = {"Mã TT", "Mã Đơn", "Số tiền nhận", "Phương thức", "Trạng thái", "Ngày thanh toán", "Ghi chú"};
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===== DỮ LIỆU =====
            int rowIdx = 1;
            for (ThanhToan p : list) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getMaThanhToan());
                row.createCell(1).setCellValue(p.getThueSan() != null ? p.getThueSan().getMaDonDat() : 0);
                row.createCell(2).setCellValue(p.getSoTienNhan() != null ? nf.format(p.getSoTienNhan()) + " ₫" : "");
                row.createCell(3).setCellValue(p.getPhuongThuc());
                row.createCell(4).setCellValue(p.getTrangThaiThanhToan());
                row.createCell(5).setCellValue(p.getNgayThanhToan() != null ? p.getNgayThanhToan().format(df) : "");
                row.createCell(6).setCellValue(p.getGhiChu() != null ? p.getGhiChu() : "");
            }

            // Auto resize
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
