package pharmacy.dao;

import pharmacy.Interface.KhachHang_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.KhachHang;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
		String query = "UPDATE KhachHang SET hoTen = ?, soDienThoai = ?, namSinh = ?, diemTichLuy = ?, ghiChu = ? WHERE maKhachHang = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, kh.getHoTen());
			statement.setString(2, kh.getSoDienThoai());
			statement.setInt(3, kh.getNamSinh().getYear());
			statement.setInt(4, kh.getDiemTichLuy());
			statement.setString(5, kh.getGhiChu());
			statement.setString(6, kh.getMaKhachHang());

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
				int namSinhInt = rs.getInt("namSinh");
				int diemTichLuy = rs.getInt("diemTichLuy");
				String ghiChu = rs.getString("ghiChu");

				LocalDate namSinh = LocalDate.of(namSinhInt, 1, 1);
				KhachHang kh = new KhachHang(maKhachHang, hoTen, soDienThoai, diemTichLuy, namSinh, ghiChu);
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

				LocalDate namSinh = LocalDate.of(namSinhInt, 1, 1);
				return new KhachHang(maKhachHang, hoTen, soDienThoai, diemTichLuy, namSinh, ghiChu);
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

}
