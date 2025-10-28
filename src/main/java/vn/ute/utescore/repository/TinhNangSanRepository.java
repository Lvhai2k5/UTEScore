package vn.ute.utescore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.TinhNangSan;
import vn.ute.utescore.model.TinhNangSanId;

@Repository
public interface TinhNangSanRepository extends JpaRepository<TinhNangSan, TinhNangSanId> {
    
    // Tìm theo sân
    @Query("SELECT t FROM TinhNangSan t WHERE t.sanBong.maSan = :maSan")
    List<TinhNangSan> findBySanBong_MaSan(@Param("maSan") Integer maSan);
    
    // Tìm theo tính năng
    @Query("SELECT t FROM TinhNangSan t WHERE t.tinhNang.MaTinhNang = :maTinhNang")
    List<TinhNangSan> findByTinhNang_MaTinhNang(@Param("maTinhNang") Integer maTinhNang);
    
    // Xóa theo sân
    @Modifying
    @Query("DELETE FROM TinhNangSan t WHERE t.sanBong.maSan = :maSan")
    void deleteBySanBong_MaSan(@Param("maSan") Integer maSan);
    
    // Kiểm tra tính năng có tồn tại không
    @Query("SELECT COUNT(t) > 0 FROM TinhNangSan t WHERE t.sanBong.maSan = :maSan AND t.tinhNang.MaTinhNang = :maTinhNang")
    boolean existsBySanBongAndTinhNang(@Param("maSan") Integer maSan, @Param("maTinhNang") Integer maTinhNang);
}

