package pharmacy.entity;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private String email;
    private String chucVu;
    private String soDienThoai;
    private LocalDate ngayVaoLam;
    private String trangThai;
    private String trinhDo;
    private String gioiTinh;
    private LocalDate namSinh;
    private double tienLuong;
    private String cccd;

    public NhanVien() {
    }

    public NhanVien(String maNhanVien, String hoTen, String chucVu, String soDienThoai, String email,
            LocalDate ngayVaoLam, String trangThai, String trinhDo,
            String gioiTinh, LocalDate namSinh, double tienLuong, String cccd) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.chucVu = chucVu;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.ngayVaoLam = ngayVaoLam;
        this.trangThai = trangThai;
        this.trinhDo = trinhDo;
        this.gioiTinh = gioiTinh;
        this.namSinh = namSinh;
        this.tienLuong = tienLuong;
        this.cccd = cccd;
    }

    public NhanVien(NhanVien nv) {
        this(nv.getMaNhanVien(), nv.getHoTen(), nv.getChucVu(),
                nv.getSoDienThoai(), nv.getEmail(), nv.getNgayVaoLam(), nv.getTrangThai(),
                nv.getTrinhDo(), nv.getGioiTinh(), nv.getNamSinh(),
                nv.getTienLuong(), nv.getCccd());
    }

    public double getTienLuong() {
        return tienLuong;
    }

    public void setTienLuong(double tienLuong) {
        if (tienLuong < 0) {
            throw new IllegalArgumentException("Tiền lương không được nhỏ hơn 0");
        }
        this.tienLuong = tienLuong;
    }

    public String getCccd() {
        if (cccd == null || !Pattern.matches("\\d{12}", cccd)) {
            throw new IllegalArgumentException("CCCD không hợp lệ");
        }
        return cccd;
    }

    public void setCccd(String cccd) {
        if (cccd == null || !Pattern.matches("\\d{12}", cccd)) {
            throw new IllegalArgumentException("CCCD không hợp lệ");
        }
        this.cccd = cccd;
    }

    public String getMaNhanVien() {
        if (maNhanVien == null || maNhanVien.isEmpty()) {
            throw new IllegalArgumentException("maNhanVien không hợp lệ");
        }
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        if (maNhanVien == null || !Pattern.matches("MK\\d{4}", maNhanVien)) {
            throw new IllegalArgumentException("maNhanVien không hợp lệ");
        }
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        if (hoTen == null || hoTen.isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được rỗng");
        }
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        if (chucVu == null || chucVu.isEmpty()) {
            throw new IllegalArgumentException("Chức vụ không được rỗng");
        }
        this.chucVu = chucVu;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTrinhDo() {
        return trinhDo;
    }

    public void setTrinhDo(String trinhDo) {
        this.trinhDo = trinhDo;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(LocalDate namSinh) {
        this.namSinh = namSinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", chucVu='" + chucVu + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", ngayVaoLam=" + ngayVaoLam +
                ", trangThai='" + trangThai + '\'' +
                ", trinhDo='" + trinhDo + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", namSinh=" + namSinh +
                ", tienLuong=" + tienLuong +
                ", cccd='" + cccd + '\'' +
                '}';
    }
}
