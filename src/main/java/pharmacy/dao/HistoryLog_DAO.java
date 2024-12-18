package pharmacy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pharmacy.Interface.HistoryLog_Interface;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.CustomerHistoryLog;
import pharmacy.entity.EmployeeHistoryLog;
import pharmacy.entity.KhachHang;
import pharmacy.entity.NhaCungCap;
import pharmacy.entity.NhanVien;
import pharmacy.entity.ProductHistoryLog;
import pharmacy.entity.SanPham;
import pharmacy.entity.SupplierHistoryLog;

public class HistoryLog_DAO implements HistoryLog_Interface {

    private Connection connection;
    private PreparedStatement statement;
    private String query;

    public HistoryLog_DAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void addEmployeeHistory(EmployeeHistoryLog history) {
        query = "INSERT INTO NhatKyThayDoiNhanVien (maNhanVien, hoTen, chucVu, soDienThoai, email, ngayVaoLam, namSinh, trangThai, trinhDo, gioiTinh, cccd, tienLuong, ngayCapNhat, maNhanVienDaSua) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, history.getNhanVien().getMaNhanVien());
            statement.setString(2, history.getNhanVien().getHoTen());
            statement.setString(3, history.getNhanVien().getChucVu());
            statement.setString(4, history.getNhanVien().getSoDienThoai());
            statement.setString(5, history.getNhanVien().getEmail());
            statement.setTimestamp(6, Timestamp.valueOf(history.getNhanVien().getNgayVaoLam().atStartOfDay()));
            statement.setDate(7, java.sql.Date.valueOf(history.getNhanVien().getNamSinh()));
            statement.setString(8, history.getNhanVien().getTrangThai());
            statement.setString(9, history.getNhanVien().getTrinhDo());
            statement.setString(10, history.getNhanVien().getGioiTinh());
            statement.setString(11, history.getNhanVien().getCccd());
            statement.setDouble(12, history.getNhanVien().getTienLuong());
            statement.setTimestamp(13, Timestamp.valueOf(history.getNgayCapNhat()));
            statement.setString(14, history.getNguoiSua().getMaNhanVien());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<EmployeeHistoryLog> getEmployeeHistoryById(String employeeId) {
        List<EmployeeHistoryLog> historyList = new ArrayList<>();
        query = "SELECT * FROM NhatKyThayDoiNhanVien WHERE maNhanVien = ?";

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, employeeId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    EmployeeHistoryLog history = new EmployeeHistoryLog();

                    history.setNhanVien(new NhanVien_BUS().getEmployeeByMaNhanVien(rs.getString("maNhanVien")));
                    history.setNgayCapNhat(rs.getTimestamp("ngayCapNhat").toLocalDateTime());
                    history.setNguoiSua(new NhanVien_BUS().getEmployeeByMaNhanVien(rs.getString("maNhanVienDaSua")));

                    historyList.add(history);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }

    @Override
    public void addProductHistory(ProductHistoryLog history) {
        query = "INSERT INTO NhatKyThayDoiSanPham (maSanPham, tenSanPham, danhMuc, loaiSanPham, ngaySX, nhaSX, ngayTao, ngayCapNhat, soLuongTon, donGiaBan, thue, hanSuDung, donViTinh, moTa, trangThai, maNhanVien) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, history.getSanPham().getMaSanPham());
            statement.setString(2, history.getSanPham().getTenSanPham());
            statement.setString(3, history.getSanPham().getDanhMuc());
            statement.setString(4, history.getSanPham().getLoaiSanPham());
            statement.setDate(5, java.sql.Date.valueOf(history.getSanPham().getNgaySX()));
            statement.setString(6, history.getSanPham().getNhaSX());
            statement.setDate(7, java.sql.Date.valueOf(history.getSanPham().getNgayTao()));
            statement.setTimestamp(8, Timestamp.valueOf(history.getSanPham().getNgayCapNhat()));
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
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<ProductHistoryLog> getProductHistoryById(String productId) {
        List<ProductHistoryLog> productHistoryList = new ArrayList<>();
        query = "SELECT * FROM NhatKyThayDoiSanPham WHERE maSanPham = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, productId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ProductHistoryLog history = new ProductHistoryLog();
                    NhanVien nhanVien = new NhanVien_BUS().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
                    SanPham sanPham = new SanPham(
                            rs.getString("maSanPham"),
                            rs.getString("tenSanPham"),
                            rs.getString("danhMuc"),
                            rs.getDate("ngaySX") != null ? rs.getDate("ngaySX").toLocalDate() : null,
                            rs.getString("nhaSX"),
                            rs.getDate("ngayTao") != null ? rs.getDate("ngayTao").toLocalDate() : null,
                            rs.getTimestamp("ngayCapNhat") != null ? rs.getTimestamp("ngayCapNhat").toLocalDateTime()
                            : null,
                            rs.getInt("soLuongTon"),
                            rs.getDouble("donGiaBan"),
                            rs.getFloat("thue"),
                            rs.getDate("hanSuDung") != null ? rs.getDate("hanSuDung").toLocalDate() : null,
                            rs.getString("moTa"),
                            rs.getString("donViTinh"),
                            rs.getString("trangThai"),
                            rs.getString("loaiSanPham"));
                    history.setSanPham(sanPham);
                    history.setNhanVien(nhanVien);
                    productHistoryList.add(history);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productHistoryList;
    }

    @Override
    public void addCustomerHistory(CustomerHistoryLog history) {
        query = "INSERT INTO NhatKyThayDoiKhachHang (maKhachHang, hoTen, soDienThoai, namSinh, diemTichLuy, gioiTinh, ghiChu, ngayCapNhat, maNhanVien) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {

            statement = connection.prepareStatement(query);
            statement.setString(1, history.getMaKhachHang());
            statement.setString(2, history.getHoTen());
            statement.setString(3, history.getSoDienThoai());
            statement.setDate(4, java.sql.Date.valueOf(history.getNamSinh()));
            statement.setInt(5, history.getDiemTichLuy());
            statement.setString(6, history.getGioiTinh());
            statement.setString(7, history.getGhiChu());
            statement.setTimestamp(8, Timestamp.valueOf(history.getNgayCapNhat()));
            statement.setString(9, history.getNhanVien().getMaNhanVien());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public List<CustomerHistoryLog> getCustomerHistoryById(String customerId) {
        List<CustomerHistoryLog> customerHistoryList = new ArrayList<>();
        query = "SELECT * FROM NhatKyThayDoiKhachHang WHERE maKhachHang = ?";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, customerId);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                CustomerHistoryLog history = new CustomerHistoryLog();
                NhanVien nhanVien = new NhanVien_BUS().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("Hoten"),
                        rs.getString("soDienThoai"),
                        rs.getInt("diemTichLuy"),
                        rs.getDate("namSinh") != null ? rs.getDate("namSinh").toLocalDate() : null,
                        rs.getString("ghiChu"),
                        rs.getString("gioiTinh"));

                history.setKhachHang(khachHang);
                history.setNhanVien(nhanVien);
                history.setNgayCapNhat(rs.getTimestamp("ngayCapNhat").toLocalDateTime());
                customerHistoryList.add(history);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return customerHistoryList;
    }

    @Override
    public void addSupplierHistory(SupplierHistoryLog history) {
        query = "INSERT INTO NhatKyThayDoiNhaCungCap (maNCC, tenNCC, soDienThoai, diaChi, email, ngayCapNhat, maNhanVien) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, history.getNcc().getMaNCC());
            statement.setString(2, history.getNcc().getTenNCC());
            statement.setString(3, history.getNcc().getSoDienThoai());
            statement.setString(4, history.getNcc().getDiaChi());
            statement.setString(5, history.getNcc().getEmail());
            statement.setTimestamp(6, Timestamp.valueOf(history.getNgayCapNhat()));
            statement.setString(7, history.getNv().getMaNhanVien());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<SupplierHistoryLog> getSupplierHistoryById(String supplierId) {
        List<SupplierHistoryLog> supplierHistoryList = new ArrayList<>();
        query = "SELECT * FROM NhatKyThayDoiNhaCungCap WHERE maNCC = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, supplierId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    SupplierHistoryLog history = new SupplierHistoryLog();
                    NhanVien nhanVien = new NhanVien_BUS().getEmployeeByMaNhanVien(rs.getString("maNhanVien"));
                    NhaCungCap nhaCungCap = new NhaCungCap(
                            rs.getString("maNCC"),
                            rs.getString("tenNCC"),
                            rs.getString("soDienThoai"),
                            rs.getString("diaChi"),
                            rs.getString("email")
                    );
                    history.setNgayCapNhat(rs.getTimestamp("ngayCapNhat").toLocalDateTime());
                    history.setNcc(nhaCungCap);
                    history.setNv(nhanVien);
                    supplierHistoryList.add(history);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return supplierHistoryList;
    }

}
