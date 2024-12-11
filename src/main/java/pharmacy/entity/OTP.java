package pharmacy.entity;

import java.time.LocalDateTime;

public class OTP {

    private int otpId;                 
    private String tenDangNhap;         
    private String otpCode;            
    private LocalDateTime createdAt;    
    private LocalDateTime expiresAt;   

    public OTP(int otpId, String tenDangNhap, String otpCode, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.otpId = otpId;
        this.tenDangNhap = tenDangNhap;
        this.otpCode = otpCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public int getOtpId() {
        return otpId;
    }

    public void setOtpId(int otpId) {
        this.otpId = otpId;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    @Override
    public String toString() {
        return "OTP{" +
                "otpId=" + otpId +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", otpCode='" + otpCode + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
