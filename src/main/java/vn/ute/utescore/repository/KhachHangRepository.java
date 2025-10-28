package vn.ute.utescore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.KhachHang;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    
    // Tìm theo email
    @Query("SELECT k FROM KhachHang k WHERE k.email = :email")
    Optional<KhachHang> findByEmail(@Param("email") String email);
    
    // Tìm theo số điện thoại
    @Query("SELECT k FROM KhachHang k WHERE k.soDienThoai = :soDienThoai")
    Optional<KhachHang> findBySoDienThoai(@Param("soDienThoai") String soDienThoai);
    
    // Tìm theo trạng thái
    @Query("SELECT k FROM KhachHang k WHERE k.trangThai = :trangThai")
    List<KhachHang> findByTrangThai(@Param("trangThai") String trangThai);
    
    // Tìm khách hàng đang hoạt động
    @Query("SELECT k FROM KhachHang k WHERE k.trangThai = 'Active' AND (k.isDeleted = false OR k.isDeleted IS NULL)")
    List<KhachHang> findActiveCustomers();
    
    // Tìm theo tên (không phân biệt hoa thường)
    @Query("SELECT k FROM KhachHang k WHERE LOWER(k.hoTen) LIKE LOWER(CONCAT('%', :hoTen, '%'))")
    List<KhachHang> findByHoTenContainingIgnoreCase(@Param("hoTen") String hoTen);
    
    // Tìm kiếm tổng hợp (tên, email, SĐT)
    @Query("SELECT k FROM KhachHang k WHERE " +
           "LOWER(k.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(k.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "k.soDienThoai LIKE CONCAT('%', :keyword, '%')")
    List<KhachHang> searchCustomers(@Param("keyword") String keyword);
    
    // Tìm khách hàng không bị xóa mềm
    @Query("SELECT k FROM KhachHang k WHERE k.isDeleted = false OR k.isDeleted IS NULL")
    List<KhachHang> findNotDeleted();
    
    // Tìm khách hàng đã bị xóa mềm
    @Query("SELECT k FROM KhachHang k WHERE k.isDeleted = true")
    List<KhachHang> findDeleted();
}
