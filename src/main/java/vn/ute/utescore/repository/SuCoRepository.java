package vn.ute.utescore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.ute.utescore.model.SuCo;
import vn.ute.utescore.model.ThueSan;

public interface SuCoRepository extends JpaRepository<SuCo, Integer> {
	@Query("""
	        SELECT t FROM ThueSan t
	        WHERE t.khachHang.hoTen LIKE %:kw%
	           OR CAST(t.maDonDat AS string) LIKE %:kw%
	    """)
	    List<ThueSan> findByKeyword(@Param("kw") String keyword);
}
