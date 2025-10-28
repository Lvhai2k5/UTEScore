package vn.ute.utescore.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import vn.ute.utescore.model.SanBong;
import vn.ute.utescore.service.CustomerSanBongService;
import java.util.List;

@Service
public class CustomerCustomerSanBongServiceImpl implements CustomerSanBongService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SanBong> timSanTheoDieuKien(String keyword,
                                            String loaiSan,
                                            String trangThai,
                                            Integer maTinhNang,
                                            String gioBatDau,
                                            String gioKetThuc) {

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT sb.* FROM SanBong sb " +
                "LEFT JOIN TinhNangSan tns ON sb.MaSan = tns.MaSan " +
                "LEFT JOIN TinhNang tn ON tn.MaTinhNang = tns.MaTinhNang " +
                "WHERE 1=1"
        );

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (LOWER(sb.TenSan) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
               .append("OR LOWER(sb.KhuVuc) LIKE LOWER(CONCAT('%', :keyword, '%')))");
        }
        if (loaiSan != null && !loaiSan.isBlank()) {
            sql.append(" AND LOWER(sb.LoaiSan) = LOWER(:loaiSan)");
        }
        if (trangThai != null && !trangThai.isBlank()) {
            sql.append(" AND LOWER(sb.TrangThai) = LOWER(:trangThai)");
        }
        if (maTinhNang != null) {
            sql.append(" AND tn.MaTinhNang = :maTinhNang");
        }

        // ⚡ Ép cả 2 vế về kiểu time trong SQL Server để tránh lỗi
        if (gioBatDau != null && !gioBatDau.isBlank()) {
            sql.append(" AND CAST(sb.GioMoCua AS time) <= CAST(:gioBatDau AS time)");
        }
        if (gioKetThuc != null && !gioKetThuc.isBlank()) {
            sql.append(" AND CAST(sb.GioDongCua AS time) >= CAST(:gioKetThuc AS time)");
        }

        Query query = entityManager.createNativeQuery(sql.toString(), SanBong.class);

        if (keyword != null && !keyword.isBlank())
            query.setParameter("keyword", keyword);
        if (loaiSan != null && !loaiSan.isBlank())
            query.setParameter("loaiSan", loaiSan);
        if (trangThai != null && !trangThai.isBlank())
            query.setParameter("trangThai", trangThai);
        if (maTinhNang != null)
            query.setParameter("maTinhNang", maTinhNang);

        if (gioBatDau != null && !gioBatDau.isBlank())
            query.setParameter("gioBatDau", gioBatDau); // giữ string "18:00"
        if (gioKetThuc != null && !gioKetThuc.isBlank())
            query.setParameter("gioKetThuc", gioKetThuc);

        return query.getResultList();
    }
}
