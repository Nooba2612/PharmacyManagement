package pharmacy.entity;

import java.time.LocalDate;

public class SanPham {
	private String maSanPham;
	private String tenSanPham;
	private String danhMuc;
	private LocalDate ngaySX;
	private String nhaSX;
	private LocalDate ngayTao;
	private LocalDate ngayCapNhat;
	private int soLuongTon;
	private double donGiaBan;
	private float thue;
	private String moTa;
	private LocalDate hanSuDung;
	private String donViTinh;
	private String trangThai;
	private String loaiSanPham;

	public SanPham() {
	}

	public SanPham(String maSanPham, String tenSanPham, String danhMuc, LocalDate ngaySX, String nhaSX,
			LocalDate ngayTao, LocalDate ngayCapNhat, int soLuongTon, double donGiaBan,
			float thue, LocalDate hanSuDung, String moTa, String donViTinh, String trangThai,
			String loaiSanPham) {
		this.maSanPham = maSanPham;
		this.tenSanPham = tenSanPham;
		this.danhMuc = danhMuc;
		this.ngaySX = ngaySX;
		this.nhaSX = nhaSX;
		this.ngayTao = ngayTao;
		this.ngayCapNhat = ngayCapNhat;
		this.soLuongTon = soLuongTon;
		this.donGiaBan = donGiaBan;
		this.thue = thue;
		this.hanSuDung = hanSuDung;
		this.moTa = moTa;
		this.donViTinh = donViTinh;
		this.trangThai = trangThai;
		this.loaiSanPham = loaiSanPham;
	}

	public SanPham(SanPham original) {
		this.maSanPham = original.maSanPham;
		this.tenSanPham = original.tenSanPham;
		this.danhMuc = original.danhMuc;
		this.ngaySX = original.ngaySX;
		this.nhaSX = original.nhaSX;
		this.ngayTao = original.ngayTao;
		this.ngayCapNhat = original.ngayCapNhat;
		this.soLuongTon = original.soLuongTon;
		this.donGiaBan = original.donGiaBan;
		this.thue = original.thue;
		this.hanSuDung = original.hanSuDung;
		this.moTa = original.moTa;
		this.donViTinh = original.donViTinh;
		this.trangThai = original.trangThai;
		this.loaiSanPham = original.loaiSanPham;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		if (trangThai == null || trangThai.isEmpty()) {
			throw new IllegalArgumentException("Trạng thái không được rỗng.");
		}
		this.trangThai = trangThai;
	}

	public String getMaSanPham() {
		return maSanPham;
	}

	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}

	public String getDanhMuc() {
		return danhMuc;
	}

	public void setDanhMuc(String danhMuc) {
		this.danhMuc = danhMuc;
	}

	public String getTenSanPham() {
		return tenSanPham;
	}

	public void setTenSanPham(String tenSanPham) {
		if (tenSanPham == null || tenSanPham.isEmpty()) {
			throw new IllegalArgumentException("Tên thuốc không được rỗng.");
		}
		this.tenSanPham = tenSanPham;
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

	public String getNhaSX() {
		return nhaSX;
	}

	public void setNhaSX(String nhaSX) {
		this.nhaSX = nhaSX;
	}

	public LocalDate getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(LocalDate ngayTao) {
		this.ngayTao = ngayTao;
	}

	public LocalDate getNgayCapNhat() {
		return ngayCapNhat;
	}

	public void setNgayCapNhat(LocalDate ngayCapNhat) {
		this.ngayCapNhat = ngayCapNhat;
	}

	public int getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(int soLuongTon) {
		if (soLuongTon < 0) {
			throw new IllegalArgumentException("Số lượng tồn >= 0");
		}
		this.soLuongTon = soLuongTon;
	}

	public double getDonGiaBan() {
		return donGiaBan;
	}

	public void setDonGiaBan(double donGiaBan) {
		if (donGiaBan < 0) {
			throw new IllegalArgumentException("Đơn giá bán >= 0");
		}
		this.donGiaBan = donGiaBan;
	}

	public float getThue() {
		return thue;
	}

	public void setThue(float thue) {
		if (thue < 0 || thue > 1) {
			throw new IllegalArgumentException("0 <= Thuế <= 1");
		}
		this.thue = thue;
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

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		if (donViTinh == null || donViTinh.isEmpty()) {
			throw new IllegalArgumentException("Đơn vị tính không được rỗng.");
		}
		this.donViTinh = donViTinh;
	}

	public String getLoaiSanPham() { // Thêm getter cho loaiSanPham
		return loaiSanPham;
	}

	public void setLoaiSanPham(String loaiSanPham) { // Thêm setter cho loaiSanPham
		if (loaiSanPham == null || loaiSanPham.isEmpty()) {
			throw new IllegalArgumentException("Loại sản phẩm không được rỗng.");
		}
		this.loaiSanPham = loaiSanPham;
	}

	@Override
	public String toString() {
		return "SanPham{" +
				"maSanPham='" + maSanPham + '\'' +
				", tenSanPham='" + tenSanPham + '\'' +
				", danhMuc=" + danhMuc +
				", ngaySX=" + ngaySX +
				", nhaSX='" + nhaSX + '\'' +
				", ngayTao=" + ngayTao +
				", ngayCapNhat=" + ngayCapNhat +
				", soLuongTon=" + soLuongTon +
				", donGiaBan=" + donGiaBan +
				", thue=" + thue +
				", hanSuDung=" + hanSuDung +
				", donViTinh='" + donViTinh + '\'' +
				", loaiSanPham='" + loaiSanPham + '\'' +
				'}';
	}
}
