package vn.ute.utescore.utils;

import vn.ute.utescore.dto.Guest_PitchDTO;
import vn.ute.utescore.dto.Guest_ReviewDTO;
import vn.ute.utescore.model.SanBong;
import vn.ute.utescore.model.GopYHeThong;

import java.util.List;
import java.util.stream.Collectors;

public class Guest_MapperUtils {

    // ==================== PITCH ====================
	public static Guest_PitchDTO toPitchDTO(SanBong e) {
	    if (e == null) return null;

	    String kg = (e.getGioMoCua() != null && e.getGioDongCua() != null)
	            ? e.getGioMoCua() + " - " + e.getGioDongCua()
	            : "—";

	    // ✅ Lấy URL ảnh chuẩn
	    String hinhAnh = (e.getHinhAnh() != null && !e.getHinhAnh().isBlank())
	            ? e.getHinhAnh()
	            : "https://res.cloudinary.com/du6x32par/image/upload/v1761452824/san4_jpenlh.jpg";

	    return new Guest_PitchDTO(
	            e.getMaSan() != null ? e.getMaSan().longValue() : null,
	            e.getTenSan(),
	            0, // ✅ SanBong KHÔNG CÓ soSan → để mặc định 0
	            e.getLoaiSan(),
	            0.0, // ✅ Chưa có giá thuê → để mặc định 0.0
	            kg,
	            e.getKhuVuc(),
	            hinhAnh,
	            e.getTrangThai()
	    );
	}


    public static List<Guest_PitchDTO> toPitchDTOList(List<SanBong> entities) {
        return entities == null ? List.of()
                : entities.stream().map(Guest_MapperUtils::toPitchDTO).collect(Collectors.toList());
    }

    // ==================== REVIEW (GopYHeThong) ====================
    public static Guest_ReviewDTO toReviewDTO(GopYHeThong g) {
        if (g == null) return null;

        String userName = (g.getKhachHang() != null) ? g.getKhachHang().getHoTen() : "Ẩn danh";
        int userAge = 0; // nếu có năm sinh trong KhachHang, bạn có thể tính
        double rating = 0.0; // chưa có cột điểm → 0.0 hoặc 4.5 “mặc định”
        String image = "san1.jpg"; // hoặc null/placeholder
        var date = (g.getNgayGopY() != null) ? g.getNgayGopY().toLocalDate() : null;

        Guest_ReviewDTO dto = new Guest_ReviewDTO();
        dto.setId(g.getMaGopY() == null ? null : g.getMaGopY().longValue());
        dto.setUserName(userName);
        dto.setUserAge(userAge);
        dto.setRating(rating);
        dto.setContent(g.getNoiDung());
        dto.setImage(image);
        dto.setDate(date);

        return dto;
    }

    public static List<Guest_ReviewDTO> toReviewDTOList(List<GopYHeThong> entities) {
        return entities == null ? List.of()
                : entities.stream().map(Guest_MapperUtils::toReviewDTO).collect(Collectors.toList());
    }
}
