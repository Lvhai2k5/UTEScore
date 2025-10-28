package vn.ute.utescore.service.impl;

import vn.ute.utescore.model.TinTuc;
import vn.ute.utescore.model.KhuyenMai;
import vn.ute.utescore.repository.Guest_TinTucRepository;
import vn.ute.utescore.repository.Guest_KhuyenMaiRepository;
import vn.ute.utescore.service.Guest_NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Guest_NewsServiceImpl implements Guest_NewsService {

    @Autowired
    private Guest_TinTucRepository tinTucRepo;

    @Autowired
    private Guest_KhuyenMaiRepository kmRepo;

    @Override
    public List<TinTuc> getTinTucMoi() {
        return tinTucRepo.findByTrangThaiIgnoreCaseOrderByNgayDangDesc("HoatDong");
    }

    @Override
    public List<KhuyenMai> getKhuyenMaiDangDienRa() {
        return kmRepo.findByTrangThaiIgnoreCaseOrderByNgayBatDauDesc("HoatDong");
    }
    @Override
    public KhuyenMai getKhuyenMaiById(Integer id) {
        return kmRepo.findById(id).orElse(null);
    }

    @Override
    public TinTuc getTinTucById(Integer id) {
        return tinTucRepo.findById(id).orElse(null); // ✅ Sửa đúng biến repo
    }
}
