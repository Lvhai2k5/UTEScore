// ThueSanService.java
package vn.ute.utescore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.repository.ThueSanRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThueSanService {
    @Autowired private ThueSanRepository repo;

    public List<ThueSan> getAll() { return repo.findAll(); }

    public ThueSan findById(Integer id) {
        return repo.findById(id).orElse(null);
    }
    public List<ThueSan> searchFutureUncheckin(String keyword) {
        LocalDateTime now = LocalDateTime.now(); // mốc thời gian hiện tại
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = ""; // tránh null
        }
        return repo.findFutureUncheckinBookingsByKeyword(now, keyword);
    }
}

