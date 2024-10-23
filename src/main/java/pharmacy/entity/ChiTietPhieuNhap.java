package pharmacy.entity;

public class ChiTietPhieuNhap {
	private ThietBiYTe thietBi;
	private Thuoc thuoc;
	private PhieuNhap phieuNhap;
	private int soLuong;
	private double donGia;
	private double thue;

	public ChiTietPhieuNhap() {
	}

	public ChiTietPhieuNhap(Thuoc thuoc, PhieuNhap phieuNhap, ThietBiYTe thietBi, int soLuong, double donGia,
			double thue) {
		setThuoc(thuoc);
		setPhieuNhap(phieuNhap);
		setThietBiYTe(thietBi);
		setSoLuong(soLuong);
		setDonGia(donGia);
		setThue(thue);
	}

	public ChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
		this(chiTietPhieuNhap.getThuoc(), chiTietPhieuNhap.getPhieuNhap(), chiTietPhieuNhap.getThietBiYTe(),
				chiTietPhieuNhap.getSoLuong(), chiTietPhieuNhap.getDonGia(), chiTietPhieuNhap.getThue());
	}

	public Thuoc getThuoc() {
		return thuoc;
	}

	public void setThuoc(Thuoc thuoc) {
		if (thuoc == null) {
			throw new IllegalArgumentException("Thuốc không hợp lệ");
		}
		this.thuoc = thuoc;
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

	public ThietBiYTe getThietBiYTe() {
		return thietBi;
	}

	public void setThietBiYTe(ThietBiYTe thietBi) {
		if (thietBi == null) {
			throw new IllegalArgumentException("Thiết bị y tế không hợp lệ");
		}
		this.thietBi = thietBi;
	}

	@Override
	public String toString() {
		return "ChiTietPhieuNhap{" + "thietBi=" + thietBi + ", thuoc=" + thuoc + ", phieuNhap=" + phieuNhap
				+ ", soLuong=" + soLuong + ", donGia=" + donGia + ", thue=" + thue + '}';
	}
}
