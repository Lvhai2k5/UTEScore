package vn.ute.utescore.dto;

import vn.ute.utescore.model.ThueSan;

/**
 * DTO dùng để hiển thị lịch sử đặt sân trong trang customer/history.html
 * Gói toàn bộ thông tin cần thiết cho từng đơn thuê sân.
 */
public class CustomerThueSanViewDTO {

    /** 🏟️ Thông tin chi tiết của đơn thuê sân (liên kết entity ThueSan) */
    private ThueSan thueSan;

    /** 
     * 🪪 Trạng thái hiển thị của đơn:
     * - "⏳ Chưa thanh toán"
     * - "💵 Đã đặt cọc (30%)"
     * - "💰 Đã thanh toán (đủ tiền)"
     * - "✅ Hoàn tất (đã check-in)"
     * - "🔁 Hoàn đơn (đã hoàn cọc)"
     * - "❌ Đã hủy"
     */
    private String trangThai;

    /** ⏱️ Thời gian đếm ngược còn lại (tính bằng giây) — dùng cho trạng thái "Chưa thanh toán" */
    private long countdownSeconds;

    /** 💸 Tổng tiền hiển thị (ví dụ: "200,000 VNĐ") */
    private String tongTienHienThi;

    /** 📝 Ghi chú hoặc mô tả trạng thái hiển thị (nếu cần) */
    private String ghiChuTrangThai;

    // ✅ Constructor mặc định
    public CustomerThueSanViewDTO() {
    }

    // ✅ Constructor đầy đủ (tương đương @AllArgsConstructor)
    public CustomerThueSanViewDTO(ThueSan thueSan, String trangThai, long countdownSeconds,
                                  String tongTienHienThi, String ghiChuTrangThai) {
        this.thueSan = thueSan;
        this.trangThai = trangThai;
        this.countdownSeconds = countdownSeconds;
        this.tongTienHienThi = tongTienHienThi;
        this.ghiChuTrangThai = ghiChuTrangThai;
    }

    // ✅ Constructor tuỳ chỉnh (Controller hay dùng)
    public CustomerThueSanViewDTO(ThueSan thueSan, String trangThai, long countdownSeconds) {
        this.thueSan = thueSan;
        this.trangThai = trangThai;
        this.countdownSeconds = countdownSeconds;
        this.tongTienHienThi = getFormattedTongTien();
    }

    // ✅ Getter & Setter
    public ThueSan getThueSan() {
        return thueSan;
    }

    public void setThueSan(ThueSan thueSan) {
        this.thueSan = thueSan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public long getCountdownSeconds() {
        return countdownSeconds;
    }

    public void setCountdownSeconds(long countdownSeconds) {
        this.countdownSeconds = countdownSeconds;
    }

    public String getTongTienHienThi() {
        return tongTienHienThi;
    }

    public void setTongTienHienThi(String tongTienHienThi) {
        this.tongTienHienThi = tongTienHienThi;
    }

    public String getGhiChuTrangThai() {
        return ghiChuTrangThai;
    }

    public void setGhiChuTrangThai(String ghiChuTrangThai) {
        this.ghiChuTrangThai = ghiChuTrangThai;
    }

    // ✅ Helper: định dạng tổng tiền từ entity ThueSan
    public String getFormattedTongTien() {
        if (thueSan != null && thueSan.getTongTien() != null) {
            return String.format("%,.0f VNĐ", thueSan.getTongTien().doubleValue());
        }
        return "0 VNĐ";
    }

    @Override
    public String toString() {
        return "CustomerThueSanViewDTO{" +
                "thueSan=" + (thueSan != null ? thueSan.getMaDonDat() : "null") +
                ", trangThai='" + trangThai + '\'' +
                ", countdownSeconds=" + countdownSeconds +
                ", tongTienHienThi='" + tongTienHienThi + '\'' +
                ", ghiChuTrangThai='" + ghiChuTrangThai + '\'' +
                '}';
    }
}
