package vn.ute.utescore.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.ute.utescore.model.ThueSan;

public interface ThueSanRepository extends JpaRepository<ThueSan, Integer> {

	    @Query("""
	        SELECT DISTINCT t
	        FROM ThueSan t
	        WHERE t.ngayThue > :from
	          AND NOT EXISTS (
	              SELECT 1 FROM CheckinLog c
	              WHERE c.thueSan.maDonDat = t.maDonDat
	                AND c.ghiChu = '1'   
	          )
	        ORDER BY t.ngayThue ASC
	    """)
	    List<ThueSan> findNotCheckedIn(@Param("from") LocalDateTime from);
	    List<ThueSan> findBySanBong_MaSanAndNgayThueBetween(Integer sanId,
                LocalDateTime start,
                LocalDateTime end);
	    @Query("""
	    	    SELECT DISTINCT t
	    	    FROM ThueSan t
	    	    WHERE t.ngayThue > :from
	    	      AND (
	    	          LOWER(t.khachHang.hoTen) LIKE LOWER(CONCAT('%', :kw, '%'))
	    	          OR STR(t.maDonDat) LIKE CONCAT('%', :kw, '%')
	    	      )
	    	      AND NOT EXISTS (
	    	          SELECT 1 FROM CheckinLog c
	              WHERE c.thueSan.maDonDat = t.maDonDat
	                AND c.ghiChu = '1'
	    	      )
	    	    ORDER BY t.ngayThue ASC
	    	""")
	    	List<ThueSan> findFutureUncheckinBookingsByKeyword(
	    	        @Param("from") LocalDateTime from,
	    	        @Param("kw") String keyword);
	}

