package pharmacy.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LichLamViec {

	private String maLichLamViec;
	private NhanVien nhanVien;
	private String caLam;
	private LocalDate ngayLam;

	public LichLamViec() {
	}

	public LichLamViec(String maLichLamViec, String caLam, NhanVien nhanVien, LocalDate ngayLam) {
		this.maLichLamViec = maLichLamViec;
		this.nhanVien = nhanVien;
		this.caLam = caLam;
		this.ngayLam = ngayLam;
	}

	public LichLamViec(LichLamViec other) {
		this.maLichLamViec = other.maLichLamViec;
		this.nhanVien = other.nhanVien;
		this.caLam = other.caLam;
		this.ngayLam = other.ngayLam;
	}

	public String getMaLichLamViec() {
		return maLichLamViec;
	}

	public void setMaLichLamViec(String maLichLamViec) {
		if (maLichLamViec == null || maLichLamViec.isEmpty()) {
			throw new IllegalArgumentException("Mã lịch làm việc không được rỗng");
		}
		this.maLichLamViec = maLichLamViec;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		if (nhanVien == null) {
			throw new IllegalArgumentException("Nhân viên không hợp lệ");
		}
		this.nhanVien = nhanVien;
	}

	public String getCaLam() {
		return caLam;
	}

	public void setCaLam(String caLam) {
		if (caLam == null || caLam.isEmpty()) {
			throw new IllegalArgumentException("Ca làm việc không hợp lệ");
		}
		this.caLam = caLam;
	}

	public LocalDate getNgayLam() {
		return ngayLam;
	}

	public void setNgayLam(LocalDate ngayLam) {
		if (ngayLam == null) {
			throw new IllegalArgumentException("Ngày làm không hợp lệ");
		}
		this.ngayLam = ngayLam;
	}

	@Override
	public String toString() {
		return "LichLamViec{" + "maLichLamViec='" + maLichLamViec + '\'' + ", nhanVien='" + nhanVien + '\''
				+ ", caLam='" + caLam + '\'' + ", ngayLam=" + ngayLam.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
				+ '}';
	}
}
