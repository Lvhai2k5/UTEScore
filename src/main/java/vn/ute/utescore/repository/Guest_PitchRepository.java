package vn.ute.utescore.repository;

import vn.ute.utescore.model.SanBong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Guest_PitchRepository extends JpaRepository<SanBong, Integer> {

    // TrangThai là field PascalCase trong entity → dùng derived query IgnoreCase
    List<SanBong> findByTrangThaiIgnoreCase(String trangThai);

    // Tạm thời không có GiaThue → order by TenSan
    List<SanBong> findTop3ByTrangThaiIgnoreCaseOrderByTenSanAsc(String trangThai);
}
