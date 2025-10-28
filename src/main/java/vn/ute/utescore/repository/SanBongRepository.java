package vn.ute.utescore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.SanBong;

@Repository
public interface SanBongRepository extends JpaRepository<SanBong, Integer> {
	// Tìm kiếm theo trạng thái
    @Query("SELECT s FROM SanBong s WHERE s.trangThai = :trangThai")
    List<SanBong> findByTrangThai(@Param("trangThai") String trangThai);
    
    // Đếm theo trạng thái
    @Query("SELECT COUNT(s) FROM SanBong s WHERE s.trangThai = :trangThai")
    long countByTrangThai(@Param("trangThai") String trangThai);
    
    // Tìm kiếm theo loại sân
    @Query("SELECT s FROM SanBong s WHERE s.loaiSan = :loaiSan")
    List<SanBong> findByLoaiSan(@Param("loaiSan") String loaiSan);
    
    // Tìm kiếm theo khu vực
    @Query("SELECT s FROM SanBong s WHERE LOWER(s.khuVuc) LIKE LOWER(CONCAT('%', :khuVuc, '%'))")
    List<SanBong> findByKhuVucContainingIgnoreCase(@Param("khuVuc") String khuVuc);
    
    // Tìm kiếm theo tên sân
    @Query("SELECT s FROM SanBong s WHERE LOWER(s.tenSan) LIKE LOWER(CONCAT('%', :tenSan, '%'))")
    List<SanBong> findByTenSanContainingIgnoreCase(@Param("tenSan") String tenSan);
    
    // Tìm kiếm theo nhân viên phụ trách
    @Query("SELECT s FROM SanBong s WHERE s.cameras IS NOT EMPTY AND EXISTS " +
           "(SELECT c FROM Camera c WHERE c.sanBong = s AND c.nhanVienPhuTrach.userID = :nhanVienId)")
    List<SanBong> findByNhanVienPhuTrach(@Param("nhanVienId") Integer nhanVienId);
}
