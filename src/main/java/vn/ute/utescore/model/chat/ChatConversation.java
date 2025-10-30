package vn.ute.utescore.model.chat;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.model.NhanVien;

@Entity
@Table(name = "ChatConversation")
public class ChatConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaConversation")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private NhanVien nhanVien;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "LastMessageAt")
    private LocalDateTime lastMessageAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }
    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(LocalDateTime lastMessageAt) { this.lastMessageAt = lastMessageAt; }
}