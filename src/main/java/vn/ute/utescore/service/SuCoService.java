// SuCoService.java
package vn.ute.utescore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.utescore.model.SuCo;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.repository.SuCoRepository;

import java.util.List;

@Service
public class SuCoService {
    @Autowired private SuCoRepository suCoRepo;

    public List<SuCo> getAll() { return suCoRepo.findAll(); }

    public SuCo save(SuCo suCo) { return suCoRepo.save(suCo); }
    
    public List<ThueSan> searchByKeyword(String keyword) {
        
        return suCoRepo.findByKeyword(keyword);
    }
}
