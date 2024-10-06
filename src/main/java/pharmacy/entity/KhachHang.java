package pharmacy.entity;

import java.time.LocalDate;

public class KhachHang {
    private String maKhachHang;
    private String hoTen;
    private String soDienThoai;
    private int diemTichLuy;
    private LocalDate namSinh;
    private String ghiChu;

    public KhachHang() { }

    public KhachHang(String maKhachHang, String hoTen, String soDienThoai, int diemTichLuy, LocalDate namSinh, String ghiChu) {
        this.setMaKhachHang(maKhachHang);
        this.setHoTen(hoTen);
        this.setSoDienThoai(soDienThoai);
        this.diemTichLuy = diemTichLuy;
        this.setNamSinh(namSinh);
        this.ghiChu = ghiChu;
    }

    public KhachHang(KhachHang original) {
        this.maKhachHang = original.maKhachHang;
        this.hoTen = original.hoTen;
        this.soDienThoai = original.soDienThoai;
        this.diemTichLuy = original.diemTichLuy;
        this.namSinh = original.namSinh;
        this.ghiChu = original.ghiChu;
    }

    public String getMaKhachHang() {
        if (maKhachHang == null || maKhachHang.isEmpty()) {
            throw new IllegalArgumentException("maKhachHang không được rỗng");
        }
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        if (maKhachHang == null || maKhachHang.isEmpty()) {
            throw new IllegalArgumentException("maKhachHang không được rỗng");
        }
        this.maKhachHang = maKhachHang;
    }

    public String getHoTen() {
        if (hoTen == null || hoTen.isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được rỗng");
        }
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được rỗng");
        }
        if (!Character.isUpperCase(hoTen.charAt(0))) {
            throw new IllegalArgumentException("Họ tên phải viết hoa chữ cái đầu");
        }
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.length() != 10) {
            throw new IllegalArgumentException("Số điện thoại phải đủ 10 ký tự số");
        }
        this.soDienThoai = soDienThoai;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public LocalDate getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(LocalDate namSinh) {
        if (namSinh == null) {
            throw new IllegalArgumentException("Ngày sinh không được rỗng");
        }
        this.namSinh = namSinh;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKhachHang='" + maKhachHang + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", diemTichLuy=" + diemTichLuy +
                ", namSinh=" + namSinh +
                '}';
    }
}
