package vn.ute.utescore.repository;

import vn.ute.utescore.model.TaiKhoan;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface Guest_TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    Optional<TaiKhoan> findBySoDienThoai(String soDienThoai);
}
