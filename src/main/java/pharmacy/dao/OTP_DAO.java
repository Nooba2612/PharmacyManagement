package pharmacy.dao;

import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.OTP;
import java.sql.*;
import java.time.LocalDateTime;

public class OTP_DAO {

    private Connection connection;

    public OTP_DAO() {
        try {
			this.connection = DatabaseConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public boolean addOTP(OTP otp) {
        String sql = "INSERT INTO OTP (tenDangNhap, otpCode, createdAt, expiresAt) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, otp.getTenDangNhap());
            statement.setString(2, otp.getOtpCode());
            statement.setTimestamp(3, Timestamp.valueOf(otp.getCreatedAt()));
            statement.setTimestamp(4, Timestamp.valueOf(otp.getExpiresAt()));

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public OTP getOTPByUsername(String tenDangNhap) {
    	String sql = "SELECT TOP 1 * FROM OTP WHERE tenDangNhap = ? ORDER BY createdAt DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tenDangNhap);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int otpId = resultSet.getInt("otpId");
                String otpCode = resultSet.getString("otpCode");
                LocalDateTime createdAt = resultSet.getTimestamp("createdAt").toLocalDateTime();
                LocalDateTime expiresAt = resultSet.getTimestamp("expiresAt").toLocalDateTime();

                return new OTP(otpId, tenDangNhap, otpCode, createdAt, expiresAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isOTPValid(String tenDangNhap, String otpCode) {
        OTP otp = getOTPByUsername(tenDangNhap);

        if (otp != null && otp.getOtpCode().equals(otpCode)) {
            if (otp.getExpiresAt().isAfter(LocalDateTime.now())) {
                return true;
            }
        }
        return false;
    }

    public void deleteExpiredOTP() {
        String sql = "DELETE FROM OTP WHERE expiresAt < ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
