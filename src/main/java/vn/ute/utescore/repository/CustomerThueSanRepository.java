package vn.ute.utescore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.model.ThueSan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CustomerThueSanRepository extends JpaRepository<ThueSan, Integer> {

    // ✅ Lấy tất cả đơn theo khách hàng
    List<ThueSan> findByKhachHang(KhachHang khachHang);

    // ✅ Lấy đơn mới nhất của KH
    Optional<ThueSan> findTopByKhachHangOrderByNgayTaoDesc(KhachHang khachHang);

    // ✅ Phân trang toàn bộ đơn của KH
    Page<ThueSan> findByKhachHang(KhachHang kh, Pageable pageable);

    // ✅ Tìm các đơn theo sân + khoảng thời gian
    @Query("""
        SELECT t FROM ThueSan t
        WHERE t.sanBong.maSan = :pitchId
          AND t.ngayThue BETWEEN :start AND :end
    """)
    List<ThueSan> findBySanBong_MaSanAndNgayThueBetween(
            @Param("pitchId") Integer pitchId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // ✅ Lấy đơn KH trong khoảng thời gian (không phân trang)
    List<ThueSan> findByKhachHangAndNgayThueBetween(
            KhachHang khachHang,
            LocalDateTime from,
            LocalDateTime to
    );

    // ✅ Phân trang lịch sử thuê sân theo ngày
    Page<ThueSan> findByKhachHangAndNgayThueBetweenOrderByNgayThueDesc(
            KhachHang khachHang,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );

    // ✅ Truy vấn linh hoạt dùng cho export / thống kê
    @Query("""
        SELECT t FROM ThueSan t
        WHERE t.khachHang = :khach
          AND (:from IS NULL OR t.ngayThue >= :from)
          AND (:to IS NULL OR t.ngayThue <= :to)
        ORDER BY t.ngayThue DESC
    """)
    List<ThueSan> findAllByKhachHangAndDateRange(
            @Param("khach") KhachHang khach,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // ✅ Gửi thông báo trận sắp diễn ra (30 phút tới)
    @Query(value = """
        SELECT DISTINCT ts.*
        FROM ThueSan ts
        INNER JOIN ThanhToan tt ON tt.MaDonDat = ts.MaDonDat
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
            ) BETWEEN 0 AND 30
            AND (ts.GhiChu IS NULL OR LOWER(ts.GhiChu) NOT LIKE N'%hủy%')
            AND tt.TrangThaiThanhToan LIKE N'%Thành công%'
        """, nativeQuery = true)
    List<ThueSan> findUpcomingMatches();

    // ✅ Lấy email khách hàng từ mã đơn
    @Query("""
        SELECT kh.email
        FROM ThueSan ts
        JOIN ts.khachHang kh
        WHERE ts.maDonDat = :maDonDat
    """)
    String findCustomerEmailByMaDonDat(@Param("maDonDat") Integer maDonDat);
    
    @Query("""
    	    SELECT t FROM ThueSan t
    	    WHERE t.khachHang = :khachHang
    	      AND t.ngayThue BETWEEN :from AND :to
    	    ORDER BY t.ngayThue DESC, t.maDonDat DESC
    	""")
    	Page<ThueSan> findRecentBookings(
    	        @Param("khachHang") KhachHang khachHang,
    	        @Param("from") LocalDateTime from,
    	        @Param("to") LocalDateTime to,
    	        Pageable pageable
    	);
    @Query(value = """
    	    SELECT * FROM ThueSan
    	    WHERE MaSan = :pitchId
    	      AND CAST(NgayThue AS date) = CAST(:ngayThue AS date)
    	      AND (
    	            (KhungGioBatDau < :endTime AND KhungGioKetThuc > :startTime)
    	          )
    	      AND (GhiChu IS NULL OR LOWER(GhiChu) NOT LIKE N'%hủy%')
    	    """, nativeQuery = true)
    	List<ThueSan> findOverlappingBookings(
    	        @Param("pitchId") Integer pitchId,
    	        @Param("ngayThue") LocalDate ngayThue,
    	        @Param("startTime") String startTime,
    	        @Param("endTime") String endTime
    	);


}
