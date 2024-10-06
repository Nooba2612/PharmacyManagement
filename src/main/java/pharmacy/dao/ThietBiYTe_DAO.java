package pharmacy.dao;

import pharmacy.Interface.ThietBiYTe_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.DanhMuc;
import pharmacy.entity.ThietBiYTe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThietBiYTe_DAO implements ThietBiYTe_Interface {

	public boolean createThietBiYTe(ThietBiYTe thietBiYTe) {
		String query = "INSERT INTO ThietBiYTe (maThietBi, tenThietBi, ngaySX, moTa, soLuong) VALUES (?, ?, ?, ?, ?)";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, thietBiYTe.getMaThietBi());
			statement.setString(2, thietBiYTe.getTenThietBi());
			statement.setDate(3, Date.valueOf(thietBiYTe.getNgaySX()));
			statement.setString(4, thietBiYTe.getMoTa());
			statement.setInt(5, thietBiYTe.getSoLuong());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ThietBiYTe getThietBiYTeByMaThietBi(String maThietBi) {
		String query = "SELECT * FROM ThietBiYTe WHERE maThietBi = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet rs = statement.executeQuery();

			statement.setString(1, maThietBi);
			DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));

			if (rs.next()) {
				return new ThietBiYTe(rs.getString("maThietBi"), rs.getString("tenThietBi"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("moTa"), rs.getInt("soLuong"), danhMuc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ThietBiYTe> getAllThietBiYTe() {
		List<ThietBiYTe> thietBiYTeList = new ArrayList<>();
		String query = "SELECT * FROM ThietBiYTe";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet rs = statement.executeQuery()) {

			while (rs.next()) {
				DanhMuc danhMuc = new DanhMuc_DAO().getDanhMucByMaDM(rs.getString("maDanhMuc"));
				thietBiYTeList.add(new ThietBiYTe(rs.getString("maThietBi"), rs.getString("tenThietBi"),
						rs.getDate("ngaySX").toLocalDate(), rs.getString("moTa"), rs.getInt("soLuong"), danhMuc));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thietBiYTeList;
	}

	public boolean updateThietBiYTe(ThietBiYTe thietBiYTe) {
		String query = "UPDATE ThietBiYTe SET tenThietBi = ?, ngaySX = ?, moTa = ?, soLuong = ? WHERE maThietBi = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, thietBiYTe.getTenThietBi());
			statement.setDate(2, Date.valueOf(thietBiYTe.getNgaySX()));
			statement.setString(3, thietBiYTe.getMoTa());
			statement.setInt(4, thietBiYTe.getSoLuong());
			statement.setString(5, thietBiYTe.getMaThietBi());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteThietBiYTe(String maThietBi) {
		String query = "DELETE FROM ThietBiYTe WHERE maThietBi = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maThietBi);
			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int countThietBiYTe() {
	    String query = "SELECT COUNT(*) FROM ThietBiYTe";
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

}
