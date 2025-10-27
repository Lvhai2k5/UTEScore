package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.ute.utescore.model.CheckinLog;
import vn.ute.utescore.model.ThueSan;
import java.util.List;

public interface CheckinRepository extends JpaRepository<CheckinLog, Integer> {
    List<CheckinLog> findByThueSan(ThueSan thueSan);
    @Query("""
            SELECT t FROM ThueSan t
            WHERE t.khachHang.hoTen LIKE %:kw%
               OR CAST(t.maDonDat AS string) LIKE %:kw%
        """)
        List<ThueSan> findByKeyword(@Param("kw") String keyword);
}
