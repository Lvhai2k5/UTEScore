package vn.ute.utescore.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import vn.ute.utescore.model.GiaThue;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.model.SanBong;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.repository.CustomerGiaThueRepository;
import vn.ute.utescore.repository.CustomerSanBongRepository;
import vn.ute.utescore.repository.CustomerThueSanRepository;
import vn.ute.utescore.service.CustomerChatQueryProcessor;
import vn.ute.utescore.service.CustomerGeminiChatService;
import vn.ute.utescore.utils.SessionUtil;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Chatbot xử lý câu hỏi của khách hàng:
 * - Nếu là câu hỏi quen thuộc → trả lời bằng dữ liệu hệ thống
 * - Nếu không → gọi Gemini AI để phản hồi tự nhiên bằng tiếng Việt có dấu
 */
@Service
public class CustomerChatQueryProcessorImpl implements CustomerChatQueryProcessor {

    private final CustomerThueSanRepository thueSanRepo;
    private final CustomerSanBongRepository sanRepo;
    private final CustomerGiaThueRepository giaRepo;
    private final CustomerGeminiChatService geminiChatService;

    public CustomerChatQueryProcessorImpl(CustomerThueSanRepository thueSanRepo,
                                          CustomerSanBongRepository sanRepo,
                                          CustomerGiaThueRepository giaRepo,
                                          CustomerGeminiChatService geminiChatService) {
        this.thueSanRepo = thueSanRepo;
        this.sanRepo = sanRepo;
        this.giaRepo = giaRepo;
        this.geminiChatService = geminiChatService;
    }

    @Override
    public String handleQuery(String msg, HttpSession session) {
        if (msg == null || msg.isBlank()) {
            return "❓ Bạn vui lòng nhập câu hỏi cụ thể hơn để mình hỗ trợ tốt hơn nhé!";
        }

        String m = msg.toLowerCase(Locale.ROOT).trim();
        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // 👋 Nhận diện lời chào
        if (m.contains("chào") || m.contains("hello") || m.contains("hi ") ||
            m.contains("hey") || m.contains("helo") || m.contains("bot ơi")) {
            return """
                    Xin chào 👋! Tôi là trợ lý ảo **UTEScore** 🤖
                    Bạn có thể hỏi tôi về 💬 *giá thuê sân*, *sân trống*, hoặc *lịch sử đặt sân* nhé!
                    """;
        }

        // 🏷️ 1️⃣ Hỏi giá thuê sân
        if (m.matches(".*(giá|bao nhiêu|thuê sân|mức phí|bao nhieu|sân [0-9]+).*")) {
            List<GiaThue> dsGiaThue = giaRepo.findAll();
            if (dsGiaThue == null || dsGiaThue.isEmpty()) {
                return "Hiện tại mình chưa có thông tin về bảng giá thuê sân. Bạn vui lòng quay lại sau nhé! ⚽";
            }

            String loaiSan = null;
            if (m.contains("5")) loaiSan = "5";
            else if (m.contains("7")) loaiSan = "7";
            else if (m.contains("11")) loaiSan = "11";

            final String loaiSanFinal = loaiSan; // ✅ fix: effectively final

            if (loaiSanFinal != null) {
                List<GiaThue> dsLoc = dsGiaThue.stream()
                        .filter(g -> g.getLoaiSan() != null && g.getLoaiSan().contains(loaiSanFinal))
                        .toList();

                if (dsLoc.isEmpty()) {
                    return "⚠️ Hiện chưa có thông tin giá thuê cho sân " + loaiSanFinal + " người trong hệ thống.";
                }

                StringBuilder sb = new StringBuilder("📋 Giá thuê **sân " + loaiSanFinal + " người** hiện tại:\n\n");
                for (GiaThue g : dsLoc) {
                    sb.append(String.format(
                            "• %s - %s: %s đ/giờ (%s)\n",
                            g.getKhungGioBatDau(),
                            g.getKhungGioKetThuc(),
                            nf.format(g.getGiaThue()),
                            g.getTrangThai() != null ? g.getTrangThai().toLowerCase() : "không rõ trạng thái"
                    ));
                }
                sb.append("\n💡 Bạn muốn mình giúp tra sân trống cho loại sân " + loaiSanFinal + " người không?");
                return sb.toString().trim();
            }

            // Nếu hỏi chung chung
            StringBuilder sb = new StringBuilder("📋 Đây là bảng giá thuê sân hiện tại của UTEScore:\n\n");
            for (GiaThue g : dsGiaThue) {
                sb.append(String.format(
                        "• Sân %s: %s - %s → %s đ/giờ (%s)\n",
                        g.getLoaiSan(),
                        g.getKhungGioBatDau(),
                        g.getKhungGioKetThuc(),
                        nf.format(g.getGiaThue()),
                        g.getTrangThai() != null ? g.getTrangThai().toLowerCase() : "không rõ trạng thái"
                ));
            }
            sb.append("\n💡 Bạn có thể hỏi ví dụ: *“Sân 7 người giá bao nhiêu?”* hoặc *“Sân 11 người buổi tối giá thế nào?”*");
            return sb.toString().trim();
        }

        // ⚽ 2️⃣ Hỏi sân trống / gợi ý sân theo khu vực
        if (m.contains("thuê sân") || m.contains("tìm sân") || m.contains("sân khu") ||
            m.contains("khu a") || m.contains("khu b") || m.contains("khu c") || m.contains("khu d")) {

            String loaiSan = null;
            if (m.contains("5")) loaiSan = "5";
            else if (m.contains("7")) loaiSan = "7";
            else if (m.contains("11")) loaiSan = "11";

            String khuVuc = null;
            if (m.contains("khu a")) khuVuc = "A";
            else if (m.contains("khu b")) khuVuc = "B";
            else if (m.contains("khu c")) khuVuc = "C";
            else if (m.contains("khu d")) khuVuc = "D";

            final String loaiSanFinal = loaiSan; // ✅ fix: effectively final
            final String khuVucFinal = khuVuc;   // ✅ fix: effectively final

            List<SanBong> dsSanBong = sanRepo.findAll();
            List<SanBong> dsLoc = dsSanBong.stream()
                    .filter(s -> (loaiSanFinal == null || s.getLoaiSan().contains(loaiSanFinal)) &&
                                 (khuVucFinal == null || (s.getKhuVuc() != null && s.getKhuVuc().equalsIgnoreCase(khuVucFinal))))
                    .toList();

            if (dsLoc.isEmpty()) {
                return "😥 Rất tiếc, hiện UTEScore chưa tìm thấy sân phù hợp với yêu cầu của bạn.";
            }

            StringBuilder sb = new StringBuilder("⚽ Dưới đây là các sân phù hợp với yêu cầu của bạn:\n\n");
            for (SanBong s : dsLoc) {
                sb.append(String.format(
                        "• %s (%s người, khu %s)\n  🕒 %s - %s\n  📍 %s\n  🌐 %s\n\n",
                        s.getTenSan(),
                        s.getLoaiSan(),
                        s.getKhuVuc(),
                        s.getGioMoCua() != null ? s.getGioMoCua() : "Chưa rõ",
                        s.getGioDongCua() != null ? s.getGioDongCua() : "Chưa rõ",
                        s.getMoTa() != null ? s.getMoTa() : "Không có mô tả",
                        s.getDuongDanGGMap() != null ? s.getDuongDanGGMap() : "Không có link Google Map"
                ));
            }
            sb.append("💬 Bạn có muốn mình giúp đặt sân trong khu này không?");
            return sb.toString().trim();
        }

        // 🕓 3️⃣ Lịch sử đặt sân
        if (m.contains("tôi đã đặt") || m.contains("lịch sử") ||
            m.contains("đơn của tôi") || m.contains("sân tôi đặt")) {
            KhachHang kh = SessionUtil.getKhachHang(session);
            if (kh == null) {
                return "⚠️ Bạn cần đăng nhập để xem lịch sử đặt sân của mình nha!";
            }

            List<ThueSan> dsThueSan = thueSanRepo.findByKhachHang(kh);
            if (dsThueSan == null || dsThueSan.isEmpty()) {
                return "📭 Có vẻ bạn chưa có đơn đặt sân nào trong hệ thống cả. Hãy thử đặt sân ngay hôm nay nhé! ⚽";
            }

            StringBuilder sb = new StringBuilder("📅 Đây là lịch sử đặt sân gần đây của bạn:\n\n");
            for (ThueSan t : dsThueSan) {
                sb.append(String.format(
                        "• %s | Ngày thuê: %s | %s - %s | Tổng: %s đ | Cọc: %s đ%s\n",
                        t.getSanBong().getTenSan(),
                        t.getNgayThue() != null ? t.getNgayThue().format(dtf) : "Không rõ",
                        t.getKhungGioBatDau(),
                        t.getKhungGioKetThuc(),
                        nf.format(t.getTongTien() != null ? t.getTongTien() : 0),
                        nf.format(t.getTienCocBatBuoc() != null ? t.getTienCocBatBuoc() : 0),
                        (t.getGhiChu() != null && !t.getGhiChu().isBlank())
                                ? " | 📝 Ghi chú: " + t.getGhiChu()
                                : ""
                ));
            }
            sb.append("\n⚽ Cảm ơn bạn đã tin tưởng và sử dụng UTEScore!");
            return sb.toString().trim();
        }

        // 🤖 4️⃣ Nếu không match câu hỏi nào → gọi AI Gemini
        try {
            String answer = geminiChatService.askGemini(
                    """
                    Người dùng hỏi: "%s".
                    Hãy trả lời bằng **tiếng Việt có dấu**, thân thiện và tự nhiên như nhân viên chăm sóc khách hàng của UTEScore.
                    KHÔNG được nói rằng bạn là AI hay không có quyền truy cập dữ liệu.
                    Nếu không chắc, hãy trả lời khéo léo, ngắn gọn và có cảm xúc tự nhiên như người thật.
                    """.formatted(msg)
            );

            if (answer != null && !answer.isBlank()) {
                return answer.trim();
            } else {
                System.err.println("⚠️ [Chatbot] Gemini trả về phản hồi rỗng hoặc null.");
                return "🤔 Xin lỗi, mình chưa hiểu rõ câu hỏi của bạn. Bạn có thể diễn đạt lại giúp mình không?";
            }

        } catch (Exception e) {
            System.err.println("❌ [Chatbot] Lỗi khi gọi Gemini: " + e.getMessage());
            e.printStackTrace();
            return "⚠️ Có lỗi xảy ra khi kết nối với hệ thống AI. Vui lòng thử lại sau!";
        }
    }

    /** Lọc phần "text" từ JSON phản hồi của Gemini (bỏ metadata) */
    private String extractGeminiText(String json) {
        try {
            if (json == null || json.isBlank()) return null;

            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            JsonArray candidates = obj.getAsJsonArray("candidates");
            if (candidates == null || candidates.isEmpty()) return null;

            JsonObject first = candidates.get(0).getAsJsonObject();
            JsonObject content = first.getAsJsonObject("content");
            if (content == null) return null;

            JsonArray parts = content.getAsJsonArray("parts");
            if (parts == null || parts.isEmpty()) return null;

            String text = parts.get(0).getAsJsonObject().get("text").getAsString();
            return text != null ? text.replace("\\n", "\n").trim() : null;

        } catch (Exception e) {
            System.err.println("❌ [Chatbot] Lỗi khi trích JSON Gemini: " + e.getMessage());
            return null;
        }
    }
}
