package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.GiaThue;

import java.util.List;

@Repository
public interface CustomerGiaThueRepository extends JpaRepository<GiaThue, Integer> {

    /**
     * ✅ Lấy danh sách giá thuê đang áp dụng cho loại sân cụ thể
     * (So sánh không phân biệt hoa thường, bỏ khoảng trắng)
     */
    @Query("""
        SELECT g FROM GiaThue g
        WHERE LOWER(TRIM(g.loaiSan)) = LOWER(TRIM(:loaiSan))
          AND LOWER(TRIM(g.trangThai)) = LOWER(TRIM(:status))
        ORDER BY g.khungGioBatDau ASC
    """)
    List<GiaThue> findActiveByLoaiSan(
            @Param("loaiSan") String loaiSan,
            @Param("status") String status
    );

    
    @Query("""
        SELECT g FROM GiaThue g
        WHERE LOWER(TRIM(g.trangThai)) = LOWER(TRIM(:status))
        ORDER BY g.loaiSan ASC, g.khungGioBatDau ASC
    """)
    List<GiaThue> findByTrangThai(@Param("status") String status);
}
