package pharmacy.dao;

import java.sql.*;

import pharmacy.Interface.TaiKhoan_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.NhanVien;
import pharmacy.entity.TaiKhoan;
import pharmacy.utils.PasswordUtil;

public class TaiKhoan_DAO implements TaiKhoan_Interface {
	private Connection connection;
	private PreparedStatement statement;
	private ResultSet rs;
	private String query;

	public TaiKhoan_DAO() throws SQLException {
		connection = DatabaseConnection.getConnection();
	}

	public boolean authenticate(String loginName, String rawPassword) {
		query = "SELECT matKhau FROM TaiKhoan WHERE tenDangNhap = ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, loginName);
			rs = statement.executeQuery();

			if (rs.next()) {
				String storedPasswordHash = rs.getString("matKhau");
				if (PasswordUtil.checkPassword(rawPassword, storedPasswordHash)) {
					query = "UPDATE TaiKhoan SET isLoggedIn = 0 WHERE tenDangNhap != ?";
					statement = connection.prepareStatement(query);
					statement.setString(1, loginName);
					statement.executeUpdate();

					query = "UPDATE TaiKhoan SET isLoggedIn = ? WHERE tenDangNhap = ?";
					statement = connection.prepareStatement(query);
					statement.setInt(1, 1);
					statement.setString(2, loginName);
					statement.executeUpdate();

					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return false;
	}

	public boolean createAccount(TaiKhoan taiKhoan) {
		query = "INSERT INTO TaiKhoan (tenDangNhap, matKhau) VALUES (?, ?)";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, taiKhoan.getTenDangNhap().getMaNhanVien());
			statement.setString(2, taiKhoan.getMatKhau());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}

	public TaiKhoan getAccountByTenDangNhap(String tenDangNhap) {
		query = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, tenDangNhap);
			rs = statement.executeQuery();

			if (rs.next()) {
				NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
				NhanVien nhanVien = nhanVienDAO.getEmployeeByMaNhanVien(rs.getString("tenDangNhap"));

				return new TaiKhoan(nhanVien, rs.getString("matKhau"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return null;
	}

	public boolean updateAccount(TaiKhoan taiKhoan) {
		query = "UPDATE TaiKhoan SET matKhau = ? WHERE tenDangNhap = ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, taiKhoan.getMatKhau());
			statement.setString(2, taiKhoan.getTenDangNhap().getMaNhanVien());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}

	public boolean deleteAccount(String tenDangNhap) {
		query = "DELETE FROM TaiKhoan WHERE tenDangNhap = ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, tenDangNhap);
			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}

	public int countAccounts() {
		query = "SELECT COUNT(*) FROM TaiKhoan";
		int count = 0;

		try {
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return count;
	}

	public TaiKhoan getCurrentAccount() {
		query = "SELECT * FROM TaiKhoan WHERE isLoggedIn = 1";
		try {
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();

			if (rs.next()) {
				NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
				NhanVien nhanVien = nhanVienDAO.getEmployeeByMaNhanVien(rs.getString("tenDangNhap"));

				return new TaiKhoan(nhanVien, rs.getString("matKhau"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return null;
	}

	public void logoutAccount(String tenDangNhap) {
		query = "UPDATE TaiKhoan SET isLoggedIn = 0 WHERE tenDangNhap = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, tenDangNhap);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void closeResources() {
		try {
			if (rs != null)
				rs.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
