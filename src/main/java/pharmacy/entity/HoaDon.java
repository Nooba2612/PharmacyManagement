package pharmacy.entity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import pharmacy.bus.ChiTietHoaDon_BUS;
import pharmacy.bus.SanPham_BUS;

public class HoaDon {

    private String maHoaDon;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private LocalDateTime ngayTao;
    private double tienKhachDua;
    private double diemSuDung;
    private String loaiThanhToan;
    private List<ChiTietHoaDon> chiTietHoaDonList;
    private Double tienThua;
    private Double tongTien;

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, KhachHang khachHang, NhanVien nhanVien, LocalDateTime ngayTao, double tienKhachDua,
            double diemSuDung, String loaiThanhToan, double tongTien) throws SQLException {
        this.maHoaDon = maHoaDon;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.ngayTao = ngayTao;
        this.tienKhachDua = tienKhachDua;
        this.diemSuDung = diemSuDung;
        this.loaiThanhToan = loaiThanhToan;
        this.tongTien = tongTien;

        if (tienKhachDua - tongTien >= 0) {
            this.tienThua = tienKhachDua - tongTien;
        } else {
            this.tienThua = 0.0;
        }
    }

    public Double getTienThua() {
        return tienThua;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public double getTienKhachDua() {
        return tienKhachDua;
    }

    public void setTienKhachDua(double tienKhachDua) {
        this.tienKhachDua = tienKhachDua;
    }

    public double getDiemSuDung() {
        return diemSuDung;
    }

    public void setDiemSuDung(double diemSuDung) {
        this.diemSuDung = diemSuDung;
    }

    public String getLoaiThanhToan() {
        return loaiThanhToan;
    }

    public void setLoaiThanhToan(String loaiThanhToan) {
        this.loaiThanhToan = loaiThanhToan;
    }

    public List<ChiTietHoaDon> getChiTietHoaDonList() {
        return new ChiTietHoaDon_BUS().getChiTietHoaDonByMaHoaDon(maHoaDon);
    }

    public void setChiTietHoaDonList(List<ChiTietHoaDon> chiTietHoaDonList) {
        this.chiTietHoaDonList = chiTietHoaDonList;
    }

    @Override
    public String toString() {
        return "HoaDon{" + "maHoaDon='" + maHoaDon + '\'' + ", khachHang=" + khachHang + ", nhanVien=" + nhanVien
                + ", ngayTao=" + ngayTao + ", tienKhachDua=" + tienKhachDua + ", diemSuDung=" + diemSuDung
                + ", loaiThanhToan='" + loaiThanhToan + '\'' + ", chiTietHoaDonList=" + chiTietHoaDonList + '}';
    }
}
