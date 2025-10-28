package vn.ute.utescore.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.repository.CustomerThanhToanRepository;
import vn.ute.utescore.service.CustomerExcelReportService;

import java.io.*;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CustomerExcelReportServiceImpl implements CustomerExcelReportService {

    // ✅ Logger thay thế cho Lombok @Slf4j
    private static final Logger log = LoggerFactory.getLogger(CustomerExcelReportServiceImpl.class);

    private final CustomerThanhToanRepository thanhToanRepo;

    public CustomerExcelReportServiceImpl(CustomerThanhToanRepository thanhToanRepo) {
        this.thanhToanRepo = thanhToanRepo;
    }

    /* =====================================================
       🧾 XUẤT FILE CHI TIÊU KHÁCH HÀNG (TRỰC TIẾP)
    ===================================================== */
    @Override
    public void exportReport(HttpServletResponse response,
                             String customerName,
                             LocalDateTime fromDate,
                             LocalDateTime toDate) throws IOException {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            // 1️⃣ SHEET 1: DỮ LIỆU CHI TIÊU
            XSSFSheet sheet = workbook.createSheet("ChiTieu_Theo_Thang");
            CellStyle headerStyle = createHeaderStyle(workbook);

            String[] headers = {"Tháng", "Tổng chi tiêu (VNĐ)", "Số đơn đặt", "Số hoàn tiền", "Số hủy"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ✅ 3 tháng gần nhất
            YearMonth current = YearMonth.from(LocalDate.now());
            List<YearMonth> months = List.of(
                    current.minusMonths(2),
                    current.minusMonths(1),
                    current
            );

            Map<String, BigDecimal> tongChiTieu = new LinkedHashMap<>();
            Map<String, Integer> soDon = new LinkedHashMap<>();
            Map<String, Integer> soHoan = new LinkedHashMap<>();
            Map<String, Integer> soHuy = new LinkedHashMap<>();

            months.forEach(m -> {
                String key = m.format(DateTimeFormatter.ofPattern("MM/yyyy"));
                tongChiTieu.put(key, BigDecimal.ZERO);
                soDon.put(key, 0);
                soHoan.put(key, 0);
                soHuy.put(key, 0);
            });

            // ⚙️ Lấy danh sách thanh toán từ DB
            List<ThanhToan> thanhToans = thanhToanRepo.findAll();

            for (ThanhToan tt : thanhToans) {
                if (tt.getNgayThanhToan() == null) continue;
                YearMonth ym = YearMonth.from(tt.getNgayThanhToan());
                String key = ym.format(DateTimeFormatter.ofPattern("MM/yyyy"));
                if (!tongChiTieu.containsKey(key)) continue;

                BigDecimal soTien = Optional.ofNullable(tt.getSoTienNhan()).orElse(BigDecimal.ZERO);
                String loai = Optional.ofNullable(tt.getLoaiThanhToan()).orElse("").toLowerCase();

                if (loai.contains("hoàn")) {
                    soHoan.computeIfPresent(key, (k, v) -> v + 1);
                    tongChiTieu.computeIfPresent(key, (k, v) -> v.subtract(soTien));
                } else if (loai.contains("thanh toán") || loai.contains("đặt cọc")) {
                    soDon.computeIfPresent(key, (k, v) -> v + 1);
                    tongChiTieu.computeIfPresent(key, (k, v) -> v.add(soTien));
                } else if (loai.contains("hủy")) {
                    soHuy.computeIfPresent(key, (k, v) -> v + 1);
                }
            }

            int rowIdx = 1;
            for (String key : tongChiTieu.keySet()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(key);
                row.createCell(1).setCellValue(tongChiTieu.get(key).doubleValue());
                row.createCell(2).setCellValue(soDon.get(key));
                row.createCell(3).setCellValue(soHoan.get(key));
                row.createCell(4).setCellValue(soHuy.get(key));
            }
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            // 2️⃣ SHEET 2: BIỂU ĐỒ
            XSSFSheet chartSheet = workbook.createSheet("BieuDo_ChiTieu");
            createLineChart(chartSheet, sheet, tongChiTieu.size());
            createBarChart(chartSheet, sheet, tongChiTieu.size());

            // 3️⃣ GHI FILE
            String safeName = removeVietnameseAccents(customerName).replaceAll("\\s+", "_");
            String fileName = "ChiTieu_" + safeName + "_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmm")) + ".xlsx";

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            workbook.write(response.getOutputStream());
            log.info("✅ Xuất file Excel báo cáo chi tiêu thành công: {}", fileName);

        } catch (Exception e) {
            log.error("❌ Lỗi khi xuất báo cáo Excel: {}", e.getMessage(), e);
        }
    }

    /* =====================================================
       📊 BUILD FILE EXCEL (TRẢ VỀ STREAM)
    ===================================================== */
    @Override
    public ByteArrayInputStream buildExcelWithCharts(Map<String, Object> reportData) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet dataSheet = workbook.createSheet("ChiTieu_Theo_Thang");
            CellStyle headerStyle = createHeaderStyle(workbook);

            String[] headers = {"Tháng", "Tổng chi tiêu (VNĐ)", "Số đơn đặt", "Số hoàn tiền", "Số hủy"};
            Row header = dataSheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            List<Map<String, Object>> data = (List<Map<String, Object>>) reportData.getOrDefault("data", new ArrayList<>());
            int rowIdx = 1;
            for (Map<String, Object> item : data) {
                Row row = dataSheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(String.valueOf(item.get("month")));
                row.createCell(1).setCellValue(((Number) item.get("spend")).doubleValue());
                row.createCell(2).setCellValue(((Number) item.get("booked")).intValue());
                row.createCell(3).setCellValue(((Number) item.get("refund")).intValue());
                row.createCell(4).setCellValue(((Number) item.get("cancel")).intValue());
            }

            for (int i = 0; i < headers.length; i++) dataSheet.autoSizeColumn(i);

            XSSFSheet chartSheet = workbook.createSheet("BieuDo_ChiTieu");
            createLineChart(chartSheet, dataSheet, data.size());
            createBarChart(chartSheet, dataSheet, data.size());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            log.info("📊 Tạo file Excel báo cáo chi tiêu thành công (stream)");
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            log.error("❌ Lỗi tạo Excel: {}", e.getMessage(), e);
            return null;
        }
    }

    /* =====================================================
       🎨 STYLE HEADER
    ===================================================== */
    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /* =====================================================
       📈 BIỂU ĐỒ ĐƯỜNG
    ===================================================== */
    private void createLineChart(XSSFSheet chartSheet, XSSFSheet dataSheet, int count) {
        try {
            XSSFDrawing drawing = chartSheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 1, 1, 10, 18);
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("💰 Chi tiêu khách hàng (3 tháng gần nhất)");
            XDDFCategoryAxis bottom = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis left = chart.createValueAxis(AxisPosition.LEFT);
            left.setTitle("VNĐ");

            XDDFDataSource<String> months = XDDFDataSourcesFactory.fromStringCellRange(dataSheet,
                    new CellRangeAddress(1, count, 0, 0));
            XDDFNumericalDataSource<Double> spend = XDDFDataSourcesFactory.fromNumericCellRange(dataSheet,
                    new CellRangeAddress(1, count, 1, 1));

            XDDFChartData data = chart.createData(ChartTypes.LINE, bottom, left);
            data.addSeries(months, spend).setTitle("Chi tiêu", null);
            chart.plot(data);
        } catch (Exception e) {
            log.warn("⚠️ Không thể tạo biểu đồ đường: {}", e.getMessage());
        }
    }

    /* =====================================================
       📊 BIỂU ĐỒ CỘT
    ===================================================== */
    private void createBarChart(XSSFSheet chartSheet, XSSFSheet dataSheet, int count) {
        try {
            XSSFDrawing drawing = chartSheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 11, 1, 20, 18);
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("📊 Số đơn theo trạng thái");
            XDDFCategoryAxis bottom = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis left = chart.createValueAxis(AxisPosition.LEFT);
            left.setTitle("Số lượng");

            XDDFDataSource<String> months = XDDFDataSourcesFactory.fromStringCellRange(dataSheet,
                    new CellRangeAddress(1, count, 0, 0));
            XDDFNumericalDataSource<Double> booked = XDDFDataSourcesFactory.fromNumericCellRange(dataSheet,
                    new CellRangeAddress(1, count, 2, 2));
            XDDFNumericalDataSource<Double> refund = XDDFDataSourcesFactory.fromNumericCellRange(dataSheet,
                    new CellRangeAddress(1, count, 3, 3));
            XDDFNumericalDataSource<Double> cancel = XDDFDataSourcesFactory.fromNumericCellRange(dataSheet,
                    new CellRangeAddress(1, count, 4, 4));

            XDDFChartData data = chart.createData(ChartTypes.BAR, bottom, left);
            data.addSeries(months, booked).setTitle("Đơn đặt", null);
            data.addSeries(months, refund).setTitle("Hoàn tiền", null);
            data.addSeries(months, cancel).setTitle("Hủy", null);
            chart.plot(data);
        } catch (Exception e) {
            log.warn("⚠️ Không thể tạo biểu đồ cột: {}", e.getMessage());
        }
    }

    /* =====================================================
       🔠 BỎ DẤU TIẾNG VIỆT
    ===================================================== */
    private String removeVietnameseAccents(String input) {
        if (input == null) return "";
        String temp = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        return temp.replaceAll("\\p{M}", "").replaceAll("Đ", "D").replaceAll("đ", "d");
    }
}
