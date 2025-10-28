package vn.ute.utescore.service.impl;

import vn.ute.utescore.dto.Guest_PitchDTO;
import vn.ute.utescore.model.SanBong;
import vn.ute.utescore.repository.Guest_PitchRepository;
import vn.ute.utescore.service.Guest_PitchService;
import vn.ute.utescore.utils.Guest_MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Guest_PitchServiceImpl implements Guest_PitchService {

    @Autowired
    private Guest_PitchRepository repo;

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
}
