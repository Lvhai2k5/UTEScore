package vn.ute.utescore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.ute.utescore.model.TinhNang;
import vn.ute.utescore.repository.CustomerTinhNangRepository;
import vn.ute.utescore.repository.CustomerTinhNangSanRepository;
import vn.ute.utescore.service.CustomerTinhNangService;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerTinhNangServiceImpl implements CustomerTinhNangService {

    private final CustomerTinhNangRepository tinhNangRepository;
    private final CustomerTinhNangSanRepository tinhNangSanRepository;
  public CustomerTinhNangServiceImpl(CustomerTinhNangRepository tinhNangRepository,
                                        CustomerTinhNangSanRepository tinhNangSanRepository) {
         this.tinhNangRepository = tinhNangRepository;
         this.tinhNangSanRepository = tinhNangSanRepository;
     }


    /** 🔹 Lấy tất cả tính năng */
    @Override
    public List<TinhNang> findAll() {
        return tinhNangRepository.findAll();
    }

    /** 🔹 Tìm kiếm theo từ khóa */
    @Override
    public List<TinhNang> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return tinhNangRepository.findAll();
        }
        return tinhNangRepository.searchByName(keyword);
    }

    /** 🔹 Lấy danh sách tính năng theo mã sân */
    @Override
    public List<TinhNang> findByMaSan(Integer maSan) {
        return tinhNangSanRepository.findTinhNangByMaSan(maSan);
    }

    /** 🔹 Kiểm tra tồn tại theo tên */
    @Override
    public Optional<TinhNang> findByTenTinhNang(String tenTinhNang) {
        return tinhNangRepository.findByTenTinhNang(tenTinhNang);
    }

    /** 🔹 Thêm mới hoặc cập nhật tính năng */
    @Override
    public TinhNang save(TinhNang tinhNang) {
        return tinhNangRepository.save(tinhNang);
    }

    /** 🔹 Xóa tính năng theo ID */
    @Override
    public void deleteById(Integer id) {
        tinhNangRepository.deleteById(id);
    }
}
