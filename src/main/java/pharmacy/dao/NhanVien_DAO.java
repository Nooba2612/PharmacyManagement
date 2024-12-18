package pharmacy.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.NhanVien_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.NhanVien;

public class NhanVien_DAO implements NhanVien_Interface {

	@Override
	public boolean createEmployee(NhanVien nhanVien) {
		String query = "INSERT INTO NhanVien (maNhanVien, hoTen, chucVu, soDienThoai, email, ngayVaoLam, namSinh, trangThai, trinhDo, gioiTinh, cccd, tienLuong) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, nhanVien.getMaNhanVien());
			statement.setString(2, nhanVien.getHoTen());
			statement.setString(3, nhanVien.getChucVu());
			statement.setString(4, nhanVien.getSoDienThoai());
			statement.setString(5, nhanVien.getEmail());
			statement.setDate(6, Date.valueOf(nhanVien.getNgayVaoLam()));
			statement.setDate(7, Date.valueOf(nhanVien.getNamSinh()));
			statement.setString(8, nhanVien.getTrangThai());
			statement.setString(9, nhanVien.getTrinhDo());
			statement.setString(10, nhanVien.getGioiTinh());
			statement.setString(11, nhanVien.getCccd());
			statement.setDouble(12, nhanVien.getTienLuong());

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public NhanVien getEmployeeByMaNhanVien(String maNhanVien) {
		String query = "SELECT * FROM NhanVien WHERE maNhanVien = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maNhanVien);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				return new NhanVien(rs.getString("maNhanVien"), rs.getString("hoTen"), rs.getString("chucVu"),
						rs.getString("soDienThoai"), rs.getString("email"), rs.getDate("ngayVaoLam").toLocalDate(),
						rs.getString("trangThai"), rs.getString("trinhDo"), rs.getString("gioiTinh"),
						rs.getDate("namSinh").toLocalDate(), rs.getDouble("tienLuong"), rs.getString("cccd"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public NhanVien getEmployeeByEmail(String email) {
		String query = "SELECT * FROM NhanVien WHERE email = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, email);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				return new NhanVien(rs.getString("maNhanVien"), rs.getString("hoTen"), rs.getString("chucVu"),
						rs.getString("soDienThoai"), rs.getString("email"), rs.getDate("ngayVaoLam").toLocalDate(),
						rs.getString("trangThai"), rs.getString("trinhDo"), rs.getString("gioiTinh"),
						rs.getDate("namSinh").toLocalDate(), rs.getDouble("tienLuong"), rs.getString("cccd"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateEmployee(NhanVien nhanVien) {
		String query = "UPDATE NhanVien SET hoTen = ?, chucVu = ?, soDienThoai = ?, ngayVaoLam = ?, "
				+ "trangThai = ?, trinhDo = ?, gioiTinh = ?, namSinh = ?, email = ? " + "WHERE maNhanVien = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, nhanVien.getHoTen());
			statement.setString(2, nhanVien.getChucVu());
			statement.setString(3, nhanVien.getSoDienThoai());

			if (nhanVien.getNgayVaoLam() != null) {
				statement.setDate(4, Date.valueOf(nhanVien.getNgayVaoLam()));
			} else {
				statement.setNull(4, java.sql.Types.DATE);
			}

			statement.setString(5, nhanVien.getTrangThai());
			statement.setString(6, nhanVien.getTrinhDo());
			statement.setString(7, nhanVien.getGioiTinh());

			if (nhanVien.getNamSinh() != null) {
				statement.setDate(8, Date.valueOf(nhanVien.getNamSinh()));
			} else {
				statement.setNull(8, java.sql.Types.DATE);
			}

			statement.setString(9, nhanVien.getEmail());
			statement.setString(10, nhanVien.getMaNhanVien());

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			System.err.println("Input employee error: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteEmployee(String maNhanVien) {
		String query = "DELETE FROM NhanVien WHERE maNhanVien = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maNhanVien);
			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<NhanVien> getAllEmployees() {
		List<NhanVien> employees = new ArrayList<>();
		String query = "SELECT * FROM NhanVien";

		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {

			while (rs.next()) {
				NhanVien nhanVien = new NhanVien(rs.getString("maNhanVien"), rs.getString("hoTen"),
						rs.getString("chucVu"), rs.getString("soDienThoai"), rs.getString("email"),
						rs.getDate("ngayVaoLam").toLocalDate(), rs.getString("trangThai"), rs.getString("trinhDo"),
						rs.getString("gioiTinh"), rs.getDate("namSinh").toLocalDate(), rs.getDouble("tienLuong"),
						rs.getString("cccd"));
				employees.add(nhanVien);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employees;
	}

	@Override
	public int countEmployees() {
		String query = "SELECT COUNT(*) FROM NhanVien";
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
	public List<NhanVien> getTopRevenueEmployees(String date) {
		List<NhanVien> topEmployees = new ArrayList<>();
		String[] dateParts = date.split("[/-]"); // Tách ngày, tháng, năm từ chuỗi nhập vào
		String query = "";

		switch (dateParts.length) {
		case 3:
			query = "SELECT nv.*, subquery.totalRevenue " + "FROM NhanVien nv "
					+ "JOIN (SELECT maNhanVien, SUM(tongTien) AS totalRevenue " + "FROM HoaDon "
					+ "WHERE CAST(ngayTao AS DATE) = ? " + "GROUP BY maNhanVien) AS subquery "
					+ "ON nv.maNhanVien = subquery.maNhanVien " + "ORDER BY subquery.totalRevenue DESC";
			break;
		case 2:
			query = "SELECT nv.*, subquery.totalRevenue " + "FROM NhanVien nv "
					+ "JOIN (SELECT maNhanVien, SUM(tongTien) AS totalRevenue " + "FROM HoaDon "
					+ "WHERE YEAR(CAST(ngayTao AS DATE)) = ? AND MONTH(CAST(ngayTao AS DATE)) = ? "
					+ "GROUP BY maNhanVien) AS subquery " + "ON nv.maNhanVien = subquery.maNhanVien "
					+ "ORDER BY subquery.totalRevenue DESC";
			break;
		case 1:
			query = "SELECT nv.*, subquery.totalRevenue " + "FROM NhanVien nv "
					+ "JOIN (SELECT maNhanVien, SUM(tongTien) AS totalRevenue " + "FROM HoaDon "
					+ "WHERE YEAR(CAST(ngayTao AS DATE)) = ? " + "GROUP BY maNhanVien) AS subquery "
					+ "ON nv.maNhanVien = subquery.maNhanVien " + "ORDER BY subquery.totalRevenue DESC";
			break;
		default:
			break;
		}

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			if (dateParts.length == 3) {
				statement.setString(1, date);
			} else if (dateParts.length == 2) {
				statement.setString(1, dateParts[0]); // Năm
				statement.setString(2, dateParts[1]); // Tháng
			} else if (dateParts.length == 1) {
				statement.setString(1, dateParts[0]); // Năm
			}

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				NhanVien nhanVien = new NhanVien(rs.getString("maNhanVien"), rs.getString("hoTen"),
						rs.getString("chucVu"), rs.getString("soDienThoai"), rs.getString("email"),
						rs.getDate("ngayVaoLam").toLocalDate(), rs.getString("trangThai"), rs.getString("trinhDo"),
						rs.getString("gioiTinh"), rs.getDate("namSinh").toLocalDate(), rs.getDouble("tienLuong"),
						rs.getString("cccd"));
				topEmployees.add(nhanVien);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return topEmployees;
	}

	@Override
	public int getOrderQuantityOfEmployee(String maNhanVien) {
		int orderQuantity = 0;

		String query = "SELECT COUNT(*) AS orderCount " + "FROM HoaDon " + "WHERE maNhanVien = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, maNhanVien);

			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					orderQuantity = rs.getInt("orderCount");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return orderQuantity;
	}

	@Override
	public List<NhanVien> getEmployeesByStatus(String status) {
		String sql = "SELECT * FROM NhanVien";

		switch (status) {
		case "Tất cả nhân viên" -> {
			return getAllEmployees();
		}
		case "Nhân viên đã nghỉ" -> {
			sql += " WHERE trangThai = N'Đã nghỉ việc'";
		}
		case "Nhân viên còn làm" -> {
			sql += " WHERE trangThai = N'Còn làm việc'";
		}
		case "Nhân viên nghỉ tạm thời" -> {
			sql += " WHERE trangThai = N'Nghỉ phép'";
		}
		default -> {
			return new ArrayList<>();
		}
		}

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			List<NhanVien> employees = new ArrayList<>();
			while (rs.next()) {
				NhanVien nv = new NhanVien(rs.getString("maNhanVien"), rs.getString("hoTen"), rs.getString("chucVu"),
						rs.getString("soDienThoai"), rs.getString("email"), rs.getDate("ngayVaoLam").toLocalDate(),
						rs.getString("trangThai"), rs.getString("trinhDo"), rs.getString("gioiTinh"),
						rs.getDate("namSinh").toLocalDate(), rs.getDouble("tienLuong"), rs.getString("cccd"));
				employees.add(nv);
			}
			return employees;

		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

}
