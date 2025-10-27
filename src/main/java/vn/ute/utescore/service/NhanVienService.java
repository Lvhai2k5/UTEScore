// NhanVienService.java
package vn.ute.utescore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.repository.NhanVienRepository;

@Service
public class NhanVienService {
    @Autowired private NhanVienRepository repo;

    public NhanVien findById(Integer id) { return repo.findById(id).orElse(null); }
}
