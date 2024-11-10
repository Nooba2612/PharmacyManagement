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

public class ChiTietHoaDon_DAO implements ChiTietHoaDon_Interface {

    @Override
    public boolean createChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        String query = "INSERT INTO ChiTietHoaDon (maHoaDon, maSanPham, soLuong, donGiaNhap, lieuLuong, thue) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chiTietHoaDon.getMaHoaDon());
            statement.setString(2, chiTietHoaDon.getMaSanPham());
            statement.setInt(3, chiTietHoaDon.getSoLuong());
            statement.setDouble(4, chiTietHoaDon.getDonGiaNhap());
            statement.setString(5, chiTietHoaDon.getLieuLuong());
            statement.setFloat(6, chiTietHoaDon.getThue());

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

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, maHoaDon);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon(
                        rs.getString("maHoaDon"),
                        rs.getString("maSanPham"),
                        rs.getInt("soLuong"),
                        rs.getFloat("thue"),
                        rs.getString("lieuLuong"),
                        rs.getDouble("donGiaNhap")
                );
                chiTietHoaDonList.add(chiTietHoaDon);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chiTietHoaDonList;
    }

    @Override
    public boolean updateChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        String query = "UPDATE ChiTietHoaDon SET maSanPham = ?, soLuong = ?, donGiaNhap = ?, thue = ?, lieuLuong = ? WHERE maHoaDon = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chiTietHoaDon.getMaSanPham());
            statement.setInt(2, chiTietHoaDon.getSoLuong());
            statement.setDouble(3, chiTietHoaDon.getDonGiaNhap());
            statement.setFloat(4, chiTietHoaDon.getThue());
            statement.setString(5, chiTietHoaDon.getLieuLuong());
            statement.setString(6, chiTietHoaDon.getMaHoaDon());

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

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, maHoaDon);
            int result = statement.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
