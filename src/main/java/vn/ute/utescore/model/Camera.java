package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Camera")
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer CameraID;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TenCamera;

    @ManyToOne
    @JoinColumn(name = "SanID")
    private SanBong sanBong;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String IP;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String TrangThai;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String FileMoPhong;

    @ManyToOne
    @JoinColumn(name = "NhanVienPhuTrach")
    private NhanVien nhanVienPhuTrach;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String GhiChu;

    private LocalDateTime NgayTao;
    private LocalDateTime NgayCapNhat;

    @OneToMany(mappedBy = "camera", cascade = CascadeType.ALL)
    private List<LichSuCamera> lichSuCameras;

    // ===== Constructors =====
    public Camera() {
    }

    public Camera(Integer cameraID, String tenCamera, SanBong sanBong, String IP, String trangThai,
                  String fileMoPhong, NhanVien nhanVienPhuTrach, String ghiChu,
                  LocalDateTime ngayTao, LocalDateTime ngayCapNhat, List<LichSuCamera> lichSuCameras) {
        this.CameraID = cameraID;
        this.TenCamera = tenCamera;
        this.sanBong = sanBong;
        this.IP = IP;
        this.TrangThai = trangThai;
        this.FileMoPhong = fileMoPhong;
        this.nhanVienPhuTrach = nhanVienPhuTrach;
        this.GhiChu = ghiChu;
        this.NgayTao = ngayTao;
        this.NgayCapNhat = ngayCapNhat;
        this.lichSuCameras = lichSuCameras;
    }

    // ===== Getters & Setters =====
    public Integer getCameraID() {
        return CameraID;
    }

    public void setCameraID(Integer cameraID) {
        this.CameraID = cameraID;
    }

    public String getTenCamera() {
        return TenCamera;
    }

    public void setTenCamera(String tenCamera) {
        this.TenCamera = tenCamera;
    }

    public SanBong getSanBong() {
        return sanBong;
    }

    public void setSanBong(SanBong sanBong) {
        this.sanBong = sanBong;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        this.TrangThai = trangThai;
    }

    public String getFileMoPhong() {
        return FileMoPhong;
    }

    public void setFileMoPhong(String fileMoPhong) {
        this.FileMoPhong = fileMoPhong;
    }

    public NhanVien getNhanVienPhuTrach() {
        return nhanVienPhuTrach;
    }

    public void setNhanVienPhuTrach(NhanVien nhanVienPhuTrach) {
        this.nhanVienPhuTrach = nhanVienPhuTrach;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.GhiChu = ghiChu;
    }

    public LocalDateTime getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.NgayTao = ngayTao;
    }

    public LocalDateTime getNgayCapNhat() {
        return NgayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.NgayCapNhat = ngayCapNhat;
    }

    public List<LichSuCamera> getLichSuCameras() {
        return lichSuCameras;
    }

    public void setLichSuCameras(List<LichSuCamera> lichSuCameras) {
        this.lichSuCameras = lichSuCameras;
    }

    // ===== equals & hashCode =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Camera)) return false;
        Camera camera = (Camera) o;
        return Objects.equals(CameraID, camera.CameraID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(CameraID);
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "Camera{" +
                "CameraID=" + CameraID +
                ", TenCamera='" + TenCamera + '\'' +
                ", SanBong=" + (sanBong != null ? sanBong.getMaSan() : "null") +
                ", IP='" + IP + '\'' +
                ", TrangThai='" + TrangThai + '\'' +
                ", FileMoPhong='" + FileMoPhong + '\'' +
                ", NhanVienPhuTrach=" + (nhanVienPhuTrach != null ? nhanVienPhuTrach.getUserID() : "null") +
                ", GhiChu='" + GhiChu + '\'' +
                ", NgayTao=" + NgayTao +
                ", NgayCapNhat=" + NgayCapNhat +
                '}';
    }
}
