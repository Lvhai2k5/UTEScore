package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.model.ThueSan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerThanhToanRepository extends JpaRepository<ThanhToan, Integer> {

    // ✅ Lấy giao dịch mới nhất theo đơn thuê
    @Query("SELECT t FROM ThanhToan t WHERE t.thueSan = :thueSan ORDER BY t.NgayThanhToan DESC")
    List<ThanhToan> findLatestByThueSan(@Param("thueSan") ThueSan thueSan);

    // ✅ Lấy danh sách thanh toán theo đơn thuê
    List<ThanhToan> findByThueSan(ThueSan thueSan);

    // ✅ Lấy danh sách theo mã đơn và trạng thái
    @Query("SELECT t FROM ThanhToan t WHERE t.thueSan.maDonDat IN :ids AND t.TrangThaiThanhToan = :status")
    List<ThanhToan> findByThueSan_MaDonDatInAndTrangThaiThanhToan(
            @Param("ids") List<Integer> ids,
            @Param("status") String status
    );

    // ✅ Lấy thanh toán mới nhất theo mã đơn
    @Query(value = "SELECT TOP 1 * FROM ThanhToan WHERE MaDonDat = :maDonDat ORDER BY NgayThanhToan DESC", nativeQuery = true)
    Optional<ThanhToan> findByMaDonDat(@Param("maDonDat") Integer maDonDat);

    // ✅ Lấy theo khách hàng + trạng thái + thời gian
    @Query("""
        SELECT t FROM ThanhToan t
        WHERE t.thueSan.khachHang = :khachHang
          AND t.TrangThaiThanhToan = :status
          AND t.NgayThanhToan BETWEEN :from AND :to
    """)
    List<ThanhToan> findByThueSan_KhachHangAndTrangThaiThanhToanAndNgayThanhToanBetween(
            @Param("khachHang") KhachHang khachHang,
            @Param("status") String status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // ✅ Kiểm tra tồn tại theo loại thanh toán (Đặt cọc / Còn lại)
    @Query("""
        SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
        FROM ThanhToan t
        WHERE t.thueSan = :thueSan
          AND LOWER(t.LoaiThanhToan) = LOWER(:loaiThanhToan)
    """)
    boolean existsByThueSanAndLoaiThanhToanIgnoreCase(
            @Param("thueSan") ThueSan thueSan,
            @Param("loaiThanhToan") String loaiThanhToan
    );

    // ✅ Kiểm tra tồn tại giao dịch theo mã đơn, loại và trạng thái
    @Query("""
        SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
        FROM ThanhToan t
        WHERE t.thueSan.maDonDat = :maDonDat
          AND LOWER(t.LoaiThanhToan) = LOWER(:loaiThanhToan)
          AND LOWER(t.TrangThaiThanhToan) = LOWER(:trangThaiThanhToan)
    """)
    boolean existsByThueSan_MaDonDatAndLoaiThanhToanAndTrangThaiThanhToan(
            @Param("maDonDat") Integer maDonDat,
            @Param("loaiThanhToan") String loaiThanhToan,
            @Param("trangThaiThanhToan") String trangThaiThanhToan
    );

    // ✅ Tìm các thanh toán sắp tới (trận sắp diễn ra)
    @Query(value = """
        SELECT tt.*
        FROM ThanhToan tt
        INNER JOIN ThueSan ts ON tt.MaDonDat = ts.MaDonDat
        INNER JOIN KhachHang kh ON kh.MaKhachHang = ts.MaKhachHang
        WHERE 
            CAST(ts.NgayThue AS date) = CAST(SYSDATETIME() AS date)
            AND DATEDIFF(
                MINUTE,
                SYSDATETIME(),
                DATEADD(
                    HOUR, DATEPART(HOUR, ts.KhungGioBatDau),
                    DATEADD(
                        MINUTE, DATEPART(MINUTE, ts.KhungGioBatDau),
                        DATEADD(
                            SECOND, DATEPART(SECOND, ts.KhungGioBatDau),
                            CAST(ts.NgayThue AS datetime)
                        )
                    )
                )
            ) = 120
            AND tt.TrangThaiThanhToan LIKE N'%Thành công%'
            AND (ts.GhiChu IS NULL OR LOWER(ts.GhiChu) NOT LIKE N'%hủy%')
        """, nativeQuery = true)
    List<ThanhToan> findUpcomingPayments();
    @Query("""
            SELECT t FROM ThanhToan t
            WHERE t.thueSan = :thueSan
              AND LOWER(t.LoaiThanhToan) = LOWER(:loai)
              AND LOWER(t.TrangThaiThanhToan) = LOWER(:trangThai)
            ORDER BY t.NgayThanhToan DESC
        """)
        List<ThanhToan> findByThueSanAndLoaiThanhToanAndTrangThaiThanhToan(
                @Param("thueSan") ThueSan thueSan,
                @Param("loai") String loai,
                @Param("trangThai") String trangThai
        );
}
