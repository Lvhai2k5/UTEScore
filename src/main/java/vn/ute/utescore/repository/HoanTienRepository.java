package vn.ute.utescore.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.HoanTien;

@Repository
public interface HoanTienRepository extends JpaRepository<HoanTien, Integer> {
	// Find all refunds
    List<HoanTien> findAll();
    
    // Find refunds by status
    @Query("SELECT h FROM HoanTien h WHERE h.trangThaiHoan = :status")
    List<HoanTien> findByTrangThaiHoan(@Param("status") String status);
    
    // Find pending refunds (Chờ duyệt)
    @Query("SELECT h FROM HoanTien h WHERE h.trangThaiHoan = 'Chờ duyệt'")
    List<HoanTien> findPendingRefunds();
    
    // Find completed refunds (Đã hoàn) - using JPQL with correct field name
    @Query("SELECT h FROM HoanTien h WHERE h.trangThaiHoan = 'Đã hoàn'")
    List<HoanTien> findCompletedRefunds();
    
    // Find rejected refunds (Từ chối)
    @Query("SELECT h FROM HoanTien h WHERE h.trangThaiHoan = 'Từ chối'")
    List<HoanTien> findRejectedRefunds();
    
    // Find refunds by date range
    @Query("SELECT h FROM HoanTien h WHERE h.ngayYeuCau BETWEEN :startDate AND :endDate")
    List<HoanTien> findByNgayYeuCauBetween(@Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
    
    // Find refunds by payment ID
    @Query("SELECT h FROM HoanTien h WHERE h.thanhToan.MaThanhToan = :maThanhToan")
    List<HoanTien> findByThanhToan_MaThanhToan(@Param("maThanhToan") Integer maThanhToan);
}
