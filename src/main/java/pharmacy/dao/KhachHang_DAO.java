package pharmacy.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.KhachHang_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.KhachHang;

public class KhachHang_DAO implements KhachHang_Interface {

	@Override
	public boolean addKhachHang(KhachHang kh) {
		String query = "INSERT INTO KhachHang (maKhachHang, hoTen, soDienThoai, namSinh, diemTichLuy, ghiChu) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, kh.getMaKhachHang());
			statement.setString(2, kh.getHoTen());
			statement.setString(3, kh.getSoDienThoai());
			statement.setInt(4, kh.getNamSinh().getYear());
			statement.setInt(5, kh.getDiemTichLuy());
			statement.setString(6, kh.getGhiChu());

			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateKhachHang(KhachHang kh) {
		String query = "UPDATE KhachHang SET hoTen = ?, soDienThoai = ?, namSinh = ?, diemTichLuy = ?, gioiTinh = ?, ghiChu = ? WHERE maKhachHang = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			
			statement.setString(1, kh.getHoTen());
			statement.setString(2, kh.getSoDienThoai());
			statement.setDate(3, Date.valueOf(kh.getNamSinh()));
			statement.setInt(4, kh.getDiemTichLuy());
			statement.setString(5, kh.getGioiTinh());
			statement.setString(6, kh.getGhiChu());
			statement.setString(7, kh.getMaKhachHang());
			
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteKhachHang(String maKhachHang) {
		String query = "DELETE FROM KhachHang WHERE maKhachHang = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, maKhachHang);
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<KhachHang> getAllKhachHang() {
		List<KhachHang> khachHangList = new ArrayList<>();
		String query = "SELECT * FROM KhachHang";
		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {

			while (rs.next()) {
				String maKhachHang = rs.getString("maKhachHang");
				String hoTen = rs.getString("hoTen");
				String soDienThoai = rs.getString("soDienThoai");
				LocalDate namSinh = rs.getDate("namSinh") != null ? rs.getDate("namSinh").toLocalDate() : null;
				int diemTichLuy = rs.getInt("diemTichLuy");
				String ghiChu = rs.getString("ghiChu");
				String gioiTinh = rs.getString("gioiTinh");

				KhachHang kh = new KhachHang(maKhachHang, hoTen, soDienThoai, diemTichLuy, namSinh, ghiChu, gioiTinh);
				khachHangList.add(kh);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return khachHangList;
	}

	@Override
	public KhachHang getKhachHangById(String maKhachHang) {
		String query = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet rs = statement.executeQuery();

			statement.setString(1, maKhachHang);
			if (rs.next()) {
				String hoTen = rs.getString("hoTen");
				String soDienThoai = rs.getString("soDienThoai");
				int namSinhInt = rs.getInt("namSinh");
				int diemTichLuy = rs.getInt("diemTichLuy");
				String ghiChu = rs.getString("ghiChu");
				String gioiTinh = rs.getString("gioiTinh");

				LocalDate namSinh = LocalDate.of(namSinhInt, 1, 1);
				return new KhachHang(maKhachHang, hoTen, soDienThoai, diemTichLuy, namSinh, ghiChu, gioiTinh);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int countKhachHang() {
		String query = "SELECT COUNT(*) FROM KhachHang";
		int count = 0;
		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {

			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<KhachHang> getNewCustomerByDate(String date) {
		String[] dateParts = date.split("[/-]");

		List<KhachHang> newCustomerList = new ArrayList<>();
		String query = "";

		switch (dateParts.length) {
			case 3:
				query = "SELECT kh.maKhachHang, kh.hoTen, kh.soDienThoai, CONVERT(DATE, kh.namSinh) AS namSinh, kh.diemTichLuy, kh.gioiTinh, MIN(hd.ngayTao) AS ngayDangKy "
						+ "FROM KhachHang kh "
						+ "JOIN HoaDon hd ON kh.maKhachHang = hd.maKhachHang "
						+ "WHERE kh.soDienThoai IS NOT NULL "
						+ "GROUP BY kh.maKhachHang, kh.hoTen, kh.soDienThoai, CONVERT(DATE, kh.namSinh), kh.diemTichLuy, kh.gioiTinh "
						+ "HAVING CONVERT(DATE, MIN(hd.ngayTao)) = ?";
				break;

			case 2:
				query = "SELECT kh.maKhachHang, kh.hoTen, kh.soDienThoai, CONVERT(DATE, kh.namSinh) AS namSinh, kh.diemTichLuy, kh.gioiTinh, MIN(hd.ngayTao) AS ngayDangKy "
						+ "FROM KhachHang kh "
						+ "JOIN HoaDon hd ON kh.maKhachHang = hd.maKhachHang "
						+ "WHERE kh.soDienThoai IS NOT NULL "
						+ "GROUP BY kh.maKhachHang, kh.hoTen, kh.soDienThoai, CONVERT(DATE, kh.namSinh), kh.diemTichLuy, kh.gioiTinh "
						+ "HAVING YEAR(MIN(hd.ngayTao)) = ? AND MONTH(MIN(hd.ngayTao)) = ?";
				break;

			case 1:
				query = "SELECT kh.maKhachHang, kh.hoTen, kh.soDienThoai, CONVERT(DATE, kh.namSinh) AS namSinh, kh.diemTichLuy, kh.gioiTinh, MIN(hd.ngayTao) AS ngayDangKy "
						+ "FROM KhachHang kh "
						+ "JOIN HoaDon hd ON kh.maKhachHang = hd.maKhachHang "
						+ "WHERE kh.soDienThoai IS NOT NULL "
						+ "GROUP BY kh.maKhachHang, kh.hoTen, kh.soDienThoai, CONVERT(DATE, kh.namSinh), kh.diemTichLuy, kh.gioiTinh "
						+ "HAVING YEAR(MIN(hd.ngayTao)) = ?";
				break;

			default:
				break;
		}

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			switch (dateParts.length) {
				case 3:
					statement.setString(1, date);
					break;

				case 2:
					statement.setString(1, dateParts[0]);
					statement.setString(2, dateParts[1]);
					break;

				case 1:
					statement.setString(1, dateParts[0]);
					break;
			}

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				KhachHang khachHang = new KhachHang(
						rs.getString("maKhachHang"),
						rs.getString("hoTen"),
						rs.getString("soDienThoai"),
						rs.getInt("diemTichLuy"),
						rs.getDate("namSinh").toLocalDate(),
						"", rs.getString("gioiTinh"));
				newCustomerList.add(khachHang);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return newCustomerList;
	}

	public List<KhachHang> getTopCustomer() {
		List<KhachHang> topCustomers = new ArrayList<>();
		String query = "SELECT maKhachHang, hoTen, soDienThoai, CONVERT(DATE, namSinh) AS namSinh, diemTichLuy, gioiTinh, ghiChu FROM KhachHang ORDER BY diemTichLuy DESC";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet rs = statement.executeQuery()) {

			while (rs.next()) {
				KhachHang khachHang = new KhachHang(
						rs.getString("maKhachHang"),
						rs.getString("hoTen"),
						rs.getString("soDienThoai"),
						rs.getInt("diemTichLuy"),
						rs.getDate("namSinh").toLocalDate(),
						rs.getString("ghiChu"),
						rs.getString("gioiTinh"));
				topCustomers.add(khachHang);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return topCustomers;
	}

}
