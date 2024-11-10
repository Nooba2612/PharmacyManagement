package pharmacy.entity;

import java.sql.SQLException;
import java.time.LocalDate;

import pharmacy.bus.SanPham_BUS;

public class ChiTietHoaDon {

    private String maHoaDon;
    private String maSanPham;
    private int soLuong;
    private double donGiaNhap;
    private Float thue;
    private String lieuLuong;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(String maHoaDon, String maSanPham, int soLuong, Float thue, String lieuLuong, double donGiaNhap) {
        this.maHoaDon = maHoaDon;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.thue = thue;
        this.lieuLuong = lieuLuong;
        this.donGiaNhap = donGiaNhap;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(double donGiaNhap) {
        this.donGiaNhap = donGiaNhap;
    }

    public String getLieuLuong() {
        return lieuLuong;
    }

    public void setLieuLuong(String lieuLuong) {
        this.lieuLuong = lieuLuong;
    }

    public Float getThue() {
        return thue;
    }

    public void setThue(Float thue) {
        this.thue = thue;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public SanPham getSanPham() throws SQLException {
        return new SanPham_BUS().getSanPhamByMaSanPham(maSanPham);
    }

    public String getTenSanPham() throws SQLException {
        return new SanPham_BUS().getSanPhamByMaSanPham(maSanPham).getTenSanPham();
    }

    public String getDonViTinh() throws SQLException {
        return new SanPham_BUS().getSanPhamByMaSanPham(maSanPham).getDonViTinh();
    }

    public Double getDonGiaBan() throws SQLException {
        return new SanPham_BUS().getSanPhamByMaSanPham(maSanPham).getDonGiaBan();
    }

    public LocalDate getHanSuDung() throws SQLException {
        return new SanPham_BUS().getSanPhamByMaSanPham(maSanPham).getHanSuDung();
    }

    public double getTongTien() throws SQLException {
        return getDonGiaBan() * soLuong;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" + ", maSanPham=" + maSanPham + ", soLuong=" + soLuong + '}' + ", maHoaDon=" + maHoaDon
                + '}';
    }
}
