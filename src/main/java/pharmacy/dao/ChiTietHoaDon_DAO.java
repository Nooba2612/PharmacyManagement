package pharmacy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.ChiTietHoaDon_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.ChiTietHoaDon;
import pharmacy.entity.HoaDon;
import pharmacy.entity.ThietBiYTe;
import pharmacy.entity.Thuoc;

public class ChiTietHoaDon_DAO implements ChiTietHoaDon_Interface {

	@Override
	public boolean createChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		String query = "INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, maThietBi, soLuong) VALUES (?, ?, ?, ?)";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, chiTietHoaDon.getHoaDon().getMaHoaDon());
			statement.setString(2, chiTietHoaDon.getThuoc().getMaThuoc());
			statement.setString(3,
					chiTietHoaDon.getThietBi() != null ? chiTietHoaDon.getThietBi().getMaThietBi() : null);
			statement.setInt(4, chiTietHoaDon.getSoLuong());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<ChiTietHoaDon> getChiTietHoaDonByMaHoaDon(String maHoaDon) {
	    String query = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
	    List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>(); 

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {

	        statement.setString(1, maHoaDon);
	        ResultSet rs = statement.executeQuery();

	        while (rs.next()) {
	            HoaDon hoaDon = new HoaDon_DAO().getHoaDonById(rs.getString("maHoaDon"));
	            Thuoc thuoc = new Thuoc_DAO().getThuocByMaThuoc(rs.getString("maThuoc"));
				ThietBiYTe thietBi = new ThietBiYTe_DAO().getThietBiYTeByMaThietBi(rs.getString("maThietBi"));

	            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon(hoaDon, thuoc, thietBi, rs.getInt("soLuong"));
	            chiTietHoaDonList.add(chiTietHoaDon);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return chiTietHoaDonList;
	}


	@Override
	public boolean updateChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		String query = "UPDATE ChiTietHoaDon SET maThuoc = ?, maThietBi = ?, soLuong = ? WHERE maHoaDon = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, chiTietHoaDon.getThuoc().getMaThuoc());
			statement.setString(2,
					chiTietHoaDon.getThietBi() != null ? chiTietHoaDon.getThietBi().getMaThietBi() : null);
			statement.setInt(3, chiTietHoaDon.getSoLuong());
			statement.setString(4, chiTietHoaDon.getHoaDon().getMaHoaDon());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteChiTietHoaDon(String maHoaDon) {
		String query = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maHoaDon);
			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
