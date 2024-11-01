package pharmacy.bus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import pharmacy.dao.SanPham_DAO;
import pharmacy.entity.SanPham;

public class SanPham_BUS {
	private final SanPham_DAO sanPhamDao;

	public SanPham_BUS() throws SQLException {
		this.sanPhamDao = new SanPham_DAO();
	}

	public boolean createSanPham(SanPham thuoc) {
		return sanPhamDao.createSanPham(thuoc);
	}

	public SanPham getSanPhamByMaSanPham(String maSanPham) {
		return sanPhamDao.getSanPhamByMaSanPham(maSanPham);
	}

	public boolean updateSanPham(SanPham thuoc) {
		if (thuoc == null) {
			return false;
		}

		if (thuoc.getTenSanPham() == null || thuoc.getTenSanPham().trim().isEmpty()) {
			return false;
		}

		if (thuoc.getNhaSX() == null || thuoc.getNhaSX().trim().isEmpty()) {
			return false;
		}

		if (thuoc.getNgaySX() == null || thuoc.getNgaySX().isAfter(LocalDate.now())) {
			return false;
		}

		if (thuoc.getDonViTinh() == null || thuoc.getDonViTinh().equals("Viên") || thuoc.getDonViTinh().equals("Vỉ")
				|| thuoc.getDonViTinh().equals("Hộp") || thuoc.getDonViTinh().equals("Chai")
				|| thuoc.getDonViTinh().equals("ống") || thuoc.getDonViTinh().equals("Gói")) {
			return false;
		}

		if ((thuoc.getNgayCapNhat() == null && thuoc.getNgayTao() == null) ||
				(thuoc.getNgayCapNhat() != null && thuoc.getNgayTao() != null &&
						thuoc.getNgayCapNhat().isBefore(thuoc.getNgayTao()))) {
			return false;
		}

		if (thuoc.getHanSuDung() == null || thuoc.getNgaySX() == null) {
			return false;
		}

		if (thuoc.getSoLuongTon() < 0) {
			return false;
		}

		if (thuoc.getDonGiaBan() <= 0) {
			return false;
		}

		if (thuoc.getThue() < 0) {
			return false;
		}

		return sanPhamDao.updateSanPham(thuoc);
	}

	public boolean deleteSanPham(String maSanPham) {
		return sanPhamDao.deleteSanPham(maSanPham);
	}

	public List<SanPham> getAllSanPham() {
		return sanPhamDao.getAllSanPham();
	}

	public List<SanPham> getSanPhamSapHetTonKho() {
		return sanPhamDao.getSanPhamSapHetTonKho();
	}

	public List<SanPham> getSanPhamSapHetHanSuDung() {
		return sanPhamDao.getSanPhamSapHetHanSuDung();
	}

	public List<SanPham> getSanPhamDaHetHan() {
		return sanPhamDao.getSanPhamDaHetHan();
	}

	public int countSanPham() {
		return sanPhamDao.countSanPham();
	}

	public int countThuoc() {
		return sanPhamDao.countThuoc();
	}

	public int countThietBiYTe() {
		return sanPhamDao.countThietBiYTe();
	}

	public int countSanPhamSapHetTonKho() {
		return sanPhamDao.countSanPhamSapHetTonKho();
	}

	public int countSanPhamSapHetHanSuDung() {
		return sanPhamDao.countSanPhamSapHetHanSuDung();
	}

	public int countSanPhamDaHetHan() {
		return sanPhamDao.countSanPhamDaHetHan();
	}

	public List<SanPham> getTopSaleSanPhamByDate(String date) {
		return sanPhamDao.getTopSaleSanPhamByDate(date);
	}

	public int getSoldQuantityById(String maSanPham, String date) {
		return sanPhamDao.getSoldQuantityById(maSanPham, date);
	}

}
