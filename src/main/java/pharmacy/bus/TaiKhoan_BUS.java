package pharmacy.bus;

import pharmacy.connections.DatabaseConnection;
import pharmacy.dao.TaiKhoan_DAO;
import pharmacy.entity.TaiKhoan;
import pharmacy.utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TaiKhoan_BUS {
	private TaiKhoan_DAO taiKhoanDAO;

	public TaiKhoan_BUS() {
		this.taiKhoanDAO = new TaiKhoan_DAO();
	}
	
	public boolean authenticate(String loginName, String rawPassword) {
	    String query = "SELECT matKhau FROM TaiKhoan WHERE tenDangNhap = ?";
	    
	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {

	        statement.setString(1, loginName);
	        ResultSet rs = statement.executeQuery();

	        if (rs.next()) {
	            String storedPasswordHash = rs.getString("matKhau");
	            return PasswordUtil.checkPassword(rawPassword, storedPasswordHash);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}


	public boolean createAccount(TaiKhoan taiKhoan) {
		return taiKhoanDAO.createAccount(taiKhoan);
	}

	public TaiKhoan getAccountByTenDangNhap(String tenDangNhap) {
		return taiKhoanDAO.getAccountByTenDangNhap(tenDangNhap);
	}

	public boolean updateAccount(TaiKhoan taiKhoan) {
		return taiKhoanDAO.updateAccount(taiKhoan);
	}

	public boolean deleteAccount(String tenDangNhap) {
		return taiKhoanDAO.deleteAccount(tenDangNhap);
	}

	public int countAccounts() {
		return taiKhoanDAO.countAccounts();
	}
}
