package pharmacy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.HistoryLogCustomer_Interface;
import pharmacy.bus.KhachHang_BUS;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.CustomerHistoryLog;
import pharmacy.entity.KhachHang;
import pharmacy.entity.NhanVien;



public class HistoryLogCustomer_DAO implements HistoryLogCustomer_Interface {
	private Connection connection;
    private PreparedStatement statement;
    private String query;

    public HistoryLogCustomer_DAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void addCustomerHistory(CustomerHistoryLog history) {
    	query = "INSERT INTO KhachHang (maKhachHang, hoTen, soDienThoai, namSinh, diemTichLuy, ghiChu) VALUES (?, ?, ?, ?, ?, ?)";
		try {
				
			statement = connection.prepareStatement(query);
			statement.setString(1, history.getMaKhachHang());
			statement.setString(2, history.getHoTen());
			statement.setString(3, history.getSoDienThoai());
			statement.setInt(4, history.getNamSinh().getYear());
			statement.setInt(5, history.getDiemTichLuy());
			statement.setString(6, history.getGioiTinh());
			statement.setString(7, history.getGhiChu());

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
    public List<CustomerHistoryLog> getAllCusHistory() {
        List<CustomerHistoryLog> customerHistoryList = new ArrayList<>();
        query = "SELECT * FROM NhatKyThayDoiKhachHang";

        try {
            statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                CustomerHistoryLog history = new CustomerHistoryLog();
                NhanVien nhanVien = new NhanVien_BUS().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
                KhachHang khachHang = new KhachHang_BUS().getKhachHangById(rs.getString("maKhachHang"));

                history.setKhachHang(khachHang);
                history.setNhanVien(nhanVien);

                customerHistoryList.add(history);
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

        return customerHistoryList;
    }

}
