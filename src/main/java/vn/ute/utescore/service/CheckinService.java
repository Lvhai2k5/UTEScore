package vn.ute.utescore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.utescore.model.CheckinLog;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.repository.CheckinRepository;
import vn.ute.utescore.repository.ThueSanRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckinService {

    @Autowired
    private ThueSanRepository thueSanRepository;

    @Autowired
    private CheckinRepository checkinRepository;

    public List<ThueSan> getAllThueSan() {
        return thueSanRepository.findAll();
    }

    public List<CheckinLog> getAllCheckins() {
        return checkinRepository.findAll();
    }
    
    public List<ThueSan> searchByKeyword(String keyword) {
        return checkinRepository.findByKeyword(keyword);
    }

    public void checkinSan(Integer maDonDat, String ghiChu) {
        ThueSan thueSan = thueSanRepository.findById(maDonDat)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt sân #" + maDonDat));

        CheckinLog log = new CheckinLog();
        log.setThueSan(thueSan);
        log.setNgayCheckin(LocalDateTime.now());
        log.setGhiChu(ghiChu);

        checkinRepository.save(log);
    }
}
