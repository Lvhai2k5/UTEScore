package vn.ute.utescore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.TaiKhoan;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    
    // Tìm theo email (primary key)
    Optional<TaiKhoan> findById(String email);
    
    // Tìm theo số điện thoại
    @Query("SELECT t FROM TaiKhoan t WHERE t.soDienThoai = :soDienThoai")
    Optional<TaiKhoan> findBySoDienThoai(@Param("soDienThoai") String soDienThoai);
    
    // Tìm theo trạng thái
    @Query("SELECT t FROM TaiKhoan t WHERE t.trangThai = :trangThai")
    List<TaiKhoan> findByTrangThai(@Param("trangThai") String trangThai);
    
    // Tìm theo vai trò
    @Query("SELECT t FROM TaiKhoan t WHERE t.role.roleID = :roleId")
    List<TaiKhoan> findByRoleID(@Param("roleId") Integer roleId);
    
    // Kiểm tra email tồn tại
    boolean existsById(String email);
    
    // Kiểm tra số điện thoại đã được sử dụng
    @Query("SELECT COUNT(t) > 0 FROM TaiKhoan t WHERE t.soDienThoai = :soDienThoai AND t.email != :email")
    boolean existsBySoDienThoaiAndEmailNot(@Param("soDienThoai") String soDienThoai, @Param("email") String email);
}
