package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.SanBong;

@Repository
public interface SanBongRepository extends JpaRepository<SanBong, Integer> {
    // Có thể thêm hàm tìm kiếm nâng cao nếu cần, ví dụ:
    // List<SanBong> findByLoaiSan(String loaiSan);
}
