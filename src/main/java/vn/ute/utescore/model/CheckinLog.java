package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CheckinLog")
public class CheckinLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCheckin")
    private Integer maCheckin;

    @ManyToOne
    @JoinColumn(name = "MaDonDat", nullable = false)
    private ThueSan thueSan;

    @Column(name = "NgayCheckin")
    private LocalDateTime ngayCheckin;

    @Column(name = "GhiChu", columnDefinition = "NVARCHAR(255)")
    private String ghiChu;

    public CheckinLog() {}

    public CheckinLog(ThueSan thueSan, LocalDateTime ngayCheckin, String ghiChu) {
        this.thueSan = thueSan;
        this.ngayCheckin = ngayCheckin;
        this.ghiChu = ghiChu;
    }

    public Integer getMaCheckin() {
        return maCheckin;
    }

    public ThueSan getThueSan() {
        return thueSan;
    }

    public void setThueSan(ThueSan thueSan) {
        this.thueSan = thueSan;
    }

    public LocalDateTime getNgayCheckin() {
        return ngayCheckin;
    }

    public void setNgayCheckin(LocalDateTime ngayCheckin) {
        this.ngayCheckin = ngayCheckin;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
