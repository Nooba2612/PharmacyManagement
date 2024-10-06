package pharmacy.dao;

import pharmacy.Interface.NhaCungCap_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.NhaCungCap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCap_DAO implements NhaCungCap_Interface {

    public boolean createNhaCungCap(NhaCungCap nhaCungCap) {
        String query = "INSERT INTO NhaCungCap (maNCC, tenNCC, soDienThoai, diaChi, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nhaCungCap.getMaNCC());
            statement.setString(2, nhaCungCap.getTenNCC());
            statement.setString(3, nhaCungCap.getSoDienThoai());
            statement.setString(4, nhaCungCap.getDiaChi());
            statement.setString(5, nhaCungCap.getEmail());

            int result = statement.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public NhaCungCap getNhaCungCapByMaNCC(String maNCC) {
        String query = "SELECT * FROM NhaCungCap WHERE maNCC = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, maNCC);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new NhaCungCap(
                    rs.getString("maNCC"),
                    rs.getString("tenNCC"),
                    rs.getString("soDienThoai"),
                    rs.getString("diaChi"),
                    rs.getString("email")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhaCungCap> getAllNhaCungCap() {
        List<NhaCungCap> nhaCungCapList = new ArrayList<>();
        String query = "SELECT * FROM NhaCungCap";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                nhaCungCapList.add(new NhaCungCap(
                    rs.getString("maNCC"),
                    rs.getString("tenNCC"),
                    rs.getString("soDienThoai"),
                    rs.getString("diaChi"),
                    rs.getString("email")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhaCungCapList;
    }

    public boolean updateNhaCungCap(NhaCungCap nhaCungCap) {
        String query = "UPDATE NhaCungCap SET tenNCC = ?, soDienThoai = ?, diaChi = ?, email = ? WHERE maNCC = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nhaCungCap.getTenNCC());
            statement.setString(2, nhaCungCap.getSoDienThoai());
            statement.setString(3, nhaCungCap.getDiaChi());
            statement.setString(4, nhaCungCap.getEmail());
            statement.setString(5, nhaCungCap.getMaNCC());

            int result = statement.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNhaCungCap(String maNCC) {
        String query = "DELETE FROM NhaCungCap WHERE maNCC = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, maNCC);
            int result = statement.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int countNhaCungCap() {
        String query = "SELECT COUNT(*) FROM NhaCungCap";
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
