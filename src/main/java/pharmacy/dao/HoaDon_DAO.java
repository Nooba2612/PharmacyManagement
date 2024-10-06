package pharmacy.dao;

import pharmacy.entity.*;
import pharmacy.Interface.HoaDon_Interface;
import pharmacy.connections.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO implements HoaDon_Interface {

	@Override
	public boolean createHoaDon(HoaDon hoaDon) {
		String query = "INSERT INTO HoaDon (maHoaDon, maKhachHang, maNhanVien, ngayTao, tienKhachDua, diemSuDung, loaiThanhToan) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, hoaDon.getMaHoaDon());
			statement.setString(2, hoaDon.getKhachHang().getMaKhachHang());
			statement.setString(3, hoaDon.getNhanVien().getMaNhanVien());
			statement.setDate(4, Date.valueOf(hoaDon.getNgayTao()));
			statement.setDouble(5, hoaDon.getTienKhachDua());
			statement.setDouble(6, hoaDon.getDiemSuDung());
			statement.setString(7, hoaDon.getLoaiThanhToan());

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public HoaDon getHoaDonById(String maHoaDon) {
		String query = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maHoaDon);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				KhachHang khachHang = new KhachHang_DAO().getKhachHangById(rs.getString("maKhachHang"));
				NhanVien nhanVien = new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
				Thuoc thuoc = new Thuoc_DAO().getThuocByMaThuoc(rs.getString("thuoc"));
				LocalDate ngayTao = rs.getDate("ngayTao").toLocalDate();
				double tienKhachDua = rs.getDouble("tienKhachDua");
				double diemSuDung = rs.getDouble("diemSuDung");
				String loaiThanhToan = rs.getString("loaiThanhToan");
				List<ChiTietHoaDon> chiTietHoaDonList = new ChiTietHoaDon_DAO().getChiTietHoaDonByMaHoaDon(maHoaDon);

				return new HoaDon(maHoaDon, khachHang, nhanVien, ngayTao, tienKhachDua, diemSuDung, loaiThanhToan,
						chiTietHoaDonList);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<HoaDon> getAllHoaDon() {
		List<HoaDon> hoaDonList = new ArrayList<>();
		String query = "SELECT * FROM HoaDon";

		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {

			while (rs.next()) {
				String maHoaDon = rs.getString("maHoaDon");
				KhachHang khachHang = new KhachHang_DAO().getKhachHangById(rs.getString("maKhachHang"));
				NhanVien nhanVien = new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
				Thuoc thuoc = new Thuoc_DAO().getThuocByMaThuoc(rs.getString("thuoc"));
				LocalDate ngayTao = rs.getDate("ngayTao").toLocalDate();
				double tienKhachDua = rs.getDouble("tienKhachDua");
				double diemSuDung = rs.getDouble("diemSuDung");
				String loaiThanhToan = rs.getString("loaiThanhToan");
				List<ChiTietHoaDon> chiTietHoaDonList = new ChiTietHoaDon_DAO().getChiTietHoaDonByMaHoaDon(maHoaDon);

				HoaDon hoaDon = new HoaDon(maHoaDon, khachHang, nhanVien, ngayTao, tienKhachDua, diemSuDung,
						loaiThanhToan, chiTietHoaDonList);
				hoaDonList.add(hoaDon);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hoaDonList;
	}

	@Override
	public boolean updateHoaDon(HoaDon hoaDon) {
		String query = "UPDATE HoaDon SET maKhachHang = ?, maNhanVien = ?, ngayTao = ?, tienKhachDua = ?, diemSuDung = ?, loaiThanhToan = ? WHERE maHoaDon = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, hoaDon.getKhachHang().getMaKhachHang());
			statement.setString(2, hoaDon.getNhanVien().getMaNhanVien());
			statement.setDate(3, Date.valueOf(hoaDon.getNgayTao()));
			statement.setDouble(4, hoaDon.getTienKhachDua());
			statement.setDouble(5, hoaDon.getDiemSuDung());
			statement.setString(6, hoaDon.getLoaiThanhToan());
			statement.setString(7, hoaDon.getMaHoaDon());

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteHoaDon(String maHoaDon) {
		String query = "DELETE FROM HoaDon WHERE maHoaDon = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maHoaDon);
			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int countHoaDon() {
		String query = "SELECT COUNT(*) AS total FROM HoaDon";
		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {

			if (rs.next()) {
				return rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String calculateTotalRevenue() {
		String query = "SELECT SUM(c.soLuong * h.tongTien) AS TotalRevenue " + "FROM ChiTietHoaDon c "
				+ "JOIN HoaDon h ON c.maHoaDon = h.maHoaDon";
		double totalRevenue = 0;

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet rs = statement.executeQuery()) {

			if (rs.next()) {
				totalRevenue = rs.getDouble("TotalRevenue");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String revenueString = String.format("%.0f", totalRevenue);

		StringBuilder result = new StringBuilder(revenueString);

		for (int i = result.length() - 3; i > 0; i -= 3) {
			result.insert(i, '.');
		}

		return result.toString();
	}

	public String calculateRevenueByDate(String date) {
		String query = "SELECT SUM(c.soLuong * h.tongTien) AS RevenueByDate " + "FROM ChiTietHoaDon c "
				+ "JOIN HoaDon h ON c.maHoaDon = h.maHoaDon " + "WHERE CAST(h.ngayTao AS DATE) = ?";
		double revenueByDate = 0;

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, date);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				revenueByDate = rs.getDouble("RevenueByDate");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String revenueString = String.format("%.0f", revenueByDate);

		StringBuilder result = new StringBuilder(revenueString);

		for (int i = result.length() - 3; i > 0; i -= 3) {
			result.insert(i, '.');
		}

		return result.toString();
	}

	public String calculateRevenueByMonth(int year, int month) {
		String query = "SELECT SUM(c.soLuong * h.tongTien) AS RevenueByMonth " + "FROM ChiTietHoaDon c "
				+ "JOIN HoaDon h ON c.maHoaDon = h.maHoaDon " + "WHERE YEAR(h.ngayTao) = ? AND MONTH(h.ngayTao) = ?";
		double revenueByMonth = 0;

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, year);
			statement.setInt(2, month);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				revenueByMonth = rs.getDouble("RevenueByMonth");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String revenueString = String.format("%.0f", revenueByMonth);

		StringBuilder result = new StringBuilder(revenueString);

		for (int i = result.length() - 3; i > 0; i -= 3) {
			result.insert(i, '.');
		}

		return result.toString();
	}

	public String calculateRevenueByYear(int year) {
		String query = "SELECT SUM(c.soLuong * h.tongTien) AS RevenueByYear " + "FROM ChiTietHoaDon c "
				+ "JOIN HoaDon h ON c.maHoaDon = h.maHoaDon " + "WHERE YEAR(h.ngayTao) = ?";
		double revenueByYear = 0;

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, year);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				revenueByYear = rs.getDouble("RevenueByYear");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String revenueString = String.format("%.0f", revenueByYear);

		StringBuilder result = new StringBuilder(revenueString);

		for (int i = result.length() - 3; i > 0; i -= 3) {
			result.insert(i, '.');
		}

		return result.toString();
	}

}
