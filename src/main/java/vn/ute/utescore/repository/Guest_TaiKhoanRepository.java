package vn.ute.utescore.repository;

import vn.ute.utescore.model.TaiKhoan;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Guest_TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {

    @Query("""
        SELECT t FROM TaiKhoan t
        WHERE TRIM(LOWER(t.soDienThoai)) = TRIM(LOWER(:soDienThoai))
    """)
    Optional<TaiKhoan> findBySoDienThoai(@Param("soDienThoai") String soDienThoai);
}
