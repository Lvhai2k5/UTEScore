package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.SanBong;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface CustomerSanBongRepository extends JpaRepository<SanBong, Integer> {

	@Query("SELECT s FROM SanBong s " +
	           "WHERE (:loaiSan IS NULL OR s.loaiSan LIKE %:loaiSan%) " +
	           "AND (:khuVuc IS NULL OR s.khuVuc LIKE %:khuVuc%) " +
	           "AND (s.trangThai IS NULL OR LOWER(s.trangThai) LIKE '%hoạt động%')")
	    List<SanBong> timSanTheoDieuKien(String loaiSan, String khuVuc);
    /** 🏟️ Lọc sân theo loại, trạng thái và khung giờ hoạt động */
    @Query("""
        SELECT s FROM SanBong s
        WHERE (:loaiSan IS NULL OR LOWER(TRIM(s.loaiSan)) = LOWER(TRIM(:loaiSan)))
          AND (:trangThai IS NULL OR LOWER(TRIM(s.trangThai)) = LOWER(TRIM(:trangThai)))
          AND (:gioBatDau IS NULL OR s.gioMoCua <= :gioBatDau)
          AND (:gioKetThuc IS NULL OR s.gioDongCua >= :gioKetThuc)
    """)
    List<SanBong> filterByBasicInfo(
            @Param("loaiSan") String loaiSan,
            @Param("trangThai") String trangThai,
            @Param("gioBatDau") LocalTime gioBatDau,
            @Param("gioKetThuc") LocalTime gioKetThuc
    );

    /** ⚙️ Lọc sân theo đầy đủ điều kiện + tên tính năng */
    @Query("""
        SELECT DISTINCT s FROM SanBong s
        JOIN s.tinhNangSans ts
        JOIN ts.tinhNang t
        WHERE (:loaiSan IS NULL OR LOWER(TRIM(s.loaiSan)) = LOWER(TRIM(:loaiSan)))
          AND (:trangThai IS NULL OR LOWER(TRIM(s.trangThai)) = LOWER(TRIM(:trangThai)))
          AND (:gioBatDau IS NULL OR s.gioMoCua <= :gioBatDau)
          AND (:gioKetThuc IS NULL OR s.gioDongCua >= :gioKetThuc)
          AND (:tenTinhNang IS NULL OR LOWER(TRIM(t.TenTinhNang)) LIKE LOWER(CONCAT('%', TRIM(:tenTinhNang), '%')))
    """)
    List<SanBong> filterByFullCondition(
            @Param("loaiSan") String loaiSan,
            @Param("trangThai") String trangThai,
            @Param("gioBatDau") LocalTime gioBatDau,
            @Param("gioKetThuc") LocalTime gioKetThuc,
            @Param("tenTinhNang") String tenTinhNang
    );
    @Query("SELECT s FROM SanBong s ORDER BY s.tenSan ASC")
    List<SanBong> findAllForFeedback();
}
