package vn.ute.utescore.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.LichSuTrangThaiSan;

@Repository
public interface LichSuTrangThaiSanRepository extends JpaRepository<LichSuTrangThaiSan, Integer> {
    
    // Tìm theo sân
    @Query("SELECT l FROM LichSuTrangThaiSan l WHERE l.sanBong.maSan = :maSan")
    List<LichSuTrangThaiSan> findBySanBong_MaSan(@Param("maSan") Integer maSan);
    
    // Tìm theo người thực hiện
    @Query("SELECT l FROM LichSuTrangThaiSan l WHERE l.nguoiThucHien.userID = :nhanVienId")
    List<LichSuTrangThaiSan> findByNguoiThucHien_UserID(@Param("nhanVienId") Integer nhanVienId);
    
    // Tìm theo khoảng thời gian
    @Query("SELECT l FROM LichSuTrangThaiSan l WHERE l.ThoiGianThayDoi BETWEEN :startDate AND :endDate")
    List<LichSuTrangThaiSan> findByThoiGianThayDoiBetween(@Param("startDate") LocalDateTime startDate, 
                                                          @Param("endDate") LocalDateTime endDate);
    
    // Tìm lịch sử gần nhất của sân
    @Query("SELECT l FROM LichSuTrangThaiSan l WHERE l.sanBong.maSan = :maSan ORDER BY l.ThoiGianThayDoi DESC")
    List<LichSuTrangThaiSan> findLatestBySanBong(@Param("maSan") Integer maSan);
}

