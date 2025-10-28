package vn.ute.utescore.repository;

import vn.ute.utescore.model.GopYHeThong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Guest_ReviewRepository extends JpaRepository<GopYHeThong, Integer> {

    // Lấy góp ý theo mã sân
    List<GopYHeThong> findBySanBong_MaSan(Integer maSan);

    // Nếu bạn có trạng thái duyệt góp ý thì mở thêm:
    // List<GopYHeThong> findBySanBong_MaSanAndTrangThaiXuLyIgnoreCase(Integer maSan, String trangThai);
}
