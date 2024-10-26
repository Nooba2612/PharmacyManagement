package pharmacy.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.HoaDon_Interface;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.ChiTietHoaDon;
import pharmacy.entity.HoaDon;
import pharmacy.entity.KhachHang;
import pharmacy.entity.NhanVien;
import pharmacy.entity.SanPham;

public class HoaDon_DAO implements HoaDon_Interface {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet rs;
    private String query;

    public HoaDon_DAO() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean createHoaDon(HoaDon hoaDon) {
        query = "INSERT INTO HoaDon (maHoaDon, maKhachHang, maNhanVien, ngayTao, tienKhachDua, diemSuDung, loaiThanhToan) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, hoaDon.getMaHoaDon());
            statement.setString(2, hoaDon.getKhachHang().getMaKhachHang());
            statement.setString(3, hoaDon.getNhanVien().getMaNhanVien());
            statement.setDate(4, Date.valueOf(hoaDon.getNgayTao()));
            statement.setDouble(5, hoaDon.getTienKhachDua());
            statement.setDouble(6, hoaDon.getDiemSuDung());
            statement.setString(7, hoaDon.getLoaiThanhToan());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public HoaDon getHoaDonById(String maHoaDon) {
        query = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, maHoaDon);
            rs = statement.executeQuery();

            if (rs.next()) {
                KhachHang khachHang = new KhachHang_DAO().getKhachHangById(rs.getString("maKhachHang"));
                NhanVien nhanVien = new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
                SanPham thuoc = new SanPham_DAO().getSanPhamByMaSanPham(rs.getString("thuoc"));
                LocalDate ngayTao = rs.getDate("ngayTao").toLocalDate();
                double tienKhachDua = rs.getDouble("tienKhachDua");
                double diemSuDung = rs.getDouble("diemSuDung");
                String loaiThanhToan = rs.getString("loaiThanhToan");
                List<ChiTietHoaDon> chiTietHoaDonList = new ChiTietHoaDon_DAO().getChiTietHoaDonByMaHoaDon(maHoaDon);

                return new HoaDon(maHoaDon, khachHang, nhanVien, ngayTao, tienKhachDua, diemSuDung, loaiThanhToan,
                        chiTietHoaDonList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> hoaDonList = new ArrayList<>();
        query = "SELECT * FROM HoaDon";

        try {
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();

            while (rs.next()) {
                String maHoaDon = rs.getString("maHoaDon");
                KhachHang khachHang = new KhachHang_DAO().getKhachHangById(rs.getString("maKhachHang"));
                NhanVien nhanVien = new NhanVien_DAO().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
                SanPham thuoc = new SanPham_DAO().getSanPhamByMaSanPham(rs.getString("thuoc"));
                LocalDate ngayTao = rs.getDate("ngayTao").toLocalDate();
                double tienKhachDua = rs.getDouble("tienKhachDua");
                double diemSuDung = rs.getDouble("diemSuDung");
                String loaiThanhToan = rs.getString("loaiThanhToan");
                List<ChiTietHoaDon> chiTietHoaDonList = new ChiTietHoaDon_DAO().getChiTietHoaDonByMaHoaDon(maHoaDon);

                HoaDon hoaDon = new HoaDon(maHoaDon, khachHang, nhanVien, ngayTao, tienKhachDua, diemSuDung,
                        loaiThanhToan, chiTietHoaDonList);
                hoaDonList.add(hoaDon);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hoaDonList;
    }

    @Override
    public boolean updateHoaDon(HoaDon hoaDon) {
        query = "UPDATE HoaDon SET maKhachHang = ?, maNhanVien = ?, ngayTao = ?, tienKhachDua = ?, diemSuDung = ?, loaiThanhToan = ? WHERE maHoaDon = ?";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, hoaDon.getKhachHang().getMaKhachHang());
            statement.setString(2, hoaDon.getNhanVien().getMaNhanVien());
            statement.setDate(3, Date.valueOf(hoaDon.getNgayTao()));
            statement.setDouble(4, hoaDon.getTienKhachDua());
            statement.setDouble(5, hoaDon.getDiemSuDung());
            statement.setString(6, hoaDon.getLoaiThanhToan());
            statement.setString(7, hoaDon.getMaHoaDon());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteHoaDon(String maHoaDon) {
        query = "DELETE FROM HoaDon WHERE maHoaDon = ?";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, maHoaDon);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int countHoaDon() {
        query = "SELECT COUNT(*) AS total FROM HoaDon";
        try {
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String calculateTotalRevenue() {
        query = "SELECT SUM(c.soLuong * h.tongTien) AS TotalRevenue " +
                "FROM ChiTietHoaDon c " +
                "JOIN HoaDon h ON c.maHoaDon = h.maHoaDon";
        double totalRevenue = 0;

        try {
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();

            if (rs.next()) {
                totalRevenue = rs.getDouble("TotalRevenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String revenueString = String.format("%.0f", totalRevenue);

        StringBuilder result = new StringBuilder(revenueString);

        for (int i = result.length() - 3; i > 0; i -= 3) {
            result.insert(i, '.');
        }

        return result.toString();
    }

    public double calculateRevenueByDate(String date) {
        query = "SELECT SUM(tongTien) AS RevenueByDate " +
                "FROM HoaDon " +
                "WHERE CAST(ngayTao AS DATE) = ?";
        double revenueByDate = 0;

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, date);
            rs = statement.executeQuery();

            if (rs.next()) {
                revenueByDate = rs.getDouble("RevenueByDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenueByDate;
    }

    @Override
    public double calculateRevenueByMonth(int month, int year) {
        query = "SELECT SUM(tongTien) AS RevenueByMonth " +
                "FROM HoaDon " +
                "WHERE YEAR(ngayTao) = ? AND MONTH(ngayTao) = ?";
        double revenueByMonth = 0;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, year);
            statement.setInt(2, month);
            rs = statement.executeQuery();

            if (rs.next()) {
                revenueByMonth = rs.getDouble("RevenueByMonth");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenueByMonth;
    }

    @Override
    public double calculateRevenueByYear(int year) {
        query = "SELECT SUM(tongTien) AS RevenueByYear " +
                "FROM HoaDon " +
                "WHERE YEAR(ngayTao) = ?";
        double revenueByYear = 0;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, year);
            rs = statement.executeQuery();

            if (rs.next()) {
                revenueByYear = rs.getDouble("RevenueByYear");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenueByYear;
    }

    @Override
    public double calculateRevenueByEmployee(String maNhanVien, String date) {
        double revenueByEmployee = 0;

        try {
            if (date == null || date.length() == 0) {
                query = "SELECT SUM(tongTien) AS RevenueByEmployee FROM HoaDon WHERE maNhanVien = ?";
                try {
                    statement = connection.prepareStatement(query);
                    statement.setString(1, maNhanVien);
                    ResultSet rs = statement.executeQuery();
                    if (rs.next()) {
                        revenueByEmployee = rs.getDouble("RevenueByEmployee");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String[] dateParts = date.split("[-/]");
                if (dateParts.length == 3) { // date
                    query = "SELECT SUM(tongTien) AS RevenueByEmployee FROM HoaDon WHERE maNhanVien = ? AND CAST(ngayTao AS DATE) = ?";
                    try {
                        statement = connection.prepareStatement(query);
                        statement.setString(1, maNhanVien);
                        statement.setString(2, date);
                        ResultSet rs = statement.executeQuery();
                        if (rs.next()) {
                            revenueByEmployee = rs.getDouble("RevenueByEmployee");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (dateParts.length == 2) { // month
                    query = "SELECT SUM(tongTien) AS RevenueByEmployee FROM HoaDon WHERE maNhanVien = ? AND MONTH(ngayTao) = ?";
                    try {
                        statement = connection.prepareStatement(query);
                        statement.setString(1, maNhanVien);
                        statement.setInt(2, Integer.parseInt(dateParts[1]));
                        ResultSet rs = statement.executeQuery();
                        if (rs.next()) {
                            revenueByEmployee = rs.getDouble("RevenueByEmployee");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (dateParts.length == 1) { // year
                    query = "SELECT SUM(tongTien) AS RevenueByEmployee FROM HoaDon WHERE maNhanVien = ? AND YEAR(ngayTao) = ?";
                    try {
                        statement = connection.prepareStatement(query);
                        statement.setString(1, maNhanVien);
                        statement.setInt(2, Integer.parseInt(dateParts[0]));
                        ResultSet rs = statement.executeQuery();
                        if (rs.next()) {
                            revenueByEmployee = rs.getDouble("RevenueByEmployee");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {

        }

        return revenueByEmployee;
    }
}
