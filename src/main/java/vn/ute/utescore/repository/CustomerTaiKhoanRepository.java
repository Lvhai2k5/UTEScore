package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.ute.utescore.model.TaiKhoan;

import java.util.Optional;

public interface CustomerTaiKhoanRepository extends JpaRepository<TaiKhoan, String> {

    @Query("SELECT t FROM TaiKhoan t WHERE t.email = :email")
    Optional<TaiKhoan> findByEmail(@Param("email") String email);
}
