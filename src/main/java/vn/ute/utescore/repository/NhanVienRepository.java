// NhanVienRepository.java
package vn.ute.utescore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.ute.utescore.model.NhanVien;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
	// Tìm theo vai trò
    @Query("SELECT n FROM NhanVien n WHERE n.role.roleID = :roleId")
    List<NhanVien> findByRole_RoleID(@Param("roleId") Integer roleId);
    
    // Tìm theo trạng thái
    @Query("SELECT n FROM NhanVien n WHERE n.status = :status")
    List<NhanVien> findByStatus(@Param("status") String status);
    
    // Tìm theo email
    @Query("SELECT n FROM NhanVien n WHERE n.email = :email")
    Optional<NhanVien> findByEmail(@Param("email") String email);
    
    // Tìm theo số điện thoại
    @Query("SELECT n FROM NhanVien n WHERE n.phone = :phone")
    Optional<NhanVien> findByPhone(@Param("phone") String phone);
    
    // Tìm nhân viên đang hoạt động
    @Query("SELECT n FROM NhanVien n WHERE n.status = 'Active'")
    List<NhanVien> findActiveEmployees();
    
    // Tìm nhân viên theo tên
    @Query("SELECT n FROM NhanVien n WHERE LOWER(n.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<NhanVien> findByFullNameContainingIgnoreCase(@Param("fullName") String fullName);
}
