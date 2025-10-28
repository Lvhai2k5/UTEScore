package vn.ute.utescore.service;

import vn.ute.utescore.dto.Guest_ReviewDTO;
import java.util.List;

public interface Guest_ReviewService {
    List<Guest_ReviewDTO> getSampleReviews();  // nếu còn muốn mock tạm
    List<Guest_ReviewDTO> getByPitchId(Long pitchId);
    double averageRating(List<Guest_ReviewDTO> reviews);
}
