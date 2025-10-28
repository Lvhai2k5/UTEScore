package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.DanhGiaDonHang;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.model.ThueSan;

import java.util.List;

@Repository
public interface CustomerDanhGiaDonHangRepository extends JpaRepository<DanhGiaDonHang, Integer> {

    /**
     * 🔹 Lấy danh sách đánh giá theo sân bóng (join qua ThueSan → SanBong).
     * 🔹 Sắp xếp theo ngày đánh giá mới nhất.
     */
    @Query("""
        SELECT d FROM DanhGiaDonHang d
        JOIN d.thueSan ts
        JOIN ts.sanBong sb
        WHERE sb.maSan = :maSan
        ORDER BY d.NgayDanhGia DESC
    """)
    List<DanhGiaDonHang> findByMaSan(@Param("maSan") Integer maSan);

    /**
     * 🔹 Lấy tất cả đánh giá của 1 khách hàng cụ thể.
     */
    List<DanhGiaDonHang> findByKhachHang(KhachHang khachHang);
    boolean existsByThueSanAndKhachHang(ThueSan thueSan, KhachHang khachHang);

}
