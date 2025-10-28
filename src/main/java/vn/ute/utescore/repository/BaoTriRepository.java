package vn.ute.utescore.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.BaoTri;

@Repository
public interface BaoTriRepository extends JpaRepository<BaoTri, Integer> {
    
    // Tìm theo sân
    @Query("SELECT b FROM BaoTri b WHERE b.sanBong.maSan = :maSan")
    List<BaoTri> findBySanBong_MaSan(@Param("maSan") Integer maSan);
    
    // Tìm theo trạng thái
    @Query("SELECT b FROM BaoTri b WHERE b.TrangThai = :trangThai")
    List<BaoTri> findByTrangThai(@Param("trangThai") String trangThai);
    
    // Tìm theo sân và trạng thái
    @Query("SELECT b FROM BaoTri b WHERE b.sanBong.maSan = :maSan AND b.TrangThai = :trangThai")
    List<BaoTri> findBySanBong_MaSanAndTrangThai(@Param("maSan") Integer maSan, @Param("trangThai") String trangThai);
    
    // Tìm theo nhân viên phụ trách
    @Query("SELECT b FROM BaoTri b WHERE b.nhanVien.userID = :nhanVienId")
    List<BaoTri> findByNhanVien_UserID(@Param("nhanVienId") Integer nhanVienId);
    
    // Tìm theo ngày bảo trì
    @Query("SELECT b FROM BaoTri b WHERE CAST(b.NgayBaoTri AS DATE) = :ngay")
    List<BaoTri> findByNgayBaoTri(@Param("ngay") LocalDate ngay);
    
    // Tìm theo khoảng thời gian - sử dụng LocalDateTime
    @Query("SELECT b FROM BaoTri b WHERE b.NgayBaoTri >= :startDateTime AND b.NgayBaoTri <= :endDateTime")
    List<BaoTri> findByNgayBaoTriBetween(@Param("startDateTime") java.time.LocalDateTime startDateTime, 
                                        @Param("endDateTime") java.time.LocalDateTime endDateTime);
    
    // Tìm theo khoảng thời gian - chỉ so sánh ngày (không so sánh giờ)
    @Query("SELECT b FROM BaoTri b WHERE CAST(b.NgayBaoTri AS DATE) >= :startDate AND CAST(b.NgayBaoTri AS DATE) <= :endDate")
    List<BaoTri> findMaintenanceByDateRange(@Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    // Kiểm tra xung đột thời gian bảo trì
    @Query("SELECT b FROM BaoTri b WHERE b.sanBong.maSan = :maSan " +
           "AND b.NgayBaoTri = :ngay " +
           "AND ((b.ThoiGianBatDau <= :startTime AND b.ThoiGianKetThuc > :startTime) " +
           "OR (b.ThoiGianBatDau < :endTime AND b.ThoiGianKetThuc >= :endTime) " +
           "OR (b.ThoiGianBatDau >= :startTime AND b.ThoiGianKetThuc <= :endTime))")
    List<BaoTri> findConflictingMaintenance(@Param("maSan") Integer maSan,
                                           @Param("ngay") LocalDate ngay,
                                           @Param("startTime") LocalTime startTime,
                                           @Param("endTime") LocalTime endTime);
}

