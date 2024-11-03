package pharmacy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeeHistoryLog {

    private NhanVien nhanVien;
    private LocalDateTime ngayCapNhat;
    private NhanVien nguoiSua;

    public EmployeeHistoryLog() {
    }

    public EmployeeHistoryLog(NhanVien nhanVien, LocalDateTime ngayCapNhat, NhanVien nguoiSua) {
        this.nhanVien = nhanVien;
        this.ngayCapNhat = ngayCapNhat;
        this.nguoiSua = nguoiSua;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public NhanVien getNguoiSua() {
        return nguoiSua;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public void setNguoiSua(NhanVien nguoiSua) {
        this.nguoiSua = nguoiSua;
    }

    public String getMaNhanVien() {
        return nhanVien.getMaNhanVien();
    }

    public String getHoTen() {
        return nhanVien.getHoTen();
    }

    public String getSoDienThoai() {
        return nhanVien.getSoDienThoai();
    }

    public String getEmail() {
        return nhanVien.getEmail();
    }

    public String getChucVu() {
        return nhanVien.getChucVu();
    }

    public String getGioiTinh() {
        return nhanVien.getGioiTinh();
    }

    public LocalDate getNgayVaoLam() {
        return nhanVien.getNgayVaoLam();
    }

    public LocalDate getNamSinh() {
        return nhanVien.getNamSinh();
    }

    public String getTrinhDo() {
        return nhanVien.getTrinhDo();
    }

    public double getTienLuong() {
        return nhanVien.getTienLuong();
    }

    public String getCccd() {
        return nhanVien.getCccd();
    }

    public String getTenNguoiSua() {
        return (nguoiSua != null) ? nguoiSua.getHoTen() : null;
    }
}
