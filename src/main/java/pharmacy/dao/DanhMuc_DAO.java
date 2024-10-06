package pharmacy.dao;

import pharmacy.Interface.DanhMuc_Interface;
import pharmacy.entity.DanhMuc;
import pharmacy.entity.Thuoc;
import pharmacy.connections.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DanhMuc_DAO implements DanhMuc_Interface {
	@Override
	public boolean createDanhMuc(DanhMuc danhMuc) {
		String query = "INSERT INTO DanhMuc (maDM, tenDM, moTa, loaiDM) VALUES (?, ?, ?, ?)";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, danhMuc.getMaDM());
			statement.setString(2, danhMuc.getTenDM());
			statement.setString(3, danhMuc.getMoTa());
			statement.setInt(4, danhMuc.getLoaiDM());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public DanhMuc getDanhMucByMaDM(String maDM) {
		String query = "SELECT * FROM DanhMuc WHERE maDM = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maDM);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				return new DanhMuc(rs.getString("maDM"), rs.getString("tenDM"), new ArrayList<>(), // Assuming thuoc
																									// list is fetched
																									// separately
						rs.getString("moTa"), rs.getInt("loaiDM"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateDanhMuc(DanhMuc danhMuc) {
		String query = "UPDATE DanhMuc SET tenDM = ?, moTa = ?, loaiDM = ? WHERE maDM = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, danhMuc.getTenDM());
			statement.setString(2, danhMuc.getMoTa());
			statement.setInt(3, danhMuc.getLoaiDM());
			statement.setString(4, danhMuc.getMaDM());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteDanhMuc(String maDM) {
		String query = "DELETE FROM DanhMuc WHERE maDM = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maDM);
			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<DanhMuc> getAllDanhMuc() {
		List<DanhMuc> danhMucs = new ArrayList<>();
		String query = "SELECT * FROM DanhMuc";

		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {

			while (rs.next()) {
				danhMucs.add(new DanhMuc(rs.getString("maDM"), rs.getString("tenDM"), new ArrayList<>(), // Assuming
																											// thuoc
																											// list is
																											// fetched
																											// separately
						rs.getString("moTa"), rs.getInt("loaiDM")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return danhMucs;
	}

}
