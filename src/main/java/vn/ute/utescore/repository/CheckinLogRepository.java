package vn.ute.utescore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ute.utescore.model.CheckinLog;

public interface CheckinLogRepository extends JpaRepository<CheckinLog, Integer> {
	Optional<CheckinLog> findFirstByThueSan_MaDonDat(Integer maDonDat);
	Optional<CheckinLog> findByThueSan_MaDonDat(Integer maDonDat);
	Optional<CheckinLog> findTopByThueSan_MaDonDatOrderByMaCheckinDesc(Integer maDonDat);
	}
