package vn.ute.utescore.service;

import vn.ute.utescore.model.SanBong;
import java.util.List;

public interface CustomerSanBongService {

	List<SanBong> timSanTheoDieuKien(String keyword,
            String loaiSan,
            String trangThai,
            Integer maTinhNang,
            String gioBatDau,
            String gioKetThuc);
}
