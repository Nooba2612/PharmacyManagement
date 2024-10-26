package pharmacy.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.SanPham_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.SanPham;

public class SanPham_DAO implements SanPham_Interface {
	private Connection connection;
	private PreparedStatement statement;
	private ResultSet rs;
	private String query;

	public SanPham_DAO() throws SQLException {
		connection = DatabaseConnection.getConnection();
	}

	@Override
	public boolean createSanPham(SanPham thuoc) {
		String query = "INSERT INTO SanPham (maSanPham, tenSanPham, danhMuc, ngaySX, nhaSX, soLuongTon, donGiaBan, thue, hanSuDung, donViTinh, moTa) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, thuoc.getMaSanPham());
			statement.setString(2, thuoc.getTenSanPham());
			statement.setString(3, thuoc.getDanhMuc());
			statement.setDate(4, Date.valueOf(thuoc.getNgaySX()));
			statement.setString(5, thuoc.getNhaSX());
			statement.setInt(6, thuoc.getSoLuongTon());
			statement.setDouble(7, thuoc.getDonGiaBan());
			statement.setFloat(8, thuoc.getThue());
			statement.setDate(9, Date.valueOf(thuoc.getHanSuDung()));
			statement.setString(10, thuoc.getDonViTinh());
			statement.setString(11, thuoc.getMoTa());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public SanPham getSanPhamByMaSanPham(String maSanPham) {
		query = "SELECT * FROM SanPham WHERE maSanPham = ? WHERE d = 0";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maSanPham);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {

				return new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"), rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateSanPham(SanPham thuoc) {
		query = "UPDATE SanPham SET tenSanPham = ?, ngaySX = ?, nhaSX = ?, ngayCapNhat = ?, soLuongTon = ?, "
				+ "donGiaBan = ?, thue = ?, hanSuDung = ?, moTa = ?, donViTinh = ?, trangThai = ? WHERE maSanPham = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, thuoc.getTenSanPham());
			statement.setDate(2, Date.valueOf(thuoc.getNgaySX()));
			statement.setString(3, thuoc.getNhaSX());
			statement.setDate(4, Date.valueOf(thuoc.getNgayCapNhat()));
			statement.setInt(5, thuoc.getSoLuongTon());
			statement.setDouble(6, thuoc.getDonGiaBan());
			statement.setFloat(7, thuoc.getThue());
			statement.setDate(8, Date.valueOf(thuoc.getHanSuDung()));
			statement.setString(9, thuoc.getMoTa());
			statement.setString(10, thuoc.getDonViTinh());
			statement.setString(11, thuoc.getTrangThai());
			statement.setString(12, thuoc.getMaSanPham());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteSanPham(String maSanPham) {
		query = "UPDATE SanPham SET d = 1 WHERE maSanPham = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maSanPham);
			int result = statement.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<SanPham> getAllSanPham() {
		List<SanPham> thuocs = new ArrayList<>();
		query = "SELECT * FROM SanPham";

		try {
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();

			while (rs.next()) {
				SanPham thuoc = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("danhMuc"), rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"),
						rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				thuocs.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocs;
	}

	public int countSanPham() {
		query = "SELECT COUNT(*) FROM SanPham";
		int count = 0;

		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int countThuoc() {
		query = "SELECT COUNT(*) FROM SanPham WHERE loaiSanPham = 'Thuốc'";
		int count = 0;

		try {
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int countThietBiYTe() {
		query = "SELECT COUNT(*) FROM SanPham WHERE loaiSanPham = 'Thiết bị'";
		int count = 0;

		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public List<SanPham> getSanPhamSapHetTonKho() {
		List<SanPham> thuocsSapHetTonKho = new ArrayList<>();
		query = "SELECT * FROM SanPham WHERE soLuongTon < 15";

		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				SanPham thuoc = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				thuocsSapHetTonKho.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocsSapHetTonKho;
	}

	public List<SanPham> getSanPhamSapHetHanSuDung() {
		List<SanPham> thuocsSapHetHanSuDung = new ArrayList<>();
		query = "SELECT * FROM SanPham WHERE CONVERT(DATE, hanSuDung) BETWEEN ? AND ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setDate(1, Date.valueOf(LocalDate.now()));
			statement.setDate(2, Date.valueOf(LocalDate.now().plusMonths(1)));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				SanPham thuoc = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				thuocsSapHetHanSuDung.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocsSapHetHanSuDung;
	}

	public int countSanPhamSapHetTonKho() {
		query = "SELECT COUNT(*) FROM SanPham WHERE soLuongTon < 15";
		int count = 0;

		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int countSanPhamSapHetHanSuDung() {
		String query = "SELECT COUNT(*) FROM SanPham WHERE CONVERT(DATE, hanSuDung) BETWEEN ? AND ?";
		int count = 0;

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setDate(1, Date.valueOf(LocalDate.now()));
			statement.setDate(2, Date.valueOf(LocalDate.now().plusMonths(1)));

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public List<SanPham> getSanPhamDaHetHan() {
		List<SanPham> thuocs = new ArrayList<>();
		query = "SELECT * FROM SanPham WHERE hanSuDung < ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setDate(1, Date.valueOf(LocalDate.now()));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				SanPham thuoc = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				thuocs.add(thuoc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocs;
	}

	public int countSanPhamDaHetHan() {
		query = "SELECT COUNT(*) FROM SanPham WHERE hanSuDung < ?";
		int count = 0;

		try {
			statement = connection.prepareStatement(query);
			statement.setDate(1, Date.valueOf(LocalDate.now()));

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public List<SanPham> getTopSaleSanPhamByDate(String date) {
		String[] dateParts = date.split("[/-]");
		List<SanPham> thuocList = new ArrayList<>();

		switch (dateParts.length) {
			case 3:
				query = "SELECT t.*, SUM(cthd.soLuong) AS soLuongBan " +
						"FROM SanPham t " +
						"JOIN ChiTietHoaDon cthd ON t.maSanPham = cthd.maSanPham " +
						"JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon " +
						"WHERE CAST(hd.ngayTao AS DATE) = ? " +
						"GROUP BY t.maSanPham, t.tenSanPham, t.danhMuc, t.ngaySX, t.nhaSX, t.ngayTao, " +
						"t.ngayCapNhat, t.donViTinh, t.soLuongTon, t.donGiaBan, t.thue, t.hanSuDung, t.moTa, t.trangThai, t.loaiSanPham "
						+
						"ORDER BY soLuongBan DESC";
				break;
			case 2:
				query = "SELECT t.*, SUM(cthd.soLuong) AS soLuongBan " +
						"FROM SanPham t " +
						"JOIN ChiTietHoaDon cthd ON t.maSanPham = cthd.maSanPham " +
						"JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon " +
						"WHERE YEAR(CAST(hd.ngayTao AS DATE)) = ? AND MONTH(CAST(hd.ngayTao AS DATE)) = ? "
						+
						"GROUP BY t.maSanPham, t.tenSanPham, t.danhMuc, t.ngaySX, t.nhaSX, t.ngayTao, " +
						"t.ngayCapNhatonViTinh, t.soLuongTon, t.donGiaBan, t.thue, t.hanSuDung, t.moTa, t.trangThai, t.loaiSanPham "
						+
						"ORDER BY soLuongBan DESC";
				break;
			case 1:
				query = "SELECT t.*, SUM(cthd.soLuong) AS soLuongBan " +
						"FROM SanPham t " +
						"JOIN ChiTietHoaDon cthd ON t.maSanPham = cthd.maSanPham " +
						"JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon " +
						"WHERE YEAR(CAST(hd.ngayTao AS DATE)) = ? " +
						"GROUP BY t.maSanPham, t.tenSanPham, t.danhMuc, t.ngaySX, t.nhaSX, t.ngayTao, " +
						"t.ngayCapNhat, t.donViTinh, t.soLuongTon, t.donGiaBan, t.thue, t.hanSuDung, t.moTa, t.trangThai, t.loaiSanPham "
						+
						"ORDER BY soLuongBan DESC";
				break;
			default:
				break;
		}

		try {
			statement = connection.prepareStatement(query);
			if (dateParts.length == 3) {
				statement.setString(1, date);
			} else if (dateParts.length == 2) {
				statement.setString(1, dateParts[0]); // Year
				statement.setString(2, dateParts[1]); // Month
			} else if (dateParts.length == 1) {
				statement.setString(1, dateParts[0]); // Year
			}

			rs = statement.executeQuery();
			while (rs.next()) {
				SanPham thuoc = new SanPham(
						rs.getString("maSanPham"), rs.getString("tenSanPham"), rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				thuocList.add(thuoc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return thuocList;
	}

	public int getSoldQuantityById(String maSanPham, String date) {
		query = "SELECT SUM(cthd.soLuong) AS soLuongBan " +
				"FROM ChiTietHoaDon cthd " +
				"WHERE cthd.maSanPham = ?";
		int result = 0;

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maSanPham);

			rs = statement.executeQuery();

			if (rs.next()) {
				result = rs.getInt("soLuongBan");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}