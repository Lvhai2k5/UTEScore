package vn.ute.utescore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.ute.utescore.model.GopYHeThong;
import vn.ute.utescore.model.KhachHang;
import java.util.List;

public interface CustomerGopYHeThongRepository extends JpaRepository<GopYHeThong, Integer> {

    @Query("SELECT g FROM GopYHeThong g WHERE g.khachHang.email = :email ORDER BY g.NgayGopY DESC")
    List<GopYHeThong> findByKhachHangEmail(@Param("email") String email);

    @Query("SELECT g FROM GopYHeThong g WHERE g.LoaiGopY = :loai ORDER BY g.NgayGopY DESC")
    List<GopYHeThong> findByLoaiGopY(@Param("loai") String loai);

    @Query("""
        SELECT g FROM GopYHeThong g
        JOIN FETCH g.khachHang kh
        LEFT JOIN FETCH g.sanBong sb
        WHERE kh.email = :email
        ORDER BY g.NgayGopY DESC
    """)
    Page<GopYHeThong> pageByKhachHangEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT g FROM GopYHeThong g WHERE g.khachHang = :kh ORDER BY g.NgayGopY DESC")
    List<GopYHeThong> findByKhachHangOrderByNgayGopYDesc(@Param("kh") KhachHang kh);
}
