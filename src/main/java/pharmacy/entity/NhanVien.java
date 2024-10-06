package pharmacy.entity;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private String chucVu;
    private String soDienThoai;
    private LocalDate ngayVaoLam;
    private String trangThai;
    private String trinhDo;
    private String gioiTinh;
    private LocalDate namSinh;

    public NhanVien() {}

    public NhanVien(String maNhanVien, String hoTen, String chucVu, String soDienThoai, 
                    LocalDate ngayVaoLam, String trangThai, String trinhDo, 
                    String gioiTinh, LocalDate namSinh) {
        this.setMaNhanVien(maNhanVien);
        this.setHoTen(hoTen);
        this.setChucVu(chucVu);
        this.setSoDienThoai(soDienThoai);
        this.ngayVaoLam = ngayVaoLam;
        this.trangThai = trangThai;
        this.trinhDo = trinhDo;
        this.gioiTinh = gioiTinh;
        this.namSinh = namSinh;
    }

    public NhanVien(NhanVien nv) {
        this(nv.getMaNhanVien(), nv.getHoTen(), nv.getChucVu(), 
             nv.getSoDienThoai(), nv.getNgayVaoLam(), nv.getTrangThai(), 
             nv.getTrinhDo(), nv.getGioiTinh(), nv.getNamSinh());
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
        if (hoTen == null || !hoTen.matches("^[A-Z].*")) {
            throw new IllegalArgumentException("Họ tên phải viết hoa chữ cái đầu");
        }
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
        if (soDienThoai == null || !soDienThoai.matches("\\d{10}")) {
            throw new IllegalArgumentException("Số điện thoại phải đủ 10 ký tự số");
        }
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.matches("\\d{10}")) {
            throw new IllegalArgumentException("Số điện thoại phải đủ 10 ký tự số");
        }
        this.soDienThoai = soDienThoai;
    }

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(String ngayVaoLam) {
        try {
            this.ngayVaoLam = LocalDate.parse(ngayVaoLam, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Ngày vào làm không hợp lệ");
        }
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
        if (!trinhDo.equals("Cao đẳng") && !trinhDo.equals("Đại học") && !trinhDo.equals("Cao học")) {
            throw new IllegalArgumentException("Trình độ không hợp lệ");
        }
        this.trinhDo = trinhDo;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        if (!gioiTinh.equals("Nam") && !gioiTinh.equals("Nữ") && !gioiTinh.equals("Khác")) {
            throw new IllegalArgumentException("Giới tính không hợp lệ");
        }
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(LocalDate namSinh) {
        if (namSinh == null) {
            throw new IllegalArgumentException("Định dạng dd/MM/yyyy");
        }
        this.namSinh = namSinh;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", chucVu='" + chucVu + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", ngayVaoLam=" + ngayVaoLam +
                ", trangThai='" + trangThai + '\'' +
                ", trinhDo='" + trinhDo + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", namSinh=" + namSinh +
                '}';
    }
}
