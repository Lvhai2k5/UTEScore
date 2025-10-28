package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ute.utescore.model.KhachHang;

import java.util.Optional;

public interface CustomerKhachHangRepository extends JpaRepository<KhachHang, Integer> {
    Optional<KhachHang> findByEmail(String email); 
}
