package pharmacy.dao;

import pharmacy.entity.Thuoc;
import pharmacy.entity.DanhMuc;
import pharmacy.Interface.Thuoc_Interface;
import pharmacy.connections.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Thuoc_DAO implements Thuoc_Interface {

	@Override
	public boolean createThuoc(Thuoc thuoc) {
		String query = "INSERT INTO Thuoc (maThuoc, tenThuoc, ngaySX, nhaSX, ngayTao, ngayCapNhat, soLuongTon, donGiaBan, thue, hanSuDung, moTa) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, thuoc.getMaThuoc());
			statement.setString(2, thuoc.getTenThuoc());
			statement.setDate(3, Date.valueOf(thuoc.getNgaySX()));
			statement.setString(4, thuoc.getNhaSX());
			statement.setDate(5, Date.valueOf(thuoc.getNgayTao()));
			statement.setDate(6, Date.valueOf(thuoc.getNgayCapNhat()));
			statement.setInt(7, thuoc.getSoLuongTon());
			statement.setDouble(8, thuoc.getDonGiaBan());
			statement.setFloat(9, thuoc.getThue());
			statement.setDate(10, Date.valueOf(thuoc.getHanSuDung()));
			statement.setString(11, thuoc.getMoTa());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Thuoc getThuocByMaThuoc(String maThuoc) {
		String query = "SELECT * FROM Thuoc WHERE maThuoc = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maThuoc);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));

				return new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateThuoc(Thuoc thuoc) {
		String query = "UPDATE Thuoc SET tenThuoc = ?, ngaySX = ?, nhaSX = ?, ngayCapNhat = ?, soLuongTon = ?, "
				+ "donGiaBan = ?, thue = ?, hanSuDung = ?, moTa = ? WHERE maThuoc = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, thuoc.getTenThuoc());
			statement.setDate(2, Date.valueOf(thuoc.getNgaySX()));
			statement.setString(3, thuoc.getNhaSX());
			statement.setDate(4, Date.valueOf(thuoc.getNgayCapNhat()));
			statement.setInt(5, thuoc.getSoLuongTon());
			statement.setDouble(6, thuoc.getDonGiaBan());
			statement.setFloat(7, thuoc.getThue());
			statement.setDate(8, Date.valueOf(thuoc.getHanSuDung()));
			statement.setString(9, thuoc.getMoTa());
			statement.setString(10, thuoc.getMaThuoc());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteThuoc(String maThuoc) {
		String query = "DELETE FROM Thuoc WHERE maThuoc = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

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
		String query = "SELECT * FROM Thuoc";

		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {

			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				Thuoc thuoc = new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"));
				thuocs.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocs;
	}

	public int countThuoc() {
		String query = "SELECT COUNT(*) FROM Thuoc";
		int count = 0;

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet rs = statement.executeQuery()) {

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
		String query = "SELECT * FROM Thuoc WHERE soLuongTon < 15";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				Thuoc thuoc = new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"));
				thuocsSapHetTonKho.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocsSapHetTonKho;
	}

	public List<Thuoc> getThuocSapHetHanSuDung() {
		List<Thuoc> thuocsSapHetHanSuDung = new ArrayList<>();
		String query = "SELECT * FROM Thuoc WHERE hanSuDung < ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setDate(1, Date.valueOf(LocalDate.now().plusMonths(1)));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				Thuoc thuoc = new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"));
				thuocsSapHetHanSuDung.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocsSapHetHanSuDung;
	}

	public int countThuocSapHetTonKho() {
		String query = "SELECT COUNT(*) FROM Thuoc WHERE soLuongTon < 15";
		int count = 0;

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet rs = statement.executeQuery()) {

			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int countThuocSapHetHanSuDung() {
		String query = "SELECT COUNT(*) FROM Thuoc WHERE hanSuDung > ? AND hanSuDung <= ?";
		int count = 0;

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

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
		String query = "SELECT * FROM Thuoc WHERE hanSuDung < ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setDate(1, Date.valueOf(LocalDate.now()));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				Thuoc thuoc = new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), danhMuc,
						rs.getDate("ngaySX").toLocalDate(), rs.getString("nhaSX"), rs.getDate("ngayTao").toLocalDate(),
						rs.getDate("ngayCapNhat").toLocalDate(), rs.getInt("soLuongTon"), rs.getDouble("donGiaBan"),
						rs.getFloat("thue"), rs.getDate("hanSuDung").toLocalDate(), rs.getString("moTa"));
				thuocs.add(thuoc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thuocs;
	}

	public int countThuocDaHetHan() {
		String query = "SELECT COUNT(*) FROM Thuoc WHERE hanSuDung < ?";
		int count = 0;

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

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

}
