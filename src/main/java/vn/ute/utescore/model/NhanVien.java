package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "NhanVien")
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer UserID;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String FullName;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String Email;

    @Column(columnDefinition = "NVARCHAR(11)")
    private String Phone;

    @ManyToOne
    @JoinColumn(name = "RoleID")
    private Roles role;

    @Column(columnDefinition = "NVARCHAR(20)")
    private String Status;

    private LocalDateTime CreatedAt;
    private LocalDateTime UpdateAt;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<BaoTri> baoTris;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<SuCo> suCos;

    @OneToMany(mappedBy = "nhanVienPhuTrach", cascade = CascadeType.ALL)
    private List<Camera> cameras;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<LichSuCamera> lichSuCameras;

    @OneToMany(mappedBy = "nguoiThucHien", cascade = CascadeType.ALL)
    private List<LichSuTrangThaiSan> lichSuTrangThais;

    @OneToMany(mappedBy = "nhanVienXuLy", cascade = CascadeType.ALL)
    private List<GopYHeThong> gopYXuLys;

    @OneToMany(mappedBy = "nhanVienPhanHoi", cascade = CascadeType.ALL)
    private List<DanhGiaDonHang> danhGiaPhanHois;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<CaLamViec> caLamViecs;

    // ===== Constructors =====
    public NhanVien() {
    }

    public NhanVien(Integer userID, String fullName, String email, String phone, Roles role,
                    String status, LocalDateTime createdAt, LocalDateTime updateAt,
                    List<BaoTri> baoTris, List<SuCo> suCos, List<Camera> cameras,
                    List<LichSuCamera> lichSuCameras, List<LichSuTrangThaiSan> lichSuTrangThais,
                    List<GopYHeThong> gopYXuLys, List<DanhGiaDonHang> danhGiaPhanHois,
                    List<CaLamViec> caLamViecs) {
        this.UserID = userID;
        this.FullName = fullName;
        this.Email = email;
        this.Phone = phone;
        this.role = role;
        this.Status = status;
        this.CreatedAt = createdAt;
        this.UpdateAt = updateAt;
        this.baoTris = baoTris;
        this.suCos = suCos;
        this.cameras = cameras;
        this.lichSuCameras = lichSuCameras;
        this.lichSuTrangThais = lichSuTrangThais;
        this.gopYXuLys = gopYXuLys;
        this.danhGiaPhanHois = danhGiaPhanHois;
        this.caLamViecs = caLamViecs;
    }

    // ===== Getters & Setters =====
    public Integer getUserID() {
        return UserID;
    }

    public void setUserID(Integer userID) {
        this.UserID = userID;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        this.FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.CreatedAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.UpdateAt = updateAt;
    }

    public List<BaoTri> getBaoTris() {
        return baoTris;
    }

    public void setBaoTris(List<BaoTri> baoTris) {
        this.baoTris = baoTris;
    }

    public List<SuCo> getSuCos() {
        return suCos;
    }

    public void setSuCos(List<SuCo> suCos) {
        this.suCos = suCos;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public List<LichSuCamera> getLichSuCameras() {
        return lichSuCameras;
    }

    public void setLichSuCameras(List<LichSuCamera> lichSuCameras) {
        this.lichSuCameras = lichSuCameras;
    }

    public List<LichSuTrangThaiSan> getLichSuTrangThais() {
        return lichSuTrangThais;
    }

    public void setLichSuTrangThais(List<LichSuTrangThaiSan> lichSuTrangThais) {
        this.lichSuTrangThais = lichSuTrangThais;
    }

    public List<GopYHeThong> getGopYXuLys() {
        return gopYXuLys;
    }

    public void setGopYXuLys(List<GopYHeThong> gopYXuLys) {
        this.gopYXuLys = gopYXuLys;
    }

    public List<DanhGiaDonHang> getDanhGiaPhanHois() {
        return danhGiaPhanHois;
    }

    public void setDanhGiaPhanHois(List<DanhGiaDonHang> danhGiaPhanHois) {
        this.danhGiaPhanHois = danhGiaPhanHois;
    }

    public List<CaLamViec> getCaLamViecs() {
        return caLamViecs;
    }

    public void setCaLamViecs(List<CaLamViec> caLamViecs) {
        this.caLamViecs = caLamViecs;
    }

    // ===== equals & hashCode =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NhanVien)) return false;
        NhanVien nhanVien = (NhanVien) o;
        return Objects.equals(UserID, nhanVien.UserID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UserID);
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "NhanVien{" +
                "UserID=" + UserID +
                ", FullName='" + FullName + '\'' +
                ", Email='" + Email + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Role=" + (role != null ? role.getRoleID() : "null") +
                ", Status='" + Status + '\'' +
                ", CreatedAt=" + CreatedAt +
                ", UpdateAt=" + UpdateAt +
                '}';
    }
}
