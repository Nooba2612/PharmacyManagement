package pharmacy.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.Thuoc_Interface;
import pharmacy.bus.DanhMuc_BUS;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.DanhMuc;
import pharmacy.entity.Thuoc;

public class Thuoc_DAO implements Thuoc_Interface {
	private Connection connection;
	private PreparedStatement statement;
	private ResultSet rs;
	private String query;

	public Thuoc_DAO() throws SQLException {
		connection = DatabaseConnection.getConnection();
	}

	@Override
	public boolean createThuoc(Thuoc thuoc) {
		String query = "INSERT INTO Thuoc (maThuoc, tenThuoc, maDanhMuc, ngaySX, nhaSX, soLuongTon, donGiaBan, thue, hanSuDung, donViTinh, moTa) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, thuoc.getMaThuoc());
			statement.setString(2, thuoc.getTenThuoc());
			statement.setString(3, thuoc.getDanhMuc().getMaDM()); // Đặt mã danh mục
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
	public Thuoc getThuocByMaThuoc(String maThuoc) {
		query = "SELECT * FROM Thuoc WHERE maThuoc = ? WHERE d = 0";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maThuoc);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_BUS().getDanhMucByMaDM(rs.getString("maDanhMuc"));

				return new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateThuoc(Thuoc thuoc) {
		query = "UPDATE Thuoc SET tenThuoc = ?, ngaySX = ?, nhaSX = ?, ngayCapNhat = ?, soLuongTon = ?, "
				+ "donGiaBan = ?, thue = ?, hanSuDung = ?, moTa = ?, trangThai = ? WHERE maThuoc = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, thuoc.getTenThuoc());
			statement.setDate(2, Date.valueOf(thuoc.getNgaySX()));
			statement.setString(3, thuoc.getNhaSX());
			statement.setDate(4, Date.valueOf(thuoc.getNgayCapNhat()));
			statement.setInt(5, thuoc.getSoLuongTon());
			statement.setDouble(6, thuoc.getDonGiaBan());
			statement.setFloat(7, thuoc.getThue());
			statement.setDate(8, Date.valueOf(thuoc.getHanSuDung()));
			statement.setString(9, thuoc.getMoTa());
			statement.setString(10, thuoc.getTrangThai());
			statement.setString(11, thuoc.getMaThuoc());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteThuoc(String maThuoc) {
		query = "UPDATE Thuoc SET d = 1 WHERE maThuoc = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maThuoc);
			int result = statement.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Thuoc> getAllThuoc() {
		List<Thuoc> thuocs = new ArrayList<>();
		query = "SELECT * FROM Thuoc";

		try {
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();

			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				Thuoc thuoc = new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"));
				thuocs.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocs;
	}

	public int countThuoc() {
		query = "SELECT COUNT(*) FROM Thuoc";
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

	public List<Thuoc> getThuocSapHetTonKho() {
		List<Thuoc> thuocsSapHetTonKho = new ArrayList<>();
		query = "SELECT * FROM Thuoc WHERE soLuongTon < 15";

		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				Thuoc thuoc = new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"));
				thuocsSapHetTonKho.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocsSapHetTonKho;
	}

	public List<Thuoc> getThuocSapHetHanSuDung() {
		List<Thuoc> thuocsSapHetHanSuDung = new ArrayList<>();
		query = "SELECT * FROM Thuoc WHERE CONVERT(DATE, hanSuDung) BETWEEN ? AND ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setDate(1, Date.valueOf(LocalDate.now()));
			statement.setDate(2, Date.valueOf(LocalDate.now().plusMonths(1)));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				Thuoc thuoc = new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"));
				thuocsSapHetHanSuDung.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocsSapHetHanSuDung;
	}

	public int countThuocSapHetTonKho() {
		query = "SELECT COUNT(*) FROM Thuoc WHERE soLuongTon < 15";
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

	public int countThuocSapHetHanSuDung() {
		String query = "SELECT COUNT(*) FROM Thuoc WHERE CONVERT(DATE, hanSuDung) BETWEEN ? AND ?";
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

	public List<Thuoc> getThuocDaHetHan() {
		List<Thuoc> thuocs = new ArrayList<>();
		query = "SELECT * FROM Thuoc WHERE hanSuDung < ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setDate(1, Date.valueOf(LocalDate.now()));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				Thuoc thuoc = new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"));
				thuocs.add(thuoc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocs;
	}

	public int countThuocDaHetHan() {
		query = "SELECT COUNT(*) FROM Thuoc WHERE hanSuDung < ?";
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

	public List<Thuoc> getTopSaleThuocByDate(String date) {
		String[] dateParts = date.split("[/-]");
		List<Thuoc> thuocList = new ArrayList<>();

		switch (dateParts.length) {
			case 3:
				query = "SELECT t.*, SUM(cthd.soLuong) AS soLuongBan " +
						"FROM Thuoc t " +
						"JOIN ChiTietHoaDon cthd ON t.maThuoc = cthd.maThuoc " +
						"JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon " +
						"WHERE CAST(hd.ngayTao AS DATE) = ? " +
						"GROUP BY t.maThuoc, t.tenThuoc, t.maDanhMuc, t.ngaySX, t.nhaSX, t.ngayTao, " +
						"t.ngayCapNhat, t.donViTinh, t.soLuongTon, t.donGiaBan, t.thue, t.hanSuDung, t.moTa, t.trangThai "
						+
						"ORDER BY soLuongBan DESC";
				break;
			case 2:
				query = "SELECT t.*, SUM(cthd.soLuong) AS soLuongBan " +
						"FROM Thuoc t " +
						"JOIN ChiTietHoaDon cthd ON t.maThuoc = cthd.maThuoc " +
						"JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon " +
						"WHERE YEAR(CAST(hd.ngayTao AS DATE)) = ? AND MONTH(CAST(hd.ngayTao AS DATE)) = ? "
						+
						"GROUP BY t.maThuoc, t.tenThuoc, t.maDanhMuc, t.ngaySX, t.nhaSX, t.ngayTao, " +
						"t.ngayCapNhatonViTinh, t.soLuongTon, t.donGiaBan, t.thue, t.hanSuDung, t.moTa, t.trangThai "
						+
						"ORDER BY soLuongBan DESC";
				break;
			case 1:
				query = "SELECT t.*, SUM(cthd.soLuong) AS soLuongBan " +
						"FROM Thuoc t " +
						"JOIN ChiTietHoaDon cthd ON t.maThuoc = cthd.maThuoc " +
						"JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon " +
						"WHERE YEAR(CAST(hd.ngayTao AS DATE)) = ? " +
						"GROUP BY t.maThuoc, t.tenThuoc, t.maDanhMuc, t.ngaySX, t.nhaSX, t.ngayTao, " +
						"t.ngayCapNhat, t.donViTinh, t.soLuongTon, t.donGiaBan, t.thue, t.hanSuDung, t.moTa, t.trangThai "
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
				DanhMuc danhMuc = new DanhMuc_BUS().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				Thuoc thuoc = new Thuoc(
						rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"));
				thuocList.add(thuoc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return thuocList;
	}

	public int getSoldQuantityById(String maThuoc, String date) {
		query = "SELECT SUM(cthd.soLuong) AS soLuongBan " +
				"FROM ChiTietHoaDon cthd " +
				"WHERE cthd.maThuoc = ?";
		int result = 0;

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maThuoc);

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
