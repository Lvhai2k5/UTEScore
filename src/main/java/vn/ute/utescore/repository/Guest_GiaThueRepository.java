package vn.ute.utescore.repository;

import vn.ute.utescore.model.GiaThue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Guest_GiaThueRepository extends JpaRepository<GiaThue, Integer> {
    // Lấy bảng giá đang hiệu lực theo loại sân
    List<GiaThue> findByLoaiSanAndTrangThaiOrderByKhungGioBatDauAsc(String loaiSan, String trangThai);
}