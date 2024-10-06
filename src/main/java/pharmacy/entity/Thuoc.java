package pharmacy.entity;

import java.time.LocalDate;

public class Thuoc {
	private String maThuoc;
	private String tenThuoc;
	private DanhMuc danhMuc;
	private LocalDate ngaySX;
	private String nhaSX;
	private LocalDate ngayTao;
	private LocalDate ngayCapNhat;
	private int soLuongTon;
	private double donGiaBan;
	private float thue;
	private String moTa;
	private LocalDate hanSuDung;

	public Thuoc() {
	}

	public Thuoc(String maThuoc, String tenThuoc, DanhMuc danhMuc, LocalDate ngaySX, String nhaSX, LocalDate ngayTao,
			LocalDate ngayCapNhat, int soLuongTon, double donGiaBan, float thue, LocalDate hanSuDung, String moTa) {
		setMaThuoc(maThuoc);
		setTenThuoc(tenThuoc);
		setDanhMuc(danhMuc);
		setNgaySX(ngaySX);
		setNhaSX(nhaSX);
		setNgayTao(ngayTao);
		setNgayCapNhat(ngayCapNhat);
		setSoLuongTon(soLuongTon);
		setDonGiaBan(donGiaBan);
		setThue(thue);
		setHanSuDung(hanSuDung);
		setMoTa(moTa);
	}

	public Thuoc(Thuoc original) {
		this.maThuoc = original.maThuoc;
		this.tenThuoc = original.tenThuoc;
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
	}

	public String getMaThuoc() {
		return maThuoc;
	}

	public void setMaThuoc(String maThuoc) {
		if (maThuoc == null || maThuoc.isEmpty()) {
			this.maThuoc = generateMaThuoc();
		} else {
			this.maThuoc = maThuoc;
		}
	}

	public DanhMuc getDanhMuc() {
		return danhMuc;
	}

	public void setDanhMuc(DanhMuc danhMuc) {
		this.danhMuc = danhMuc;
	}

	public String getTenThuoc() {
		return tenThuoc;
	}

	public void setTenThuoc(String tenThuoc) {
		if (tenThuoc == null || tenThuoc.isEmpty()) {
			throw new IllegalArgumentException("Tên thuốc không được rỗng.");
		}
		this.tenThuoc = tenThuoc;
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
		if (nhaSX == null || nhaSX.isEmpty()) {
			throw new IllegalArgumentException("Nhà sản xuất không được rỗng.");
		}
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

	@Override
	public String toString() {
		return "Thuoc{" + "maThuoc='" + maThuoc + '\'' + ", tenThuoc='" + tenThuoc + '\'' + ", danhMuc=" + danhMuc
				+ ", ngaySX=" + ngaySX + ", nhaSX='" + nhaSX + '\'' + ", ngayTao=" + ngayTao + ", ngayCapNhat="
				+ ngayCapNhat + ", soLuongTon=" + soLuongTon + ", donGiaBan=" + donGiaBan + ", thue=" + thue
				+ ", hanSuDung=" + hanSuDung + '}';
	}

	private String generateMaThuoc() {
		return "MTH" + System.currentTimeMillis();
	}
}
