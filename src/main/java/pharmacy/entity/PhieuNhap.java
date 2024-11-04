package pharmacy.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhap {

	private String maPhieuNhap;
	private NhanVien nhanVien;
	private NhaCungCap nhaCungCap;
	private LocalDateTime thoiGianNhap;
	private List<ChiTietPhieuNhap> chiTietPhieuNhapList;

	public PhieuNhap() {
		this.chiTietPhieuNhapList = new ArrayList<>();
	}

	public PhieuNhap(String maPhieuNhap, NhanVien nhanVien, NhaCungCap nhaCungCap, LocalDateTime thoiGianNhap, List<ChiTietPhieuNhap> chiTietPhieuNhapList) {
		setMaPhieuNhap(maPhieuNhap);
		setNhanVien(nhanVien);
		setNhaCungCap(nhaCungCap);
		setThoiGianNhap(thoiGianNhap);
		this.chiTietPhieuNhapList = chiTietPhieuNhapList != null ? chiTietPhieuNhapList : new ArrayList<>();
	}

	public PhieuNhap(PhieuNhap phieuNhap) {
		this(phieuNhap.getMaPhieuNhap(), phieuNhap.getNhanVien(), phieuNhap.getNhaCungCap(),
				phieuNhap.getThoiGianNhap(), phieuNhap.getChiTietPhieuNhapList());
	}

	public String getMaPhieuNhap() {
		return maPhieuNhap;
	}

	public void setMaPhieuNhap(String maPhieuNhap) {
		if (maPhieuNhap == null || !maPhieuNhap.matches("PN\\d{4}")) {
			throw new IllegalArgumentException("Mã phiếu nhập không hợp lệ");
		}
		this.maPhieuNhap = maPhieuNhap;
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

	public NhaCungCap getNhaCungCap() {
		return nhaCungCap;
	}

	public void setNhaCungCap(NhaCungCap nhaCungCap) {
		if (nhaCungCap == null) {
			throw new IllegalArgumentException("Nhà cung cấp không hợp lệ");
		}
		this.nhaCungCap = nhaCungCap;
	}

	public void addChiTietPhieuNhap(ChiTietPhieuNhap chiTiet) {
		if (chiTiet == null) {
			throw new IllegalArgumentException("Chi tiết phiếu nhập không hợp lệ");
		}
		chiTietPhieuNhapList.add(chiTiet);
	}

	public List<ChiTietPhieuNhap> getChiTietPhieuNhapList() {
		return chiTietPhieuNhapList;
	}

	public LocalDateTime getThoiGianNhap() {
		return thoiGianNhap;
	}

	public void setThoiGianNhap(LocalDateTime thoiGianNhap) {
		if (thoiGianNhap == null) {
			throw new IllegalArgumentException("Thời gian nhập không hợp lệ");
		}
		this.thoiGianNhap = thoiGianNhap;
	}

	public String getTenNhaCungCap() {
        return nhaCungCap.getTenNCC();
    }

	public String getTenNhanVien() {
        return nhanVien.getHoTen();
    }

	public int getTongSoLuong() {
        int tongSoLuong = 0;
        if (chiTietPhieuNhapList != null) {
            for (ChiTietPhieuNhap chiTiet : chiTietPhieuNhapList) {
                tongSoLuong += chiTiet.getSoLuong(); // Cộng dồn số lượng
            }
        }
        return tongSoLuong;
    }

	public double getTongTien() {
        double tongTien = 0.0;
        if (chiTietPhieuNhapList != null) {
            for (ChiTietPhieuNhap chiTiet : chiTietPhieuNhapList) {
                tongTien += chiTiet.calculateThanhTien(); // Sử dụng calculateThanhTien()
            }
        }
        return tongTien;
    }

	@Override
	public String toString() {
		return "PhieuNhap{" + "maPhieuNhap='" + maPhieuNhap + '\'' + ", nhanVien=" + nhanVien + ", nhaCungCap="
				+ nhaCungCap + ", thoiGianNhap=" + thoiGianNhap + '}';
	}
}