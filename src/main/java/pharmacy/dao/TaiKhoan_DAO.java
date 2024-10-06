package pharmacy.dao;

import java.sql.*;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.NhanVien;
import pharmacy.entity.TaiKhoan;

public class TaiKhoan_DAO {

	public boolean createAccount(TaiKhoan taiKhoan) {
		String query = "INSERT INTO TaiKhoan (tenDangNhap, matKhau) VALUES (?, ?)";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, taiKhoan.getTenDangNhap().getMaNhanVien());
			statement.setString(2, taiKhoan.getMatKhau());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public TaiKhoan getAccountByTenDangNhap(String tenDangNhap) {
		String query = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, tenDangNhap);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
				NhanVien nhanVien = nhanVienDAO.getEmployeeByMaNhanVien(rs.getString("tenDangNhap"));

				return new TaiKhoan(nhanVien, rs.getString("matKhau"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean updateAccount(TaiKhoan taiKhoan) {
		String query = "UPDATE TaiKhoan SET matKhau = ? WHERE tenDangNhap = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, taiKhoan.getMatKhau());
			statement.setString(2, taiKhoan.getTenDangNhap().getMaNhanVien());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteAccount(String tenDangNhap) {
		String query = "DELETE FROM TaiKhoan WHERE tenDangNhap = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, tenDangNhap);
			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int countAccounts() {
	    String query = "SELECT COUNT(*) FROM TaiKhoan";
	    int count = 0;

	    try (Connection connection = DatabaseConnection.getConnection();
	            PreparedStatement statement = connection.prepareStatement(query);
	            ResultSet rs = statement.executeQuery()) {

	        if (rs.next()) {
	            count = rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return count;
	}

}
