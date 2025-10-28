package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.TinhNang;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerTinhNangRepository extends JpaRepository<TinhNang, Integer> {

    /** ✅ Tìm chính xác theo tên (fix tên cột viết hoa) */
    @Query("""
        SELECT t FROM TinhNang t
        WHERE t.TenTinhNang = :ten
    """)
    Optional<TinhNang> findByTenTinhNang(@Param("ten") String tenTinhNang);

    /** ✅ Tìm gần đúng theo tên (search có LIKE) */
    @Query("""
        SELECT t FROM TinhNang t
        WHERE LOWER(TRIM(t.TenTinhNang)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))
    """)
    List<TinhNang> searchByName(@Param("keyword") String keyword);
}
