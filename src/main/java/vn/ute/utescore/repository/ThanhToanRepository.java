package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.ThanhToan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {

    @Query("""
        SELECT t
        FROM ThanhToan t
        JOIN t.thueSan ts
        JOIN CheckinLog c ON c.thueSan = ts
        WHERE t.LoaiThanhToan = :depositType
        AND c.ghiChu = :noteValue
    """)
    List<ThanhToan> findDepositsCheckedIn(
        @Param("depositType") String depositType,
        @Param("noteValue") String noteValue
    );
    
    @Query("""
    	    SELECT t
    	    FROM ThanhToan t
    	    WHERE t.TrangThaiThanhToan <> :status
    	""")
    	List<ThanhToan> findUncompletedPayments1(@Param("status") String status);
  
    @Query(value = """
    	    SELECT tt.* 
    	    FROM ThanhToan tt
    	    LEFT JOIN HoanTien ht ON ht.MaThanhToan = tt.MaThanhToan
    	    WHERE 
    	        (tt.LoaiThanhToan = :status1
    	         OR (tt.LoaiThanhToan = :status2 AND ht.TrangThaiHoan = :status2))
    	      AND tt.NgayThanhToan BETWEEN :startOfDay AND :endOfDay
    	    ORDER BY tt.NgayThanhToan DESC
    	""", nativeQuery = true)
    	List<ThanhToan> findAllByStatusesAndDateRange(
    	        @Param("status1") String status1,
    	        @Param("status2") String status2,
    	        @Param("startOfDay") LocalDateTime startOfDay,
    	        @Param("endOfDay") LocalDateTime endOfDay
    	);
    @Query(value = """
    	    SELECT tt.*
    	    FROM ThanhToan tt
    	    LEFT JOIN HoanTien ht ON tt.MaDonDat = ht.MaThanhToan
    	    WHERE 
    	        (tt.LoaiThanhToan = :status1
    	         OR (tt.LoaiThanhToan = :status2 AND ht.TrangThaiHoan = :status2))
    	      AND CAST(tt.NgayThanhToan AS date) = :selectedDate
    	    ORDER BY tt.NgayThanhToan DESC
    	""", nativeQuery = true)
    	List<ThanhToan> findAllByStatusAndDate(
    	        @Param("status1") String status1,   // 'Đã thanh toán'
    	        @Param("status2") String status2,   // 'Hoàn tiền'
    	        @Param("selectedDate") String selectedDate // '2025-10-26'
    	);
 // Find all payments - simple
    List<ThanhToan> findAll();
    
    // Find all payments with successful status (Thành Công) - using exact match
    @Query(value = "SELECT * FROM ThanhToan WHERE TrangThaiThanhToan = 'Thành Công'", nativeQuery = true)
    List<ThanhToan> findSuccessfulPayments();
    
    // Find all payments with pending status (Đang xử lý)
    @Query("SELECT t FROM ThanhToan t WHERE t.TrangThaiThanhToan = 'Đang xử lý'")
    List<ThanhToan> findPendingPayments();
    
    // Find payments by booking ID
    List<ThanhToan> findByThueSan_MaDonDat(Integer maDonDat);
    
    // Find successful payments by date range
    @Query(value = "SELECT * FROM ThanhToan WHERE TrangThaiThanhToan = 'Thành công' AND NgayThanhToan BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<ThanhToan> findSuccessfulPaymentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Calculate total revenue in a date range
    @Query(value = "SELECT COALESCE(SUM(SoTienNhan), 0) FROM ThanhToan WHERE TrangThaiThanhToan = 'Thành công' AND NgayThanhToan BETWEEN :startDate AND :endDate", nativeQuery = true)
    BigDecimal calculateTotalRevenue(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Count successful payments in a date range
    @Query(value = "SELECT COUNT(*) FROM ThanhToan WHERE TrangThaiThanhToan = 'Thành công' AND NgayThanhToan BETWEEN :startDate AND :endDate", nativeQuery = true)
    Long countSuccessfulPayments(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find uncompleted payments
    @Query("SELECT t FROM ThanhToan t WHERE t.TrangThaiThanhToan <> :status")
    List<ThanhToan> findUncompletedPayments(@Param("status") String status);

        @Query("SELECT tt FROM ThanhToan tt WHERE tt.LoaiThanhToan = :status")
        List<ThanhToan> findRefundPayments(@Param("status") String status);
        default List<ThanhToan> findRefundPaymentsDefault() {
            return findRefundPayments("Hoàn tiền");
        }
   
}
