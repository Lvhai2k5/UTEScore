package vn.ute.utescore.service.impl;

import vn.ute.utescore.dto.Guest_ReviewDTO;
import vn.ute.utescore.model.GopYHeThong;
import vn.ute.utescore.repository.Guest_ReviewRepository;
import vn.ute.utescore.service.Guest_ReviewService;
import vn.ute.utescore.utils.Guest_MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class Guest_ReviewServiceImpl implements Guest_ReviewService {

    @Autowired
    private Guest_ReviewRepository reviewRepo;

    @Override
    public List<Guest_ReviewDTO> getSampleReviews() {
        // Có thể trả Collections.emptyList() cho gọn
        return Collections.emptyList();
    }

    @Override
    public List<Guest_ReviewDTO> getByPitchId(Long pitchId) {
        if (pitchId == null) return Collections.emptyList();
        List<GopYHeThong> list = reviewRepo.findBySanBong_MaSan(pitchId.intValue());
        return Guest_MapperUtils.toReviewDTOList(list);
    }

    @Override
    public double averageRating(List<Guest_ReviewDTO> reviews) {
        if (reviews == null || reviews.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Guest_ReviewDTO r : reviews) sum += (r.getRating() == 0 ? 4.5 : r.getRating()); // fallback nhẹ
        return Math.round((sum / reviews.size()) * 10.0) / 10.0;
    }
}
