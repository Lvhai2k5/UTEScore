package vn.ute.utescore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.TinhNang;

@Repository
public interface TinhNangRepository extends JpaRepository<TinhNang, Integer> {
    
    // Tìm theo tên tính năng
    @Query("SELECT t FROM TinhNang t WHERE LOWER(t.TenTinhNang) LIKE LOWER(CONCAT('%', :tenTinhNang, '%'))")
    List<TinhNang> findByTenTinhNangContainingIgnoreCase(@Param("tenTinhNang") String tenTinhNang);
}

