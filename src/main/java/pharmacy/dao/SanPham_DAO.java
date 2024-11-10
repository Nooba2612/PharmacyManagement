package pharmacy.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import pharmacy.Interface.SanPham_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.SanPham;

public class SanPham_DAO implements SanPham_Interface {
	private Connection connection;
	private PreparedStatement statement;
	private String query;

	public SanPham_DAO() throws SQLException {
		connection = DatabaseConnection.getConnection();
	}

	@Override
	public boolean createSanPham(SanPham product) {
		String query = "INSERT INTO SanPham (maSanPham, tenSanPham, danhMuc, loaiSanPham, ngaySX, nhaSX, soLuongTon, donGiaBan, thue, hanSuDung, donViTinh, moTa) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, product.getMaSanPham());
			statement.setString(2, product.getTenSanPham());
			statement.setString(3, product.getDanhMuc());
			statement.setString(4, product.getDanhMuc());
			statement.setDate(5, Date.valueOf(product.getNgaySX()));
			statement.setString(6, product.getNhaSX());
			statement.setInt(7, product.getSoLuongTon());
			statement.setDouble(8, product.getDonGiaBan());
			statement.setFloat(9, product.getThue());
			statement.setDate(10, Date.valueOf(product.getHanSuDung()));
			statement.setString(11, product.getDonViTinh());
			statement.setString(12, product.getMoTa());

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

	public boolean createSanPham(SanPham product, Connection connection) {
		String query = "INSERT INTO SanPham (maSanPham, tenSanPham, danhMuc, loaiSanPham, ngaySX, nhaSX, soLuongTon, donGiaBan, thue, hanSuDung, donViTinh, moTa) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, product.getMaSanPham());
			statement.setString(2, product.getTenSanPham());
			statement.setString(3, product.getDanhMuc());
			statement.setString(4, product.getDanhMuc());
			statement.setDate(5, Date.valueOf(product.getNgaySX()));
			statement.setString(6, product.getNhaSX());
			statement.setInt(7, product.getSoLuongTon());
			statement.setDouble(8, product.getDonGiaBan());
			statement.setFloat(9, product.getThue());
			statement.setDate(10, Date.valueOf(product.getHanSuDung()));
			statement.setString(11, product.getDonViTinh());
			statement.setString(12, product.getMoTa());

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

	// public Void batchInsertSanPham(List<SanPham> sanPhamList) {
	//     String insertSQL = "INSERT INTO SanPham (maSanPham, tenSanPham, danhMuc, loaiSanPham, ngaySX, nhaSX, soLuongTon, donGiaBan, thue, hanSuDung, donViTinh, moTa, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	// 	try {
	//     statement = connection.prepareStatement(insertSQL);

	//         for (SanPham sanPham : sanPhamList) {
	//             statement.setString(1, sanPham.getMaSanPham());
	//             statement.setString(2, sanPham.getTenSanPham());
	//             statement.setString(3, sanPham.getDanhMuc());
	//             statement.setString(4, sanPham.getLoaiSanPham());
	//             statement.setDate(5, java.sql.Date.valueOf(sanPham.getNgaySX()));
	//             statement.setString(6, sanPham.getNhaSX());
	//             statement.setInt(7, sanPham.getSoLuongTon());
	//             statement.setDouble(8, sanPham.getDonGiaBan());
	//             statement.setFloat(9, sanPham.getThue());
	// 			statement.setDate(10, java.sql.Date.valueOf(sanPham.getHanSuDung()));
	//             statement.setString(11, sanPham.getDonViTinh());
	//             statement.setString(12, sanPham.getMoTa());
	//             statement.setString(13, sanPham.getTrangThai());

	//             statement.addBatch();
	//         }

	//         statement.executeBatch();
	//     } catch (SQLException e) {
	// 		e.printStackTrace();
	// 		return null;
	// 	} finally {
	// 		if (statement != null) {
	// 			try {
	// 				statement.close();
	// 			} catch (SQLException e) {
	// 				e.printStackTrace();
	// 			}
	// 		}
	// 	}
	// 			return null;
	// }

	@Override
	public SanPham getSanPhamByMaSanPham(String maSanPham) {
		query = "SELECT * FROM SanPham WHERE maSanPham = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maSanPham);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {

				return new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"), rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getTimestamp("ngayCapNhat").toLocalDateTime(), rs.getInt("soLuongTon"),
						rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateSanPham(SanPham product) {
		query = "UPDATE SanPham SET tenSanPham = ?, ngaySX = ?, nhaSX = ?, ngayCapNhat = ?, soLuongTon = ?, "
				+ "donGiaBan = ?, thue = ?, hanSuDung = ?, moTa = ?, donViTinh = ?, trangThai = ?, loaiSanPham = ?, danhMuc = ? WHERE maSanPham = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, product.getTenSanPham());
			statement.setDate(2, Date.valueOf(product.getNgaySX()));
			statement.setString(3, product.getNhaSX());
			statement.setTimestamp(4, Timestamp.valueOf(product.getNgayCapNhat()));
			statement.setInt(5, product.getSoLuongTon());
			statement.setDouble(6, product.getDonGiaBan());
			statement.setFloat(7, product.getThue());
			statement.setDate(8, Date.valueOf(product.getHanSuDung()));
			statement.setString(9, product.getMoTa());
			statement.setString(10, product.getDonViTinh());
			statement.setString(11, product.getTrangThai());
			statement.setString(12, product.getLoaiSanPham());
			statement.setString(13, product.getDanhMuc());
			statement.setString(14, product.getMaSanPham());

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
		List<SanPham> products = new ArrayList<>();
		query = "SELECT * FROM SanPham";

		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				SanPham product = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("danhMuc"), rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"),
						rs.getDate("ngayTao").toLocalDate(),
						rs.getTimestamp("ngayCapNhat").toLocalDateTime(), rs.getInt("soLuongTon"),
						rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				products.add(product);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	public List<SanPham> getTop20SanPhamTheoSLTon() {
		List<SanPham> sanPhams = new ArrayList<>();
		query = "SELECT TOP 20 * FROM SanPham ORDER BY soLuongTon ASC, ngayTao ASC";

		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				SanPham sanPham = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("danhMuc"), rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"),
						rs.getDate("ngayTao").toLocalDate(),
						rs.getTimestamp("ngayCapNhat").toLocalDateTime(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
						sanPhams.add(sanPham);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sanPhams;
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
		query = "SELECT COUNT(*) FROM SanPham WHERE loaiSanPham = N'Thuốc'";
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
	public int countThietBiYTe() {
		query = "SELECT COUNT(*) FROM SanPham WHERE loaiSanPham = N'Thiết bị y tế'";
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
		List<SanPham> productsSapHetTonKho = new ArrayList<>();
		query = "SELECT * FROM SanPham WHERE soLuongTon <= 10";

		try {
			statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				SanPham product = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getTimestamp("ngayCapNhat").toLocalDateTime(), rs.getInt("soLuongTon"),
						rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				productsSapHetTonKho.add(product);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productsSapHetTonKho;
	}

	public List<SanPham> getSanPhamSapHetHanSuDung() {
		List<SanPham> productsSapHetHanSuDung = new ArrayList<>();
		query = "SELECT * FROM SanPham WHERE CONVERT(DATE, hanSuDung) BETWEEN ? AND ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setDate(1, Date.valueOf(LocalDate.now()));
			statement.setDate(2, Date.valueOf(LocalDate.now().plusMonths(1)));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				SanPham product = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getTimestamp("ngayCapNhat").toLocalDateTime(), rs.getInt("soLuongTon"),
						rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				productsSapHetHanSuDung.add(product);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productsSapHetHanSuDung;
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
		List<SanPham> products = new ArrayList<>();
		query = "SELECT * FROM SanPham WHERE hanSuDung < ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setDate(1, Date.valueOf(LocalDate.now()));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				SanPham product = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getTimestamp("ngayCapNhat").toLocalDateTime(), rs.getInt("soLuongTon"),
						rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
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
		List<SanPham> productList = new ArrayList<>();
		ResultSet rs;

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
				SanPham product = new SanPham(
						rs.getString("maSanPham"), rs.getString("tenSanPham"), rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getTimestamp("ngayCapNhat").toLocalDateTime(), rs.getInt("soLuongTon"),
						rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				productList.add(product);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productList;
	}

	public int getSoldQuantityById(String maSanPham, String date) {
		query = "SELECT SUM(cthd.soLuong) AS soLuongBan " +
				"FROM ChiTietHoaDon cthd " +
				"WHERE cthd.maSanPham = ?";
		int result = 0;

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, maSanPham);

			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				result = rs.getInt("soLuongBan");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public void refreshSanPham() {

		// Update statements for various statuses
		String updateExpired = "UPDATE SanPham SET trangThai = N'Hết hạn' WHERE hanSuDung < ?";
		String updateExpiringSoon = "UPDATE SanPham SET trangThai = N'Sắp hết hạn' WHERE hanSuDung BETWEEN ? AND ?";
		String updateLowStock = "UPDATE SanPham SET trangThai = N'Tồn kho thấp' WHERE soLuongTon < ?";
		String updateOutOfStock = "UPDATE SanPham SET trangThai = N'Hết hàng' WHERE soLuongTon = 0";
		String updateInStock = "UPDATE SanPham SET trangThai = N'Có sẵn' WHERE soLuongTon > 0 AND hanSuDung >= ?";

		try {
			statement = connection.prepareStatement(updateInStock);
			statement.setDate(1, Date.valueOf(LocalDate.now()));
			statement.executeUpdate();

			statement = connection.prepareStatement(updateExpired);
			statement.setDate(1, Date.valueOf(LocalDate.now()));
			statement.executeUpdate();

			LocalDate today = LocalDate.now();
			LocalDate expiringSoonDate = today.plusDays(30);
			statement = connection.prepareStatement(updateExpiringSoon);
			statement.setDate(1, Date.valueOf(today));
			statement.setDate(2, Date.valueOf(expiringSoonDate));
			statement.executeUpdate();

			statement = connection.prepareStatement(updateLowStock);
			statement.setInt(1, 10);
			statement.executeUpdate();

			statement = connection.prepareStatement(updateOutOfStock);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<SanPham> getSanPhamByMaOrTenSP(String keySearch) {
		List<SanPham> thuocs = new ArrayList<>();
		query = "SELECT * FROM SanPham WHERE maSanPham LIKE ? OR tenSanPham LIKE ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, "%" + keySearch + "%");
			statement.setString(2, "%" + keySearch + "%");

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				SanPham sanPham = new SanPham(rs.getString("maSanPham"), rs.getString("tenSanPham"),
						rs.getString("danhMuc"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getTimestamp("ngayCapNhat").toLocalDateTime(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"),
						rs.getString("donViTinh"), rs.getString("trangThai"), rs.getString("loaiSanPham"));
				thuocs.add(sanPham);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocs;
	}

	public boolean updateProductStock(String maSanPham, String tenSP, String nsx, int newQuantity, Connection connection) {
		query = "UPDATE SanPham SET soLuongTon = soLuongTon + ? WHERE maSanPham = ? and tenSanPham = ? and nhaSX = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, newQuantity);
			statement.setString(2, maSanPham);
			statement.setString(3, tenSP);
			statement.setString(4, nsx);

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkProductExist(String maSanPham, String tenSP, String nsx) {
		query = "SELECT COUNT(*) FROM SanPham WHERE maSanPham = ? and tenSanPham = ? and nhaSX = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, maSanPham);
			statement.setString(2, tenSP);
			statement.setString(3, nsx);

			ResultSet resultSet = statement.executeQuery();
        
			if (resultSet.next()) {
				int count = resultSet.getInt(1); 
				return count > 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}


	@Override
	public boolean updateProductStock(String maSanPham, String tenSP, String nsx, int newQuantity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateProductStock'");
	}

	@Override
	public List<SanPham> importSanPhamFromCSV(File file)
			throws com.itextpdf.io.exceptions.IOException, CsvValidationException, IOException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'importSanPhamFromCSV'");
	}
}