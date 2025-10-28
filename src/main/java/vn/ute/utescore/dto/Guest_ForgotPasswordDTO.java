package vn.ute.utescore.dto;

public class Guest_ForgotPasswordDTO {

    private String email;
    private String soDienThoai;

    public Guest_ForgotPasswordDTO() {
    }

    public Guest_ForgotPasswordDTO(String email, String soDienThoai) {
        this.email = email;
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}
