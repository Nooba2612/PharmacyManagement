package pharmacy.entity;

public class ChiTietHoaDon {
	private SanPham sanpham;
	private int soLuong;

	public ChiTietHoaDon() {
	}

	public ChiTietHoaDon(HoaDon hoaDon, SanPham sanpham, int soLuong) {

		if (sanpham == null) {
			throw new IllegalArgumentException("Thuốc không hợp lệ");
		}
		this.sanpham = sanpham;

		if (soLuong < 0) {
			throw new IllegalArgumentException("Số lượng không hợp lệ");
		}
		this.soLuong = soLuong;
	}

	public ChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		if (chiTietHoaDon != null) {
			this.sanpham = chiTietHoaDon.sanpham;
			this.soLuong = chiTietHoaDon.soLuong;
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

	public SanPham getSanPham() {
		return sanpham;
	}

	public void setSanPham(SanPham sanpham) {
		if (sanpham == null) {
			throw new IllegalArgumentException("Thuốc không hợp lệ");
		}
		this.sanpham = sanpham;
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon{" + ", sanpham=" + sanpham + ", soLuong=" + soLuong + '}';
	}
}
