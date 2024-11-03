package pharmacy.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.PhieuNhap_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.ChiTietPhieuNhap;
import pharmacy.entity.PhieuNhap;

public class PhieuNhap_DAO implements PhieuNhap_Interface {

	public boolean createPhieuNhap(PhieuNhap phieuNhap, Connection connection) {
		String query = "INSERT INTO PhieuNhap (maPhieuNhap, maNhanVien, maNhaCungCap, thoiGianNhap) VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, phieuNhap.getMaPhieuNhap());
			statement.setString(2, phieuNhap.getNhanVien().getMaNhanVien());
			statement.setString(3, phieuNhap.getNhaCungCap().getMaNCC());
			statement.setTimestamp(4, Timestamp.valueOf(phieuNhap.getThoiGianNhap())); // This will correctly set the date and time
			
			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public PhieuNhap getPhieuNhapByMaPhieuNhap(String maPhieuNhap) {
		String query = "SELECT * FROM PhieuNhap WHERE maPhieuNhap = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet rs = statement.executeQuery();

			List<ChiTietPhieuNhap> chiTietPhieuNhapList = new ChiTietPhieuNhap_DAO()
					.getChiTietPhieuNhapByMa(maPhieuNhap);
			statement.setString(1, maPhieuNhap);

			Timestamp thoiGianNhapTimestamp = rs.getTimestamp("thoiGianNhap");
			LocalDateTime thoiGianNhap = (thoiGianNhapTimestamp != null) ? thoiGianNhapTimestamp.toLocalDateTime() : null;

			if (rs.next()) {
				return new PhieuNhap(rs.getString("maPhieuNhap"),
						new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien")),
						new NhaCungCap_DAO().getNhaCungCapByMaNCC(rs.getString("maNhaCungCap")),
						thoiGianNhap, chiTietPhieuNhapList);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<PhieuNhap> getAllPhieuNhap() {
		List<PhieuNhap> phieuNhapList = new ArrayList<>();
		String query = "SELECT * FROM PhieuNhap";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet rs = statement.executeQuery()) {

			while (rs.next()) {
				List<ChiTietPhieuNhap> ChiTietPhieuNhapList = new ChiTietPhieuNhap_DAO()
						.getChiTietPhieuNhapByMa(rs.getString("maPhieuNhap"));
				Timestamp thoiGianNhapTimestamp = rs.getTimestamp("thoiGianNhap");
				LocalDateTime thoiGianNhap = (thoiGianNhapTimestamp != null) ? thoiGianNhapTimestamp.toLocalDateTime() : null;
				phieuNhapList.add(new PhieuNhap(rs.getString("maPhieuNhap"),
						new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien")),
						new NhaCungCap_DAO().getNhaCungCapByMaNCC(rs.getString("maNhaCungCap")),
						thoiGianNhap, ChiTietPhieuNhapList));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return phieuNhapList;
	}

	public boolean updatePhieuNhap(PhieuNhap phieuNhap) {
		String query = "UPDATE PhieuNhap SET maNhanVien = ?, maNhaCungCap = ?, thoiGianNhap = ? WHERE maPhieuNhap = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, phieuNhap.getNhanVien().getMaNhanVien());
			statement.setString(2, phieuNhap.getNhaCungCap().getMaNCC());

			

			statement.setTimestamp(3, Timestamp.valueOf(phieuNhap.getThoiGianNhap())); // This line is updated
			statement.setString(4, phieuNhap.getMaPhieuNhap());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deletePhieuNhap(String maPhieuNhap) {
		String query = "DELETE FROM PhieuNhap WHERE maPhieuNhap = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, maPhieuNhap);
			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int countPhieuNhap() {
		String query = "SELECT COUNT(*) FROM PhieuNhap";
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

	@Override
	public boolean createPhieuNhap(PhieuNhap phieuNhap) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'createPhieuNhap'");
	}
}
