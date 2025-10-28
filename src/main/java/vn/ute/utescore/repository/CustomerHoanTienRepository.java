package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.ute.utescore.model.HoanTien;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.model.ThueSan;

import java.util.List;

public interface CustomerHoanTienRepository extends JpaRepository<HoanTien, Integer> {

    // ✅ Kiểm tra có tồn tại bản ghi hoàn tiền cho đơn thuê sân cụ thể hay không
    boolean existsByThanhToan_ThueSanAndTrangThaiHoanIgnoreCase(ThueSan thueSan, String trangThaiHoan);

    // ✅ Lấy danh sách yêu cầu hoàn tiền theo thanh toán
    List<HoanTien> findByThanhToan(ThanhToan thanhToan);

    // ✅ Lấy danh sách hoàn tiền theo trạng thái
    List<HoanTien> findByTrangThaiHoanIgnoreCase(String trangThaiHoan);

    // ✅ Lấy các yêu cầu hoàn tiền đang chờ duyệt, sắp xếp theo ngày yêu cầu
    @Query("""
        SELECT h FROM HoanTien h
        WHERE LOWER(h.trangThaiHoan) = LOWER(:status)
        ORDER BY h.ngayYeuCau ASC
    """)
    List<HoanTien> findByTrangThaiHoanIgnoreCaseOrderByNgayYeuCauAsc(@Param("status") String status);

    // ✅ Lấy toàn bộ hoàn tiền theo khách hàng
    @Query("""
        SELECT h FROM HoanTien h
        JOIN h.thanhToan t
        JOIN t.thueSan ts
        WHERE ts.khachHang.maKhachHang = :maKhachHang
        ORDER BY h.ngayXuLy DESC
    """)
    List<HoanTien> findAllByKhachHangId(@Param("maKhachHang") Integer maKhachHang);
}
