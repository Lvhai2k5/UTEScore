package vn.ute.utescore.service;

import vn.ute.utescore.dto.Guest_PitchDTO;
import java.util.List;

public interface Guest_PitchService {
    List<Guest_PitchDTO> getAllActive();
    List<Guest_PitchDTO> getAll();
    List<Guest_PitchDTO> getTop3Featured();

    // Thêm để dùng ở detail (tránh stream.filter ở controller)
    Guest_PitchDTO getById(Integer id);
 // Tìm kiếm / lọc sân bóng theo điều kiện
    List<Guest_PitchDTO> searchPitches(String keyword,
                                       String loaiSan,
                                       String trangThai,
                                       String gioBatDau,
                                       String gioKetThuc);

   

}
