package vn.ute.utescore.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.utescore.dto.Guest_PitchDTO;
import vn.ute.utescore.model.SanBong;
import vn.ute.utescore.repository.Guest_PitchRepository;
import vn.ute.utescore.service.Guest_PitchService;
import vn.ute.utescore.utils.Guest_MapperUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class Guest_PitchServiceImpl implements Guest_PitchService {

    @Autowired
    private Guest_PitchRepository repo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Guest_PitchDTO> getAllActive() {
        List<SanBong> list = repo.findByTrangThaiIgnoreCase("HoatDong");
        return Guest_MapperUtils.toPitchDTOList(list);
    }

    @Override
    public List<Guest_PitchDTO> getAll() {
        return Guest_MapperUtils.toPitchDTOList(repo.findAll());
    }

    @Override
    public List<Guest_PitchDTO> getTop3Featured() {
        List<SanBong> list = repo.findTop3ByTrangThaiIgnoreCaseOrderByTenSanAsc("HoatDong");
        return Guest_MapperUtils.toPitchDTOList(list);
    }

    @Override
    public Guest_PitchDTO getById(Integer id) {
        return repo.findById(id)
                .map(Guest_MapperUtils::toPitchDTO)
                .orElse(null);
    }

    /**
     * 🔍 Lọc sân theo nhiều điều kiện
     */
    @Override
    public List<Guest_PitchDTO> searchPitches(String keyword,
                                              String loaiSan,
                                              String trangThai,
                                              String gioBatDau,
                                              String gioKetThuc) {

        StringBuilder sql = new StringBuilder("SELECT * FROM SanBong WHERE 1=1 ");

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (LOWER(TenSan) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
               .append(" OR LOWER(KhuVuc) LIKE LOWER(CONCAT('%', :keyword, '%')) )");
        }
        if (loaiSan != null && !loaiSan.isBlank()) {
            sql.append(" AND LOWER(LoaiSan) = LOWER(:loaiSan)");
        }
        if (trangThai != null && !trangThai.isBlank()) {
            sql.append(" AND LOWER(TrangThai) = LOWER(:trangThai)");
        }

        // ⚡ So sánh giờ mở cửa / đóng cửa nếu người dùng chọn
        if (gioBatDau != null && !gioBatDau.isBlank()) {
            sql.append(" AND CAST(GioMoCua AS time) <= CAST(:gioBatDau AS time)");
        }
        if (gioKetThuc != null && !gioKetThuc.isBlank()) {
            sql.append(" AND CAST(GioDongCua AS time) >= CAST(:gioKetThuc AS time)");
        }

        Query query = entityManager.createNativeQuery(sql.toString(), SanBong.class);

        if (keyword != null && !keyword.isBlank()) query.setParameter("keyword", keyword);
        if (loaiSan != null && !loaiSan.isBlank()) query.setParameter("loaiSan", loaiSan);
        if (trangThai != null && !trangThai.isBlank()) query.setParameter("trangThai", trangThai);
        if (gioBatDau != null && !gioBatDau.isBlank()) query.setParameter("gioBatDau", gioBatDau);
        if (gioKetThuc != null && !gioKetThuc.isBlank()) query.setParameter("gioKetThuc", gioKetThuc);

        List<SanBong> list = query.getResultList();
        if (list == null || list.isEmpty()) return new ArrayList<>();

        return Guest_MapperUtils.toPitchDTOList(list);
    }
}
