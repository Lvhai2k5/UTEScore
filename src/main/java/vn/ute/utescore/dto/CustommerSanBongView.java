package vn.ute.utescore.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO hiển thị thông tin sân bóng cho khách hàng
 * Hỗ trợ builder pattern thủ công (không dùng Lombok)
 */
public class CustommerSanBongView {

    private Integer id;              // Mã sân
    private String name;             // Tên sân
    private String type;             // Loại sân (5 người, 7 người,...)
    private String area;             // Khu vực
    private String imageUrl;         // Ảnh sân
    private BigDecimal price;        // Giá
    private String priceFormatted;   // Giá hiển thị (VD: "150.000đ/giờ")

    private boolean active;          // true nếu sân đang hoạt động
    private String trangThai;        // Văn bản mô tả trạng thái

    private List<String> features;   // Danh sách tính năng của sân
    private double rating;           // Điểm trung bình đánh giá
    private int totalReviews;        // Tổng lượt đánh giá

    // ✅ Constructor mặc định
    public CustommerSanBongView() {
    }

    // ✅ Constructor đầy đủ
    public CustommerSanBongView(Integer id, String name, String type, String area, String imageUrl,
                                BigDecimal price, String priceFormatted, boolean active, String trangThai,
                                List<String> features, double rating, int totalReviews) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.area = area;
        this.imageUrl = imageUrl;
        this.price = price;
        this.priceFormatted = priceFormatted;
        this.active = active;
        this.trangThai = trangThai;
        this.features = features;
        this.rating = rating;
        this.totalReviews = totalReviews;
    }

    // ✅ Getter & Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPriceFormatted() {
        return priceFormatted;
    }

    public void setPriceFormatted(String priceFormatted) {
        this.priceFormatted = priceFormatted;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    // ✅ Hàm hiển thị trạng thái (dùng cho Thymeleaf)
    public String getStatus() {
        return active ? "Hoạt động" : "Ngừng hoạt động";
    }

    @Override
    public String toString() {
        return "CustommerSanBongView{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", area='" + area + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price=" + price +
                ", priceFormatted='" + priceFormatted + '\'' +
                ", active=" + active +
                ", trangThai='" + trangThai + '\'' +
                ", features=" + features +
                ", rating=" + rating +
                ", totalReviews=" + totalReviews +
                '}';
    }

    // ✅ Builder thủ công (thay thế cho @Builder của Lombok)
    public static class Builder {
        private Integer id;
        private String name;
        private String type;
        private String area;
        private String imageUrl;
        private BigDecimal price;
        private String priceFormatted;
        private boolean active;
        private String trangThai;
        private List<String> features;
        private double rating;
        private int totalReviews;

        public Builder id(Integer id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder area(String area) { this.area = area; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }
        public Builder priceFormatted(String priceFormatted) { this.priceFormatted = priceFormatted; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder trangThai(String trangThai) { this.trangThai = trangThai; return this; }
        public Builder features(List<String> features) { this.features = features; return this; }
        public Builder rating(double rating) { this.rating = rating; return this; }
        public Builder totalReviews(int totalReviews) { this.totalReviews = totalReviews; return this; }

        public CustommerSanBongView build() {
            return new CustommerSanBongView(id, name, type, area, imageUrl, price, priceFormatted, active,
                    trangThai, features, rating, totalReviews);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
