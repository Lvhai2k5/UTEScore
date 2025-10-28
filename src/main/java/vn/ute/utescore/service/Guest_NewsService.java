package vn.ute.utescore.service;

import vn.ute.utescore.model.TinTuc;
import vn.ute.utescore.model.KhuyenMai;
import java.util.List;

public interface Guest_NewsService {
    List<TinTuc> getTinTucMoi();
    List<KhuyenMai> getKhuyenMaiDangDienRa();
    TinTuc getTinTucById(Integer id);
    KhuyenMai getKhuyenMaiById(Integer id);
}
