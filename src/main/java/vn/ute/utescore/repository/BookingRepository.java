package vn.ute.utescore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.ThueSan;

@Repository
public interface BookingRepository extends JpaRepository<ThueSan, Integer> {

    @Query("""
        SELECT DISTINCT t
        FROM ThueSan t
        JOIN t.thanhToans tt
        WHERE tt.LoaiThanhToan = :loaiThanhToan
    """)
    List<ThueSan> findByLoaiThanhToan(@Param("loaiThanhToan") String loaiThanhToan);

    @Query("""
        SELECT t FROM ThueSan t
        WHERE t.khachHang.hoTen LIKE %:kw%
           OR CAST(t.maDonDat AS string) LIKE %:kw%
    """)
    List<ThueSan> findByKeyword(@Param("kw") String keyword);
    @Query("""
            SELECT t FROM ThueSan t
            WHERE t.khachHang.hoTen LIKE %:kw%
               OR CAST(t.maDonDat AS string) LIKE %:kw%
        """)
        List<ThueSan> findByKeyword1(@Param("kw") String keyword);
}
