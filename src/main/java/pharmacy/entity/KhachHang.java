package pharmacy.entity;

import java.time.LocalDate;

public class KhachHang {

    private String maKhachHang;
    private String hoTen;
    private String soDienThoai;
    private int diemTichLuy;
    private LocalDate namSinh;
    private String ghiChu;
    private String gioiTinh;

    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String hoTen, String soDienThoai, int diemTichLuy, LocalDate namSinh,
            String ghiChu, String gioiTinh) {
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.diemTichLuy = diemTichLuy;
        this.namSinh = namSinh;
        this.ghiChu = ghiChu;
        this.gioiTinh = gioiTinh;
    }

    public KhachHang(KhachHang original) {
        this.maKhachHang = original.maKhachHang;
        this.hoTen = original.hoTen;
        this.soDienThoai = original.soDienThoai;
        this.diemTichLuy = original.diemTichLuy;
        this.namSinh = original.namSinh;
        this.ghiChu = original.ghiChu;
        this.gioiTinh = original.gioiTinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getMaKhachHang() {

        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {

        this.maKhachHang = maKhachHang;
    }

    public String getHoTen() {

        return hoTen;
    }

    public void setHoTen(String hoTen) {

        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {

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
        return "KhachHang{"
                + "maKhachHang='" + maKhachHang + '\''
                + ", hoTen='" + hoTen + '\''
                + ", soDienThoai='" + soDienThoai + '\''
                + ", diemTichLuy=" + diemTichLuy
                + ", namSinh=" + namSinh
                + ", gioiTinh='" + gioiTinh + '\''
                + ", ghiChu='" + ghiChu + '\''
                + '}';

    }
}
