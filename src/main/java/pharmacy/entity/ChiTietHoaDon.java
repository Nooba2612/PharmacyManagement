package pharmacy.entity;

public class ChiTietHoaDon {
	private String maHoaDon;
	private String maSanPham;
	private int soLuong;
	private Float thue;

	public ChiTietHoaDon() {
	}

	public ChiTietHoaDon(String maHoaDon, String maSanPham, int soLuong, Float thue) {
		this.maHoaDon = maHoaDon;
		this.maSanPham = maSanPham;
		this.soLuong = soLuong;
		this.thue = thue;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
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

	@Override
	public String toString() {
		return "ChiTietHoaDon{" + ", maSanPham=" + maSanPham + ", soLuong=" + soLuong + '}' + ", maHoaDon=" + maHoaDon
				+ '}';
	}
}
