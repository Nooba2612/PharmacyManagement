package pharmacy.Interface;

import pharmacy.entity.TaiKhoan;

public interface TaiKhoan_Interface {
	boolean createAccount(TaiKhoan taiKhoan);

	TaiKhoan getAccountByTenDangNhap(String tenDangNhap);

	boolean updateAccount(TaiKhoan taiKhoan);

	boolean deleteAccount(String tenDangNhap);

	int countAccounts();

	TaiKhoan getCurrentAccount();

	void logoutAccount(String tenDangNhap);

	void changePassword(String username, String newPasswordHashed);
}
