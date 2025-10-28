package vn.ute.utescore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.GiaThue;

@Repository
public interface GiaThueRepository extends JpaRepository<GiaThue, Integer> {
    
    // Tìm theo loại sân
    @Query("SELECT g FROM GiaThue g WHERE g.loaiSan = :loaiSan")
    List<GiaThue> findByLoaiSan(@Param("loaiSan") String loaiSan);
    
    // Tìm theo trạng thái
    @Query("SELECT g FROM GiaThue g WHERE g.trangThai = :trangThai")
    List<GiaThue> findByTrangThai(@Param("trangThai") String trangThai);
    
    // Tìm giá thuê đang áp dụng
    @Query("SELECT g FROM GiaThue g WHERE g.trangThai = 'Active' ORDER BY g.ngayApDung DESC")
    List<GiaThue> findActivePricing();
    
    // Tìm giá thuê theo loại sân và trạng thái
    @Query("SELECT g FROM GiaThue g WHERE g.loaiSan = :loaiSan AND g.trangThai = :trangThai")
    List<GiaThue> findByLoaiSanAndTrangThai(@Param("loaiSan") String loaiSan, @Param("trangThai") String trangThai);
    
    // Tìm giá thuê theo khung giờ
    @Query("SELECT g FROM GiaThue g WHERE g.khungGioBatDau <= :time AND g.khungGioKetThuc >= :time")
    List<GiaThue> findByTimeRange(@Param("time") java.time.LocalTime time);
}

