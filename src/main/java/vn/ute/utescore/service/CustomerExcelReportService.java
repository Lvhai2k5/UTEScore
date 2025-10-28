package vn.ute.utescore.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public interface CustomerExcelReportService {
    void exportReport(HttpServletResponse response,
                      String customerName,
                      LocalDateTime fromDate,
                      LocalDateTime toDate) throws IOException;

    ByteArrayInputStream buildExcelWithCharts(Map<String, Object> reportData);
}
