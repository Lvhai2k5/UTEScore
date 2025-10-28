package vn.ute.utescore.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.ThongBao;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {
    
    @Query("SELECT t FROM ThongBao t WHERE t.khachHang.maKhachHang = :maKhachHang ORDER BY t.NgayGui DESC")
    List<ThongBao> findByKhachHang(@Param("maKhachHang") Integer maKhachHang);
    
    @Query("SELECT t FROM ThongBao t WHERE t.TrangThai = :trangThai")
    List<ThongBao> findByTrangThai(@Param("trangThai") String trangThai);
    
    @Query("SELECT t FROM ThongBao t WHERE t.LoaiThongBao = :loaiThongBao")
    List<ThongBao> findByLoaiThongBao(@Param("loaiThongBao") String loaiThongBao);
    
    @Query("SELECT t FROM ThongBao t WHERE t.NgayGui >= :fromDate AND t.NgayGui <= :toDate")
    List<ThongBao> findByNgayGuiBetween(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
}

