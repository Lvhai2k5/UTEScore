package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.HoanTien;

@Repository
public interface HoanTienRepository extends JpaRepository<HoanTien, Integer> {
}
