// src/main/java/vn/ute/utescore/service/impl/Guest_PriceServiceImpl.java
package vn.ute.utescore.service.impl;

import vn.ute.utescore.model.GiaThue;
import vn.ute.utescore.repository.Guest_GiaThueRepository;
import vn.ute.utescore.service.Guest_PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Guest_PriceServiceImpl implements Guest_PriceService {

    @Autowired
    private Guest_GiaThueRepository repo;

    @Override
    public List<GiaThue> getActivePricesByLoaiSan(String loaiSan) {
        if (loaiSan == null || loaiSan.isBlank()) return List.of();
        return repo.findByLoaiSanAndTrangThaiOrderByKhungGioBatDauAsc(loaiSan, "HoatDong");
    }
}
