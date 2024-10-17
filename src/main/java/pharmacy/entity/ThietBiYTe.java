package pharmacy.entity;

import java.time.LocalDate;

public class ThietBiYTe {

    private String maThietBi;
    private String tenThietBi;
    private LocalDate ngaySX;
    private String moTa;
    private int soLuongTon;
    private double donGiaBan;
    private DanhMuc danhMuc;

    public ThietBiYTe() {
    }

    public ThietBiYTe(String maThietBi, String tenThietBi, LocalDate ngaySX, String moTa, int soLuongTon,
            double donGiaBan, DanhMuc danhMuc) {
        setMaThietBi(maThietBi);
        setTenThietBi(tenThietBi);
        setNgaySX(ngaySX);
        setMoTa(moTa);
        setSoLuongTon(soLuongTon);
        setDonGiaBan(donGiaBan);
        setDanhMuc(danhMuc);
    }

    public ThietBiYTe(ThietBiYTe other) {
        this.maThietBi = other.maThietBi;
        this.tenThietBi = other.tenThietBi;
        this.ngaySX = other.ngaySX;
        this.moTa = other.moTa;
        this.soLuongTon = other.soLuongTon;
        this.donGiaBan = other.donGiaBan;
        this.danhMuc = other.danhMuc;
    }

    public String getMaThietBi() {
        return maThietBi;
    }

    public void setMaThietBi(String maThietBi) {
        if (maThietBi == null || maThietBi.isEmpty()) {
            throw new IllegalArgumentException("Mã thiết bị không hợp lệ");
        }
        this.maThietBi = maThietBi;
    }

    public String getTenThietBi() {
        return tenThietBi;
    }

    public void setTenThietBi(String tenThietBi) {
        if (tenThietBi == null || tenThietBi.isEmpty()) {
            throw new IllegalArgumentException("Tên thiết bị không hợp lệ");
        }
        this.tenThietBi = tenThietBi;
    }

    public LocalDate getNgaySX() {
        return ngaySX;
    }

    public void setNgaySX(LocalDate ngaySX) {
        if (ngaySX == null) {
            throw new IllegalArgumentException("Ngày sản xuất không hợp lệ");
        }
        this.ngaySX = ngaySX;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        if (soLuongTon < 0) {
            throw new IllegalArgumentException("Số lượng không hợp lệ");
        }
        this.soLuongTon = soLuongTon;
    }

    public double getDonGiaBan() {
        return donGiaBan;
    }

    public void setDonGiaBan(double donGiaBan) {
        if (donGiaBan < 0) {
            throw new IllegalArgumentException("Đơn giá bán không hợp lệ");
        }
        this.donGiaBan = donGiaBan;
    }

    public DanhMuc getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(DanhMuc danhMuc) {
        this.danhMuc = danhMuc; // Giá trị danh mục từ Danh Mục
    }

    @Override
    public String toString() {
        return "Thiết bị y tế [Mã: " + maThietBi +
                ", Tên: " + tenThietBi +
                ", Ngày SX: " + ngaySX +
                ", Mô tả: " + moTa +
                ", Số lượng: " + soLuongTon +
                ", Đơn giá bán: " + donGiaBan + // Cập nhật hiển thị đơn giá bán
                ", Danh mục: " + (danhMuc != null ? danhMuc.toString() : "Chưa có") + "]";
    }
}
