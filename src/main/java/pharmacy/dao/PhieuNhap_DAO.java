package pharmacy.dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.PhieuNhap_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.ChiTietPhieuNhap;
import pharmacy.entity.NhaCungCap;
import pharmacy.entity.NhanVien;
import pharmacy.entity.PhieuNhap;
import pharmacy.entity.SanPham;

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

	public List<ChiTietPhieuNhap> getChiTietPhieuNhapByMa(PhieuNhap phieuNhap) {
    List<ChiTietPhieuNhap> chiTietList = new ArrayList<>();
    String query = "SELECT * FROM ChiTietPhieuNhap WHERE maPhieuNhap = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        // Lấy maPhieuNhap từ đối tượng phieuNhap
        statement.setString(1, phieuNhap.getMaPhieuNhap());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            SanPham sanPham = new SanPham_DAO().getSanPhamByMaSanPham(rs.getString("maSanPham"));

            // Truyền đối tượng phieuNhap vào ChiTietPhieuNhap thay vì lấy lại từ cơ sở dữ liệu
            ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap(
				sanPham, 
				phieuNhap, 
				rs.getInt("soLuong"),
                rs.getDouble("donGia"), 
				rs.getFloat("thue"),
				rs.getDate("ngaySX").toLocalDate(), 
				rs.getDate("hanSuDung").toLocalDate());
            chiTietList.add(chiTiet);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return chiTietList;
}

	

public List<PhieuNhap> getAllPhieuNhap() {
    List<PhieuNhap> phieuNhapList = new ArrayList<>();
    String query = "SELECT * FROM PhieuNhap ORDER BY thoiGianNhap desc";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet rs = statement.executeQuery()) {

        ChiTietPhieuNhap_DAO chiTietPhieuNhapDAO = new ChiTietPhieuNhap_DAO();

        while (rs.next()) {
            String maPhieuNhap = rs.getString("maPhieuNhap");

            // Lấy danh sách ChiTietPhieuNhap bằng cách truyền mã phiếu nhập
            List<ChiTietPhieuNhap> chiTietPhieuNhapList = chiTietPhieuNhapDAO.getChiTietPhieuNhapByMa(maPhieuNhap);
			
            // Lấy thời gian nhập
            Timestamp thoiGianNhapTimestamp = rs.getTimestamp("thoiGianNhap");
            LocalDateTime thoiGianNhap = (thoiGianNhapTimestamp != null) ? thoiGianNhapTimestamp.toLocalDateTime() : null;

            // Lấy nhân viên và nhà cung cấp
            NhanVien nhanVien = new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
            NhaCungCap nhaCungCap = new NhaCungCap_DAO().getNhaCungCapByMaNCC(rs.getString("maNhaCungCap"));

            // Tạo đối tượng PhieuNhap với đầy đủ thông tin
            PhieuNhap phieuNhap = new PhieuNhap(maPhieuNhap, nhanVien, nhaCungCap, thoiGianNhap, chiTietPhieuNhapList);
            phieuNhapList.add(phieuNhap);
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

	public List<PhieuNhap> getPhieuNhapByDate(LocalDate fromDate, LocalDate toDate) {
        List<PhieuNhap> phieuNhapList = new ArrayList<>();
        String query = "SELECT * FROM PhieuNhap WHERE thoiGianNhap BETWEEN ? AND ?";

        try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, java.sql.Date.valueOf(fromDate));
            statement.setDate(2, java.sql.Date.valueOf(toDate));

            ResultSet rs = statement.executeQuery();
			ChiTietPhieuNhap_DAO chiTietPhieuNhapDAO = new ChiTietPhieuNhap_DAO();

            while (rs.next()) {
				String maPhieuNhap = rs.getString("maPhieuNhap");

				// Lấy danh sách ChiTietPhieuNhap bằng cách truyền mã phiếu nhập
				List<ChiTietPhieuNhap> chiTietPhieuNhapList = chiTietPhieuNhapDAO.getChiTietPhieuNhapByMa(maPhieuNhap);
				
				// Lấy thời gian nhập
				Timestamp thoiGianNhapTimestamp = rs.getTimestamp("thoiGianNhap");
				LocalDateTime thoiGianNhap = (thoiGianNhapTimestamp != null) ? thoiGianNhapTimestamp.toLocalDateTime() : null;

				// Lấy nhân viên và nhà cung cấp
				NhanVien nhanVien = new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
				NhaCungCap nhaCungCap = new NhaCungCap_DAO().getNhaCungCapByMaNCC(rs.getString("maNhaCungCap"));

				// Tạo đối tượng PhieuNhap với đầy đủ thông tin
				PhieuNhap phieuNhap = new PhieuNhap(maPhieuNhap, nhanVien, nhaCungCap, thoiGianNhap, chiTietPhieuNhapList);
				phieuNhapList.add(phieuNhap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return phieuNhapList;
    }

	@Override
	public boolean createPhieuNhap(PhieuNhap phieuNhap) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'createPhieuNhap'");
	}

	@Override
	public PhieuNhap getPhieuNhapByMaPhieuNhap(String maPhieuNhap) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getPhieuNhapByMaPhieuNhap'");
	}
}
