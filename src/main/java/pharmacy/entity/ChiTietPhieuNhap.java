package pharmacy.entity;

public class ChiTietPhieuNhap {
	private SanPham sanpham;
	private PhieuNhap phieuNhap;
	private int soLuong;
	private double donGia;
	private double thue;

	public ChiTietPhieuNhap() {
	}

	public ChiTietPhieuNhap(SanPham sanpham, PhieuNhap phieuNhap, int soLuong, double donGia,
			double thue) {
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
		if (phieuNhap == null) {
			throw new IllegalArgumentException("Phiếu nhập không hợp lệ");
		}
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
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		if (donGia <= 0) {
			throw new IllegalArgumentException("Đơn giá không hợp lệ");
		}
		this.donGia = donGia;
	}

	public double getThue() {
		return thue;
	}

	public void setThue(double thue) {
		if (thue < 0) {
			throw new IllegalArgumentException("Thuế không hợp lệ");
		}
		this.thue = thue;
	}

	@Override
	public String toString() {
		return "ChiTietPhieuNhap{" + ", sanpham=" + sanpham + ", phieuNhap=" + phieuNhap
				+ ", soLuong=" + soLuong + ", donGia=" + donGia + ", thue=" + thue + '}';
	}
}
