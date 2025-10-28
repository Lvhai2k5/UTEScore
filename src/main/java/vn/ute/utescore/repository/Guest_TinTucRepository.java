package vn.ute.utescore.repository;

import vn.ute.utescore.model.TinTuc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface Guest_TinTucRepository extends JpaRepository<TinTuc, Integer> {
	List<TinTuc> findByTrangThaiIgnoreCaseOrderByNgayDangDesc(String trangThai);

}
