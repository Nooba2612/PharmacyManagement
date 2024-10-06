package pharmacy.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.NhanVien_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.NhanVien;

public class NhanVien_DAO implements NhanVien_Interface {

    public boolean createEmployee(NhanVien nhanVien) {
        String query = "INSERT INTO NhanVien (maNhanVien, hoTen, chucVu, soDienThoai, ngayVaoLam, namSinh, trangThai, trinhDo, gioiTinh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nhanVien.getMaNhanVien());
            statement.setString(2, nhanVien.getHoTen());
            statement.setString(3, nhanVien.getChucVu());
            statement.setString(4, nhanVien.getSoDienThoai());
            statement.setDate(5, Date.valueOf(nhanVien.getNgayVaoLam()));
            statement.setDate(6, Date.valueOf(nhanVien.getNamSinh()));
            statement.setString(7, nhanVien.getTrangThai());
            statement.setString(8, nhanVien.getTrinhDo());
            statement.setString(9, nhanVien.getGioiTinh());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public NhanVien getEmployeeByMaNhanVien(String maNhanVien) {
        String query = "SELECT * FROM NhanVien WHERE maNhanVien = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, maNhanVien);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("hoTen"),
                    rs.getString("email"),
                    rs.getString("soDienThoai"),
                    rs.getDate("ngayVaoLam").toLocalDate(),
                    rs.getString("trangThai"),
                    rs.getString("trinhDo"),
                    rs.getString("gioiTinh"),
                    rs.getDate("namSinh").toLocalDate()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateEmployee(NhanVien nhanVien) {
        String query = "UPDATE NhanVien SET hoTen = ?, chucVu = ?, soDienThoai = ?, ngayVaoLam = ?, trangThai = ?, trinhDo = ?, gioiTinh = ?, namSinh = ? WHERE maNhanVien = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nhanVien.getHoTen());
            statement.setString(2, nhanVien.getChucVu());
            statement.setString(3, nhanVien.getSoDienThoai());
            statement.setDate(4, Date.valueOf(nhanVien.getNgayVaoLam()));
            statement.setString(5, nhanVien.getTrangThai());
            statement.setString(6, nhanVien.getTrinhDo());
            statement.setString(7, nhanVien.getGioiTinh());
            statement.setDate(8, Date.valueOf(nhanVien.getNamSinh()));
            statement.setString(9, nhanVien.getMaNhanVien());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmployee(String maNhanVien) {
        String query = "DELETE FROM NhanVien WHERE maNhanVien = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, maNhanVien);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<NhanVien> getAllEmployees() {
        List<NhanVien> employees = new ArrayList<>();
        String query = "SELECT * FROM NhanVien";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                NhanVien nhanVien = new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("hoTen"),
                    rs.getString("email"),
                    rs.getString("soDienThoai"),
                    rs.getDate("ngayVaoLam").toLocalDate(),
                    rs.getString("trangThai"),
                    rs.getString("trinhDo"),
                    rs.getString("gioiTinh"),
                    rs.getDate("namSinh").toLocalDate()
                );
                employees.add(nhanVien);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public int countEmployees() {
        String query = "SELECT COUNT(*) FROM NhanVien";
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
