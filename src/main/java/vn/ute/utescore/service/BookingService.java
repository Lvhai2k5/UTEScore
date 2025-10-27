package vn.ute.utescore.service;

import org.springframework.stereotype.Service;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.repository.BookingRepository;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<ThueSan> findAll() {
        return bookingRepository.findAll();
    }
    
    public List<ThueSan> findByLoaiThanhToan(String loaiThanhToan) {
        return bookingRepository.findByLoaiThanhToan(loaiThanhToan);
    }
    
    public List<ThueSan> searchByKeyword(String keyword) {
        return bookingRepository.findByKeyword(keyword);
    }
    
    
    
    
}
