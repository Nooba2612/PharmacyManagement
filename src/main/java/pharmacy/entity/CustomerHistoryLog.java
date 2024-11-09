package pharmacy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerHistoryLog {

    private KhachHang khachHang;
    private NhanVien nhanVien;
    private LocalDateTime ngayCapNhat;

    public CustomerHistoryLog() {
    }

    public CustomerHistoryLog(KhachHang khachHang, NhanVien nhanVien, LocalDateTime ngayCapNhat) {
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.ngayCapNhat = ngayCapNhat;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getMaKhachHang() {
        return khachHang != null ? khachHang.getMaKhachHang() : null;
    }

    public String getHoTenKH() {
        return khachHang != null ? khachHang.getHoTen() : null;
    }

    public String getSoDienThoai() {
        return khachHang != null ? khachHang.getSoDienThoai() : null;
    }

    public int getDiemTichLuy() {
        return khachHang != null ? khachHang.getDiemTichLuy() : 0;
    }

    public LocalDate getNamSinh() {
        return khachHang != null ? khachHang.getNamSinh() : null;
    }

    public String getGhiChu() {
        return khachHang != null ? khachHang.getGhiChu() : null;
    }

    public String getGioiTinh() {
        return khachHang != null ? khachHang.getGioiTinh() : null;
    }

    public String getHoTen() {
        return nhanVien != null ? nhanVien.getHoTen() : null;
    }
}
