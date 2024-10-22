package pharmacy.entity;

public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private String loaiSanPham;
    private int soLuongDaBan;
    private double donGiaBan;

    public SanPham() {
    }

    public SanPham(String maSanPham, String tenSanPham, String loaiSanPham, int soLuongDaBan, double donGiaBan) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.loaiSanPham = loaiSanPham;
        this.soLuongDaBan = soLuongDaBan;
        this.donGiaBan = donGiaBan;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(String loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public int getSoLuongDaBan() {
        return soLuongDaBan;
    }

    public void setSoLuongDaBan(int soLuongDaBan) {
        this.soLuongDaBan = soLuongDaBan;
    }

    public double getDonGiaBan() {
        return donGiaBan;
    }

    public void setDonGiaBan(double donGiaBan) {
        this.donGiaBan = donGiaBan;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "maSanPham='" + maSanPham + '\'' +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", loaiSanPham='" + loaiSanPham + '\'' +
                ", soLuongDaBan=" + soLuongDaBan +
                ", donGiaBan=" + donGiaBan +
                '}';
    }
}
