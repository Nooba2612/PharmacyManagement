package pharmacy.Interface;

import pharmacy.entity.OTP;
import java.util.List;

public interface OTP_Interface {

    boolean addOTP(OTP otp);

    OTP getOTPByUsername(String tenDangNhap);

    boolean isOTPValid(String tenDangNhap, String otpCode);

    void deleteExpiredOTP();

    List<OTP> getAllOTPs();
}
