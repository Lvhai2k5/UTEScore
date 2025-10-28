package vn.ute.utescore.service;

import vn.ute.utescore.model.TinhNang;

import java.util.List;
import java.util.Optional;

public interface CustomerTinhNangService {

    /** 🔹 Lấy tất cả tính năng */
    List<TinhNang> findAll();

    /** 🔹 Tìm kiếm tính năng theo tên (keyword) */
    List<TinhNang> searchByName(String keyword);

    /** 🔹 Lấy danh sách tính năng của một sân */
    List<TinhNang> findByMaSan(Integer maSan);

    /** 🔹 Kiểm tra tồn tại theo tên */
    Optional<TinhNang> findByTenTinhNang(String tenTinhNang);

    /** 🔹 Thêm mới hoặc cập nhật tính năng */
    TinhNang save(TinhNang tinhNang);

    /** 🔹 Xóa tính năng theo ID */
    void deleteById(Integer id);
}
