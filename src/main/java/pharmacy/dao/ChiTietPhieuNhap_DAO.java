package pharmacy.dao;

import pharmacy.Interface.ChiTietPhieuNhap_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.ChiTietPhieuNhap;
import pharmacy.entity.Thuoc;
import pharmacy.entity.PhieuNhap;
import pharmacy.entity.ThietBiYTe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuNhap_DAO implements ChiTietPhieuNhap_Interface {

	@Override
	public boolean createChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
		String query = "INSERT INTO ChiTietPhieuNhap (maThietBi, maThuoc, maPhieuNhap, soLuong, donGia, thue) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1,
					chiTietPhieuNhap.getThietBiYTe() != null ? chiTietPhieuNhap.getThietBiYTe().getMaThietBi() : null);
			statement.setString(2, chiTietPhieuNhap.getThuoc().getMaThuoc());
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
		String query = "UPDATE ChiTietPhieuNhap SET maThietBi = ?, soLuong = ?, donGia = ?, thue = ? WHERE maPhieuNhap = ? AND maThuoc = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1,
					chiTietPhieuNhap.getThietBiYTe() != null ? chiTietPhieuNhap.getThietBiYTe().getMaThietBi() : null);
			statement.setInt(2, chiTietPhieuNhap.getSoLuong());
			statement.setDouble(3, chiTietPhieuNhap.getDonGia());
			statement.setDouble(4, chiTietPhieuNhap.getThue());
			statement.setString(5, chiTietPhieuNhap.getPhieuNhap().getMaPhieuNhap());
			statement.setString(6, chiTietPhieuNhap.getThuoc().getMaThuoc());

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteChiTietPhieuNhap(String maPhieuNhap, String maThuoc) {
		String query = "DELETE FROM ChiTietPhieuNhap WHERE maPhieuNhap = ? AND maThuoc = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maPhieuNhap);
			statement.setString(2, maThuoc);

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
				Thuoc thuoc = new Thuoc_DAO().getThuocByMaThuoc(rs.getString("maThuoc"));
				PhieuNhap phieuNhap = new PhieuNhap_DAO().getPhieuNhapByMaPhieuNhap(rs.getString("maPhieuNhap"));
				ThietBiYTe thietBi = new ThietBiYTe_DAO().getThietBiYTeByMaThietBi(rs.getString("maThietBi"));

				ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap(thuoc, phieuNhap, thietBi, rs.getInt("soLuong"),
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
				Thuoc thuoc = new Thuoc_DAO().getThuocByMaThuoc(rs.getString("maThuoc"));
				PhieuNhap phieuNhap = new PhieuNhap_DAO().getPhieuNhapByMaPhieuNhap(rs.getString("maPhieuNhap"));
				ThietBiYTe thietBi = new ThietBiYTe_DAO().getThietBiYTeByMaThietBi(rs.getString("maThietBi"));

				ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap(thuoc, phieuNhap, thietBi, rs.getInt("soLuong"),
						rs.getDouble("donGia"), rs.getDouble("thue"));
				list.add(chiTiet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
