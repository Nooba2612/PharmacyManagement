package pharmacy.bus;

import pharmacy.dao.OTP_DAO;
import pharmacy.entity.OTP;
import pharmacy.Interface.OTP_Interface;

import java.time.LocalDateTime;

public class OTP_BUS {

    private OTP_DAO otpDao = new OTP_DAO();

    public OTP_BUS() {
        this.otpDao = otpDao;
    }

    public boolean addOTP(String tenDangNhap, String otpCode, LocalDateTime expiresAt) {
        OTP otp = new OTP(0, otpCode, otpCode, expiresAt, expiresAt);
        otp.setTenDangNhap(tenDangNhap);
        otp.setOtpCode(otpCode);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(expiresAt);
        
        return otpDao.addOTP(otp);
    }

    public boolean isOTPValid(String tenDangNhap, String otpCode) {
        return otpDao.isOTPValid(tenDangNhap, otpCode);
    }

    public void deleteExpiredOTP() {
        otpDao.deleteExpiredOTP();
    }

    public OTP getOTPByUsername(String tenDangNhap) {
        return otpDao.getOTPByUsername(tenDangNhap);
    }
}
