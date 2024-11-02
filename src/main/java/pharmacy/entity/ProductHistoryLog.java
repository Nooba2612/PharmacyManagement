package pharmacy.entity;

import java.time.LocalDate;

public class ProductHistoryLog {
    private SanPham sanPham;
    private NhanVien nhanVien;

    public ProductHistoryLog() {
    }

    public ProductHistoryLog(SanPham sanPham, NhanVien nhanVien) {
        this.sanPham = sanPham;
        this.nhanVien = nhanVien;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getMaSanPham() {
        return sanPham != null ? sanPham.getMaSanPham() : null;
    }

    public String getTenSanPham() {
        return sanPham != null ? sanPham.getTenSanPham() : null;
    }

    public LocalDate getNgaySX() {
        return sanPham != null ? sanPham.getNgaySX() : null;
    }

    public String getMoTa() {
        return sanPham != null ? sanPham.getMoTa() : null;
    }

    public double getDonGiaBan() {
        return sanPham != null ? sanPham.getDonGiaBan() : 0;
    }

    public double getThue() {
        return sanPham != null ? sanPham.getThue() : 0;
    }

    public int getSoLuongTon() {
        return sanPham != null ? sanPham.getSoLuongTon() : 0;
    }

    public String getNhaSX() {
        return sanPham != null ? sanPham.getNhaSX() : null;
    }

    public LocalDate getHanSuDung() {
        return sanPham != null ? sanPham.getHanSuDung() : null;
    }

    public String getDonViTinh() {
        return sanPham != null ? sanPham.getDonViTinh() : null;
    }

    public String getLoaiSanPham() {
        return sanPham != null ? sanPham.getLoaiSanPham() : null;
    }

    public String getDanhMuc() {
        return sanPham != null ? sanPham.getDanhMuc() : null;
    }

    public LocalDate getNgayCapNhat() {
        return sanPham != null ? sanPham.getNgayCapNhat() : null;
    }

    public String getHoTen() {
        return nhanVien != null ? nhanVien.getHoTen() : null;
    }

}
