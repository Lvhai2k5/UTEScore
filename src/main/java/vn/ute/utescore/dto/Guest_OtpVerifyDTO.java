package vn.ute.utescore.dto;

public class Guest_OtpVerifyDTO {

    private String email;
    private String otp;

    public Guest_OtpVerifyDTO() {
    }

    public Guest_OtpVerifyDTO(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
