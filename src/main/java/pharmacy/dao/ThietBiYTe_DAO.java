package pharmacy.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.ThietBiYTe_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.DanhMuc;
import pharmacy.entity.ThietBiYTe;

public class ThietBiYTe_DAO implements ThietBiYTe_Interface {

	private Connection connection;
	private PreparedStatement statement;
	private ResultSet rs;
	private String query;

	public ThietBiYTe_DAO() throws SQLException {
		connection = DatabaseConnection.getConnection();
	}

	public boolean createThietBiYTe(ThietBiYTe thietBiYTe) {
		query = "INSERT INTO ThietBiYTe (maThietBi, tenThietBi, ngaySX, moTa, soLuong, donGiaBan, maDanhMuc) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, thietBiYTe.getMaThietBi());
			statement.setString(2, thietBiYTe.getTenThietBi());
			statement.setDate(3, Date.valueOf(thietBiYTe.getNgaySX()));
			statement.setString(4, thietBiYTe.getMoTa());
			statement.setInt(5, thietBiYTe.getSoLuongTon());
			statement.setDouble(6, thietBiYTe.getDonGiaBan());
			statement.setString(7, thietBiYTe.getDanhMuc().getMaDM());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ThietBiYTe getThietBiYTeByMaThietBi(String maThietBi) {
		query = "SELECT * FROM ThietBiYTe WHERE maThietBi = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maThietBi);
			rs = statement.executeQuery();

			if (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				return new ThietBiYTe(rs.getString("maThietBi"), rs.getString("tenThietBi"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("moTa"), rs.getInt("soLuong"),
						rs.getDouble("donGiaBan"), danhMuc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ThietBiYTe> getAllThietBiYTe() {
		List<ThietBiYTe> thietBiYTeList = new ArrayList<>();
		query = "SELECT * FROM ThietBiYTe";

		try {
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();

			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				thietBiYTeList.add(new ThietBiYTe(rs.getString("maThietBi"), rs.getString("tenThietBi"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("moTa"), rs.getInt("soLuong"),
						rs.getDouble("donGiaBan"), danhMuc));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thietBiYTeList;
	}

	public boolean updateThietBiYTe(ThietBiYTe thietBiYTe) {
		query = "UPDATE ThietBiYTe SET tenThietBi = ?, ngaySX = ?, moTa = ?, soLuong = ? WHERE maThietBi = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, thietBiYTe.getTenThietBi());
			statement.setDate(2, Date.valueOf(thietBiYTe.getNgaySX()));
			statement.setString(3, thietBiYTe.getMoTa());
			statement.setInt(4, thietBiYTe.getSoLuongTon());
			statement.setString(5, thietBiYTe.getMaThietBi());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteThietBiYTe(String maThietBi) {
		query = "DELETE FROM ThietBiYTe WHERE maThietBi = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maThietBi);
			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int countThietBiYTe() {
		query = "SELECT COUNT(*) FROM ThietBiYTe";
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

	public List<ThietBiYTe> getTopSaleThietBiYTeByDate(String date) {
		String[] dateParts = date.split("[/-]");
		List<ThietBiYTe> thietBiYTes = new ArrayList<>();

		switch (dateParts.length) {
			case 3:
				query = "SELECT t.*, SUM(cthd.soLuong) AS soLuongBan " +
						"FROM ThietBiYTe t " +
						"JOIN ChiTietHoaDon cthd ON cthd.maThietBi = t.maThietBi " +
						"JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon " +
						"WHERE CAST(hd.ngayTao AS DATE) = ? " +
						"GROUP BY t.maThietBi, t.tenThietBi, t.maDanhMuc, t.ngaySX, t.moTa, t.soLuongTon, t.donGiaBan "
						+
						"ORDER BY soLuongBan DESC";
				break;
			case 2:
				query = "SELECT t.*, SUM(cthd.soLuong) AS soLuongBan " +
						"FROM ThietBiYTe t " +
						"JOIN ChiTietHoaDon cthd ON cthd.maThietBi = t.maThietBi " +
						"JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon " +
						"WHERE YEAR(CAST(hd.ngayTao AS DATE)) = ? AND MONTH(CAST(hd.ngayTao AS DATE)) = ? " +
						"GROUP BY t.maThietBi, t.tenThietBi, t.maDanhMuc, t.ngaySX, t.moTa, t.soLuongTon, t.donGiaBan "
						+
						"ORDER BY soLuongBan DESC";
				break;
			case 1:
				query = "SELECT t.*, SUM(cthd.soLuong) AS soLuongBan " +
						"FROM ThietBiYTe t " +
						"JOIN ChiTietHoaDon cthd ON cthd.maThietBi = t.maThietBi " +
						"JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon " +
						"WHERE YEAR(CAST(hd.ngayTao AS DATE)) = ? " +
						"GROUP BY t.maThietBi, t.tenThietBi, t.maDanhMuc, t.ngaySX, t.moTa, t.soLuongTon, t.donGiaBan "
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
				statement.setString(1, dateParts[0]); // Năm
				statement.setString(2, dateParts[1]); // Tháng
			} else if (dateParts.length == 1) {
				statement.setString(1, dateParts[0]); // Năm
			}

			rs = statement.executeQuery();

			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				thietBiYTes.add(new ThietBiYTe(rs.getString("maThietBi"), rs.getString("tenThietBi"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("moTa"), rs.getInt("soLuongTon"),
						rs.getDouble("donGiaBan"), danhMuc));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return thietBiYTes;
	}

	public int getSoldQuantityById(String maThietBi, String date) {
		query = "SELECT SUM(cthd.soLuong) AS soLuongBan " +
				"FROM ChiTietHoaDon cthd " +
				"WHERE cthd.maThietBi = ?";
		int result = 0;

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maThietBi);

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
