package pharmacy.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.ChiTietPhieuNhap_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.ChiTietPhieuNhap;
import pharmacy.entity.PhieuNhap;
import pharmacy.entity.SanPham;

public class ChiTietPhieuNhap_DAO implements ChiTietPhieuNhap_Interface {

	@Override
	public boolean createChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
		String query = "INSERT INTO ChiTietPhieuNhap ( maSanPham, maPhieuNhap, soLuong, donGia, thue) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(2, chiTietPhieuNhap.getSanPham().getMaSanPham());
			statement.setString(3, chiTietPhieuNhap.getPhieuNhap().getMaPhieuNhap());
			statement.setInt(4, chiTietPhieuNhap.getSoLuong());
			statement.setDouble(5, chiTietPhieuNhap.getDonGia());
			statement.setDouble(6, chiTietPhieuNhap.getThue());

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
		String query = "UPDATE ChiTietPhieuNhap SET soLuong = ?, donGia = ?, thue = ? WHERE maPhieuNhap = ? AND maSanPham = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(2, chiTietPhieuNhap.getSoLuong());
			statement.setDouble(3, chiTietPhieuNhap.getDonGia());
			statement.setDouble(4, chiTietPhieuNhap.getThue());
			statement.setString(5, chiTietPhieuNhap.getPhieuNhap().getMaPhieuNhap());
			statement.setString(6, chiTietPhieuNhap.getSanPham().getMaSanPham());

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteChiTietPhieuNhap(String maPhieuNhap, String maSanPham) {
		String query = "DELETE FROM ChiTietPhieuNhap WHERE maPhieuNhap = ? AND maSanPham = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maPhieuNhap);
			statement.setString(2, maSanPham);

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<ChiTietPhieuNhap> getChiTietPhieuNhapByMa(String maPhieuNhap) {
		List<ChiTietPhieuNhap> chiTietList = new ArrayList<>();
		String query = "SELECT * FROM ChiTietPhieuNhap WHERE maPhieuNhap = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maPhieuNhap);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				SanPham thuoc = new SanPham_DAO().getSanPhamByMaSanPham(rs.getString("maSanPham"));
				PhieuNhap phieuNhap = new PhieuNhap_DAO().getPhieuNhapByMaPhieuNhap(rs.getString("maPhieuNhap"));

				ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap(thuoc, phieuNhap, rs.getInt("soLuong"),
						rs.getDouble("donGia"), rs.getDouble("thue"));
				chiTietList.add(chiTiet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chiTietList;
	}

	@Override
	public List<ChiTietPhieuNhap> getAllChiTietPhieuNhap() {
		List<ChiTietPhieuNhap> list = new ArrayList<>();
		String query = "SELECT * FROM ChiTietPhieuNhap";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet rs = statement.executeQuery()) {

			while (rs.next()) {
				SanPham thuoc = new SanPham_DAO().getSanPhamByMaSanPham(rs.getString("maSanPham"));
				PhieuNhap phieuNhap = new PhieuNhap_DAO().getPhieuNhapByMaPhieuNhap(rs.getString("maPhieuNhap"));

				ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap(thuoc, phieuNhap, rs.getInt("soLuong"),
						rs.getDouble("donGia"), rs.getDouble("thue"));
				list.add(chiTiet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
