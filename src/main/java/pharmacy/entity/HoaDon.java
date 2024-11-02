package pharmacy.entity;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pharmacy.bus.SanPham_BUS;

public class HoaDon {
	private String maHoaDon;
	private KhachHang khachHang;
	private NhanVien nhanVien;
	private LocalDate ngayTao;
	private double tienKhachDua;
	private double diemSuDung;
	private String loaiThanhToan;
	private List<ChiTietHoaDon> chiTietHoaDonList;
	private Double tienThua;
	private Double tongTien;

	public HoaDon() {
		chiTietHoaDonList = new ArrayList<>();
	}

	public HoaDon(String maHoaDon, KhachHang khachHang, NhanVien nhanVien, LocalDate ngayTao, double tienKhachDua,
			double diemSuDung, String loaiThanhToan, List<ChiTietHoaDon> chiTietHoaDonList) throws SQLException {
		this.maHoaDon = maHoaDon;
		this.khachHang = khachHang;
		this.nhanVien = nhanVien;
		this.ngayTao = ngayTao;
		this.tienKhachDua = tienKhachDua;
		this.diemSuDung = diemSuDung;
		this.loaiThanhToan = loaiThanhToan;
		this.chiTietHoaDonList = chiTietHoaDonList != null ? chiTietHoaDonList : new ArrayList<>();
		this.tongTien = 0.0;
		if (chiTietHoaDonList != null && !chiTietHoaDonList.isEmpty()) {
			for (ChiTietHoaDon cthd : chiTietHoaDonList) {
				if (cthd != null && cthd.getMaSanPham() != null) {
					SanPham sp = new SanPham_BUS().getSanPhamByMaSanPham(cthd.getMaSanPham());
					if (sp != null) {
						this.tongTien += (sp.getDonGiaBan() * cthd.getSoLuong())
								- (sp.getDonGiaBan() * cthd.getSoLuong() * cthd.getThue());
					}
				}
			}
		}

		this.tienThua = tienKhachDua - tongTien;
	}

	public Double getTienThua() {
		return tienThua;
	}

	public Double getTongTien() {
		return tongTien;
	}

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public LocalDate getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(LocalDate ngayTao) {
		this.ngayTao = ngayTao;
	}

	public double getTienKhachDua() {
		return tienKhachDua;
	}

	public void setTienKhachDua(double tienKhachDua) {
		this.tienKhachDua = tienKhachDua;
	}

	public double getDiemSuDung() {
		return diemSuDung;
	}

	public void setDiemSuDung(double diemSuDung) {
		this.diemSuDung = diemSuDung;
	}

	public String getLoaiThanhToan() {
		return loaiThanhToan;
	}

	public void setLoaiThanhToan(String loaiThanhToan) {
		this.loaiThanhToan = loaiThanhToan;
	}

	public List<ChiTietHoaDon> getChiTietHoaDonList() {
		return chiTietHoaDonList;
	}

	public void setChiTietHoaDonList(List<ChiTietHoaDon> chiTietHoaDonList) {
		this.chiTietHoaDonList = chiTietHoaDonList;
	}

	@Override
	public String toString() {
		return "HoaDon{" + "maHoaDon='" + maHoaDon + '\'' + ", khachHang=" + khachHang + ", nhanVien=" + nhanVien
				+ ", ngayTao=" + ngayTao + ", tienKhachDua=" + tienKhachDua + ", diemSuDung=" + diemSuDung
				+ ", loaiThanhToan='" + loaiThanhToan + '\'' + ", chiTietHoaDonList=" + chiTietHoaDonList + '}';
	}
}
