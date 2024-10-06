package pharmacy.entity;

public class ChiTietHoaDon {
	private HoaDon hoaDon;
	private Thuoc thuoc;
	private ThietBiYTe thietBi;
	private int soLuong;

	public ChiTietHoaDon() {
	}

	public ChiTietHoaDon(HoaDon hoaDon, Thuoc thuoc, ThietBiYTe thietBi, int soLuong) {
		setHoaDon(hoaDon);
		setThuoc(thuoc);
		setSoLuong(soLuong);
		setThietBi(thietBi);
	}

	public ChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		if (chiTietHoaDon != null) {
			this.hoaDon = chiTietHoaDon.hoaDon;
			this.thuoc = chiTietHoaDon.thuoc;
			this.soLuong = chiTietHoaDon.soLuong;
			this.thietBi = chiTietHoaDon.thietBi;
		}
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		if (soLuong < 0) {
			throw new IllegalArgumentException("Số lượng không hợp lệ");
		}
		this.soLuong = soLuong;
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		if (hoaDon == null) {
			throw new IllegalArgumentException("Hóa đơn không hợp lệ");
		}
		this.hoaDon = hoaDon;
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

	public ThietBiYTe getThietBi() {
		return thietBi;
	}

	public void setThietBi(ThietBiYTe thietBi) {
		if (thietBi == null) {
			throw new IllegalArgumentException("Thiết bị y tế không hợp lệ");
		}
		this.thietBi = thietBi;
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon{" + "hoaDon=" + hoaDon + ", thuoc=" + thuoc + ", soLuong=" + soLuong + '}';
	}
}
