package pharmacy.bus;

import java.sql.SQLException;

import pharmacy.dao.TaiKhoan_DAO;
import pharmacy.entity.TaiKhoan;

public class TaiKhoan_BUS {
	private TaiKhoan_DAO taiKhoanDAO;

	public TaiKhoan_BUS() throws SQLException {
		this.taiKhoanDAO = new TaiKhoan_DAO();
	}

	public boolean authenticate(String loginName, String rawPassword) {
		return taiKhoanDAO.authenticate(loginName, rawPassword);
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

	public TaiKhoan getCurrentAccount() {
		return taiKhoanDAO.getCurrentAccount();
	}

	public void logoutAccount(String tenDangNhap) {
		taiKhoanDAO.logoutAccount(tenDangNhap);
	}

}
