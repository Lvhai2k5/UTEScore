package vn.ute.utescore.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.KhachHang;

@Repository
public interface Guest_KhachHangRepository extends JpaRepository<KhachHang, Integer> { }
