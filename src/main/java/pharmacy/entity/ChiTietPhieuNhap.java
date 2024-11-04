package pharmacy.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ChiTietPhieuNhap {
	private SanPham sanpham;
	private PhieuNhap phieuNhap;
	private int soLuong;
	private double donGia;
	private float thue;

	public ChiTietPhieuNhap() {
	}

	public ChiTietPhieuNhap(SanPham sanpham, PhieuNhap phieuNhap, int soLuong, double donGia,
			float thue) {
		setSanPham(sanpham);
		setPhieuNhap(phieuNhap);
		setSoLuong(soLuong);
		setDonGia(donGia);
		setThue(thue);
	}

	public ChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
		this(chiTietPhieuNhap.getSanPham(), chiTietPhieuNhap.getPhieuNhap(),
				chiTietPhieuNhap.getSoLuong(), chiTietPhieuNhap.getDonGia(), chiTietPhieuNhap.getThue());
	}

	public SanPham getSanPham() {
		return sanpham;
	}

	public void setSanPham(SanPham sanpham) {
		if (sanpham == null) {
			throw new IllegalArgumentException("Thuốc không hợp lệ");
		}
		this.sanpham = sanpham;
	}

	public PhieuNhap getPhieuNhap() {
		return phieuNhap;
	}

	public void setPhieuNhap(PhieuNhap phieuNhap) {
		// if (phieuNhap == null) {
		// 	throw new IllegalArgumentException("Phiếu nhập không hợp lệ");
		// }
		this.phieuNhap = phieuNhap;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		if (soLuong <= 0) {
			throw new IllegalArgumentException("Số lượng không hợp lệ");
		}
		this.soLuong = soLuong;
		updateThanhTien();
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		if (donGia <= 0) {
			throw new IllegalArgumentException("Đơn giá không hợp lệ");
		}
		this.donGia = donGia;
		updateThanhTien();
	}

	public float getThue() {
		return thue;
	}

	public void setThue(float thue) {
		if (thue < 0) {
			throw new IllegalArgumentException("Thuế không hợp lệ");
		}
		this.thue = thue;
		updateThanhTien();
	}

	public String getMaSanPham() {
        return sanpham.getMaSanPham();
    }

    // Getter cho tên sản phẩm
    public String getTenSanPham() {
        return sanpham.getTenSanPham();
    }
	// Thêm property cho thành tiền
	private final DoubleProperty thanhTien = new SimpleDoubleProperty();

	public double calculateThanhTien() {
		return (soLuong * donGia) + (soLuong * donGia * thue);
	}

	public void updateThanhTien() {
		thanhTien.set(calculateThanhTien());
	}

	// Getter cho thành tiền
	public DoubleProperty thanhTienProperty() {
		return thanhTien;
	}

	public double getThanhTien() {
		return thanhTien.get();
	}

	@Override
	public String toString() {
		return "ChiTietPhieuNhap{" + ", sanpham=" + sanpham + ", phieuNhap=" + phieuNhap
				+ ", soLuong=" + soLuong + ", donGia=" + donGia + ", thue=" + thue + '}';
	}
}