package pharmacy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.HistoryLog_Interface;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.NhanVien;
import pharmacy.entity.ProductHistoryLog;
import pharmacy.entity.SanPham;

public class HistoryLog_DAO implements HistoryLog_Interface {
    private Connection connection;
    private PreparedStatement statement;
    private String query;

    public HistoryLog_DAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void addProductHistory(ProductHistoryLog history) {
        query = "INSERT INTO NhatKyThayDoiSanPham (maSanPham, tenSanPham, danhMuc, loaiSanPham, ngaySX, nhaSX, ngayTao, ngayCapNhat, soLuongTon, donGiaBan, thue, hanSuDung, donViTinh, moTa, trangThai, maNhanVien) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, history.getSanPham().getMaSanPham());
            statement.setString(2, history.getSanPham().getTenSanPham());
            statement.setString(3, history.getSanPham().getDanhMuc());
            statement.setString(4, history.getSanPham().getLoaiSanPham());
            statement.setDate(5, java.sql.Date.valueOf(history.getSanPham().getNgaySX()));
            statement.setString(6, history.getSanPham().getNhaSX());
            statement.setDate(7, java.sql.Date.valueOf(history.getSanPham().getNgayTao()));
            statement.setDate(8, java.sql.Date.valueOf(history.getSanPham().getNgayCapNhat()));
            statement.setInt(9, history.getSanPham().getSoLuongTon());
            statement.setDouble(10, history.getSanPham().getDonGiaBan());
            statement.setFloat(11, history.getSanPham().getThue());
            statement.setDate(12, java.sql.Date.valueOf(history.getSanPham().getHanSuDung()));
            statement.setString(13, history.getSanPham().getDonViTinh());
            statement.setString(14, history.getSanPham().getMoTa());
            statement.setString(15, history.getSanPham().getTrangThai());
            statement.setString(16, history.getNhanVien().getMaNhanVien());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<ProductHistoryLog> getAllProductHistory() {
        List<ProductHistoryLog> productHistoryList = new ArrayList<>();
        query = "SELECT * FROM NhatKyThayDoiSanPham";

        try {
            statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ProductHistoryLog history = new ProductHistoryLog();
                NhanVien nhanVien = new NhanVien_BUS().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
                SanPham sanPham = new SanPham_BUS().getSanPhamByMaSanPham(rs.getString("maSanPham"));

                history.setSanPham(sanPham);
                history.setNhanVien(nhanVien);

                productHistoryList.add(history);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return productHistoryList;
    }

}
