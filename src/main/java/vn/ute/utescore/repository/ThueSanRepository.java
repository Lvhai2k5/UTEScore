package vn.ute.utescore.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.ute.utescore.model.ThueSan;

public interface ThueSanRepository extends JpaRepository<ThueSan, Integer> {

	@Query("""
		    SELECT DISTINCT t
		    FROM ThueSan t
		    WHERE t.ngayThue >= :from
		      AND NOT EXISTS (
		          SELECT 1 FROM CheckinLog c
		          WHERE c.thueSan.maDonDat = t.maDonDat
		            AND c.ghiChu = '1'
		      )
		    ORDER BY t.ngayThue ASC
		""")
		List<ThueSan> findNotCheckedIn(@Param("from") LocalDateTime from);

	@Query("""
		    SELECT DISTINCT t
		    FROM ThueSan t
		    JOIN t.thanhToans a
		    WHERE t.ngayThue >= :from
		      AND a.TrangThaiThanhToan = :status
		      AND NOT EXISTS (
		          SELECT 1
		          FROM CheckinLog c
		          WHERE c.thueSan.maDonDat = t.maDonDat
		            AND c.ghiChu = '1'
		      )
		    ORDER BY t.ngayThue ASC
		""")
		List<ThueSan> findNotCheckedInWithStatus(
		        @Param("from") LocalDateTime from,
		        @Param("status") String status);

	    List<ThueSan> findBySanBong_MaSanAndNgayThueBetween(Integer sanId,
                LocalDateTime start,
                LocalDateTime end);
	    @Query("""
	    	    SELECT DISTINCT t
	    	    FROM ThueSan t
	    	    WHERE t.ngayThue > :from
	    	      AND (
	    	          LOWER(t.khachHang.hoTen) LIKE LOWER(CONCAT('%', :kw, '%'))
	    	          OR STR(t.maDonDat) LIKE CONCAT('%', :kw, '%')
	    	      )
	    	      AND NOT EXISTS (
	    	          SELECT 1 FROM CheckinLog c
	              WHERE c.thueSan.maDonDat = t.maDonDat
	                AND c.ghiChu = '1'
	    	      )
	    	    ORDER BY t.ngayThue ASC
	    	""")
	    	List<ThueSan> findFutureUncheckinBookingsByKeyword(
	    	        @Param("from") LocalDateTime from,
	    	        @Param("kw") String keyword);
	 // Tìm theo khách hàng
	    @Query("SELECT t FROM ThueSan t WHERE t.khachHang.maKhachHang = :maKhachHang")
	    List<ThueSan> findByKhachHang_MaKhachHang(@Param("maKhachHang") Integer maKhachHang);
	    
	    // Tìm theo sân
	    @Query("SELECT t FROM ThueSan t WHERE t.sanBong.maSan = :maSan")
	    List<ThueSan> findBySanBong_MaSan(@Param("maSan") Integer maSan);
	    
	    // Tìm theo ngày thuê
	    @Query("SELECT t FROM ThueSan t WHERE CAST(t.ngayThue AS DATE) = :ngay")
	    List<ThueSan> findByNgayThue(@Param("ngay") LocalDate ngay);
	    
	    // Tìm theo khoảng thời gian
	    @Query("SELECT t FROM ThueSan t WHERE t.ngayThue BETWEEN :startDate AND :endDate")
	    List<ThueSan> findByNgayThueBetween(@Param("startDate") LocalDateTime startDate, 
	                                      @Param("endDate") LocalDateTime endDate);
	    
	    // Kiểm tra xung đột với lịch thuê hiện có (dùng native query để ép kiểu TIME)
	    @Query(value = "SELECT * FROM ThueSan WHERE MaSan = :maSan " +
	           "AND CAST(NgayThue AS DATE) = CAST(:ngay AS DATE) " +
	           "AND ((CAST(CAST(KhungGioBatDau AS TIME) AS TIME) <= CAST(CAST(:startTime AS TIME) AS TIME) AND CAST(CAST(KhungGioKetThuc AS TIME) AS TIME) > CAST(CAST(:startTime AS TIME) AS TIME)) " +
	           "OR (CAST(CAST(KhungGioBatDau AS TIME) AS TIME) < CAST(CAST(:endTime AS TIME) AS TIME) AND CAST(CAST(KhungGioKetThuc AS TIME) AS TIME) >= CAST(CAST(:endTime AS TIME) AS TIME)) " +
	           "OR (CAST(CAST(KhungGioBatDau AS TIME) AS TIME) >= CAST(CAST(:startTime AS TIME) AS TIME) AND CAST(CAST(KhungGioKetThuc AS TIME) AS TIME) <= CAST(CAST(:endTime AS TIME) AS TIME)))", 
	           nativeQuery = true)
	    List<ThueSan> findConflictingBookings(@Param("maSan") Integer maSan,
	                                         @Param("ngay") LocalDate ngay,
	                                         @Param("startTime") String startTime,
	                                         @Param("endTime") String endTime);
	    
	    // Thống kê theo sân
	    @Query("SELECT COUNT(t) FROM ThueSan t WHERE t.sanBong.maSan = :maSan")
	    long countBySanBong(@Param("maSan") Integer maSan);
	    
	    // Thống kê theo ngày
	    @Query("SELECT COUNT(t) FROM ThueSan t WHERE CAST(t.ngayThue AS DATE) = :ngay")
	    long countByNgayThue(@Param("ngay") LocalDate ngay);
	    
	    // Thống kê theo khoảng thời gian tạo
	    @Query("SELECT COUNT(t) FROM ThueSan t WHERE t.ngayTao BETWEEN :startDate AND :endDate")
	    long countByNgayTaoBetween(@Param("startDate") LocalDateTime startDate, 
	                               @Param("endDate") LocalDateTime endDate);
	}

