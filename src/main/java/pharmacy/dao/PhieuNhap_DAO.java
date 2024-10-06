package pharmacy.dao;

import pharmacy.Interface.PhieuNhap_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.ChiTietPhieuNhap;
import pharmacy.entity.NhanVien;
import pharmacy.entity.PhieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhap_DAO implements PhieuNhap_Interface {

	public boolean createPhieuNhap(PhieuNhap phieuNhap) {
		String query = "INSERT INTO PhieuNhap (maPhieuNhap, maNhanVien, maNhaCungCap, thoiGianNhap) VALUES (?, ?, ?, ?)";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, phieuNhap.getMaPhieuNhap());
			statement.setString(2, phieuNhap.getNhanVien().getMaNhanVien());
			statement.setString(3, phieuNhap.getNhaCungCap().getMaNCC());
			statement.setDate(4, Date.valueOf(phieuNhap.getThoiGianNhap()));

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

			if (rs.next()) {
				return new PhieuNhap(rs.getString("maPhieuNhap"),
						new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien")),
						new NhaCungCap_DAO().getNhaCungCapByMaNCC(rs.getString("maNhaCungCap")),
						rs.getDate("thoiGianNhap").toLocalDate(), null);
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

				phieuNhapList.add(new PhieuNhap(rs.getString("maPhieuNhap"),
						new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien")),
						new NhaCungCap_DAO().getNhaCungCapByMaNCC(rs.getString("maNhaCungCap")),
						rs.getDate("thoiGianNhap").toLocalDate(), null));
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
			statement.setDate(3, Date.valueOf(phieuNhap.getThoiGianNhap()));
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
}
