package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.ThanhToan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {

    // ✅ Query chính xác theo mapping: CheckinLog có thuộc tính thueSan
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
    	List<ThanhToan> findUncompletedPayments(@Param("status") String status);
    List<ThanhToan> findByThueSan_MaDonDat(Integer maDonDat);
    
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

}
