package pharmacy.dao;

import pharmacy.entity.LichLamViec;
import pharmacy.entity.NhanVien;
import pharmacy.Interface.LichLamViec_Interface;
import pharmacy.connections.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LichLamViec_DAO implements LichLamViec_Interface {

	@Override
	public boolean createLichLamViec(LichLamViec lichLamViec) {
		String query = "INSERT INTO LichLamViec (maLichLamViec, maNhanVien, caLam, ngayLam) VALUES (?, ?, ?, ?)";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, lichLamViec.getMaLichLamViec());
			statement.setString(2, lichLamViec.getNhanVien().getMaNhanVien());
			statement.setString(3, lichLamViec.getCaLam());
			statement.setDate(4, Date.valueOf(lichLamViec.getNgayLam()));

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public LichLamViec getLichLamViecById(String maLichLamViec) {
		String query = "SELECT * FROM LichLamViec WHERE maLichLamViec = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maLichLamViec);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				NhanVien nhanVien = new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
				String caLam = rs.getString("caLam");
				LocalDate ngayLam = rs.getDate("ngayLam").toLocalDate();

				return new LichLamViec(maLichLamViec, caLam, nhanVien, ngayLam);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<LichLamViec> getAllLichLamViec() {
		List<LichLamViec> lichLamViecList = new ArrayList<>();
		String query = "SELECT * FROM LichLamViec";

		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {

			while (rs.next()) {
				NhanVien nhanVien = new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
				String caLam = rs.getString("caLam");
				LocalDate ngayLam = rs.getDate("ngayLam").toLocalDate();

				LichLamViec lichLamViec = new LichLamViec(rs.getString("maLichLamViec"), caLam, nhanVien, ngayLam);
				lichLamViecList.add(lichLamViec);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lichLamViecList;
	}

	@Override
	public boolean updateLichLamViec(LichLamViec lichLamViec) {
		String query = "UPDATE LichLamViec SET maNhanVien = ?, caLam = ?, ngayLam = ? WHERE maLichLamViec = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, lichLamViec.getNhanVien().getMaNhanVien());
			statement.setString(2, lichLamViec.getCaLam());
			statement.setDate(3, Date.valueOf(lichLamViec.getNgayLam()));
			statement.setString(4, lichLamViec.getMaLichLamViec());

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteLichLamViec(String maLichLamViec) {
		String query = "DELETE FROM LichLamViec WHERE maLichLamViec = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maLichLamViec);
			return statement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
