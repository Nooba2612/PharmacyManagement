package pharmacy.entity;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ChiTietPhieuNhap {
	private SanPham sanpham;
	private PhieuNhap phieuNhap;
	private int soLuong;
	private double donGia;
	private float thue;
	private LocalDate ngaySX;
	private LocalDate hanSuDung;
	private String ghiChu;

	public ChiTietPhieuNhap() {
	}

	public ChiTietPhieuNhap(SanPham sanpham, PhieuNhap phieuNhap, int soLuong, double donGia,
			float thue, LocalDate ngaySX, LocalDate hanSuDung) {
		setSanPham(sanpham);
		setPhieuNhap(phieuNhap);
		setSoLuong(soLuong);
		setDonGia(donGia);
		setThue(thue);
		setNgaySX(ngaySX);
		setHanSuDung(hanSuDung);
	}

	public ChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
		this(chiTietPhieuNhap.getSanPham(), chiTietPhieuNhap.getPhieuNhap(),
				chiTietPhieuNhap.getSoLuong(), chiTietPhieuNhap.getDonGia(), chiTietPhieuNhap.getThue(), chiTietPhieuNhap.getNgaySX(),
				chiTietPhieuNhap.getHanSuDung());
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

	public LocalDate getNgaySX() {
		return ngaySX;
	}

	public void setNgaySX(LocalDate ngaySX) {
		if (ngaySX == null || ngaySX.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Ngày sản xuất không thể trong tương lai.");
		}
		this.ngaySX = ngaySX;
	}

	public LocalDate getHanSuDung() {
		return hanSuDung;
	}

	public void setHanSuDung(LocalDate hanSuDung) {
		if (hanSuDung == null) {
			throw new IllegalArgumentException("Hạn sử dụng không được rỗng.");
		}
		this.hanSuDung = hanSuDung;
	}

	public String getMaSanPham() {
        return sanpham.getMaSanPham();
    }

    // Getter cho tên sản phẩm
    public String getTenSanPham() {
        return sanpham.getTenSanPham();
    }

	//  public LocalDate getNgaySX() {
    //     return sanpham.getNgaySX();
    // }

	// public LocalDate getHanSuDung() {
    //     return sanpham.getHanSuDung();
    // }

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

	public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

	@Override
	public String toString() {
		return "ChiTietPhieuNhap{" + ", sanpham=" + sanpham + ", phieuNhap=" + phieuNhap
				+ ", soLuong=" + soLuong + ", donGia=" + donGia + ", thue=" + thue + ", ngaySX=" + ngaySX + ", hanSuDung=" + hanSuDung + '}';
	}
}