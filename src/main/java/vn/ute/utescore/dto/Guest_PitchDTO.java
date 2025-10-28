package vn.ute.utescore.dto;

public class Guest_PitchDTO {
	 private Long sanId;
	    private String tenSan;
	    private int soSan;
	    private String loaiSan;
	    private double giaThue;
	    private String khungGioConTrong;
	    private String diaChi;
	    private String hinhAnh;
	    private String trangThai;

	    public Guest_PitchDTO() {}

	    public Guest_PitchDTO(Long sanId, String tenSan, int soSan, String loaiSan,
	                    double giaThue, String khungGioConTrong, String diaChi,
	                    String hinhAnh, String trangThai) {
	        this.sanId = sanId;
	        this.tenSan = tenSan;
	        this.soSan = soSan;
	        this.loaiSan = loaiSan;
	        this.giaThue = giaThue;
	        this.khungGioConTrong = khungGioConTrong;
	        this.diaChi = diaChi;
	        this.hinhAnh = hinhAnh;
	        this.trangThai = trangThai;
	    }

	    // Getters và Setters
	    public Long getSanId() { return sanId; }
	    public void setSanId(Long sanId) { this.sanId = sanId; }
	    public String getTenSan() { return tenSan; }
	    public void setTenSan(String tenSan) { this.tenSan = tenSan; }
	    public int getSoSan() { return soSan; }
	    public void setSoSan(int soSan) { this.soSan = soSan; }
	    public String getLoaiSan() { return loaiSan; }
	    public void setLoaiSan(String loaiSan) { this.loaiSan = loaiSan; }
	    public double getGiaThue() { return giaThue; }
	    public void setGiaThue(double giaThue) { this.giaThue = giaThue; }
	    public String getKhungGioConTrong() { return khungGioConTrong; }
	    public void setKhungGioConTrong(String khungGioConTrong) { this.khungGioConTrong = khungGioConTrong; }
	    public String getDiaChi() { return diaChi; }
	    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
	    public String getHinhAnh() { return hinhAnh; }
	    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
	    public String getTrangThai() { return trangThai; }
	    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

}
