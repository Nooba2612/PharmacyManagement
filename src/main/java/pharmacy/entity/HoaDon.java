package pharmacy.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {

	private String maHoaDon;
	private KhachHang khachHang;
	private NhanVien nhanVien;
	private LocalDate ngayTao;
	private double tienKhachDua;
	private double diemSuDung;
	private String loaiThanhToan;
	private List<ChiTietHoaDon> chiTietHoaDonList;

	public HoaDon() {
		chiTietHoaDonList = new ArrayList<>();
	}

	public HoaDon(String maHoaDon, KhachHang khachHang, NhanVien nhanVien, LocalDate ngayTao, double tienKhachDua,
			double diemSuDung, String loaiThanhToan, List<ChiTietHoaDon> chiTietHoaDonList) {
		setMaHoaDon(maHoaDon);
		setKhachHang(khachHang);
		setNhanVien(nhanVien);
		setNgayTao(ngayTao);
		setTienKhachDua(tienKhachDua);
		setDiemSuDung(diemSuDung);
		setLoaiThanhToan(loaiThanhToan);
		this.chiTietHoaDonList = chiTietHoaDonList != null ? chiTietHoaDonList : new ArrayList<>();
	}

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		if (maHoaDon == null || !maHoaDon.matches("HD\\d{6}\\d{5}")) {
			throw new IllegalArgumentException("Mã hóa đơn không hợp lệ");
		}
		this.maHoaDon = maHoaDon;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		if (khachHang == null) {
			throw new IllegalArgumentException("Khách hàng không hợp lệ");
		}
		this.khachHang = khachHang;
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

	public LocalDate getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(LocalDate ngayTao) {
		if (ngayTao == null) {
			throw new IllegalArgumentException("Ngày tạo không hợp lệ");
		}
		this.ngayTao = ngayTao;
	}

	public double getTienKhachDua() {
		return tienKhachDua;
	}

	public void setTienKhachDua(double tienKhachDua) {
		if (tienKhachDua < 0) {
			throw new IllegalArgumentException("Tiền khách đưa không hợp lệ");
		}
		this.tienKhachDua = tienKhachDua;
	}

	public double getDiemSuDung() {
		return diemSuDung;
	}

	public void setDiemSuDung(double diemSuDung) {
		if (diemSuDung < 0) {
			throw new IllegalArgumentException("Điểm sử dụng không hợp lệ");
		}
		this.diemSuDung = diemSuDung;
	}

	public String getLoaiThanhToan() {
		return loaiThanhToan;
	}

	public void setLoaiThanhToan(String loaiThanhToan) {
		if (!loaiThanhToan.equals("Tiền mặt") && !loaiThanhToan.equals("Chuyển khoản")) {
			throw new IllegalArgumentException("Loại thanh toán không hợp lệ");
		}
		this.loaiThanhToan = loaiThanhToan;
	}

	public List<ChiTietHoaDon> getChiTietHoaDonList() {
		return chiTietHoaDonList;
	}

	public void setChiTietHoaDonList(List<ChiTietHoaDon> chiTietHoaDonList) {
		if (chiTietHoaDonList == null) {
			throw new IllegalArgumentException("Danh sách chi tiết hóa đơn không hợp lệ");
		}
		this.chiTietHoaDonList = chiTietHoaDonList;
	}

	@Override
	public String toString() {
		return "HoaDon{" + "maHoaDon='" + maHoaDon + '\'' + ", khachHang=" + khachHang + ", nhanVien=" + nhanVien
				+ ", ngayTao=" + ngayTao + ", tienKhachDua=" + tienKhachDua + ", diemSuDung=" + diemSuDung
				+ ", loaiThanhToan='" + loaiThanhToan + '\'' + ", chiTietHoaDonList=" + chiTietHoaDonList + '}';
	}
}
