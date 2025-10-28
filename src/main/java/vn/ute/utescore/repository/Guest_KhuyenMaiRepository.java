package vn.ute.utescore.repository;

import vn.ute.utescore.model.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface Guest_KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {
    List<KhuyenMai> findByTrangThaiIgnoreCaseOrderByNgayBatDauDesc(String trangThai);
}
