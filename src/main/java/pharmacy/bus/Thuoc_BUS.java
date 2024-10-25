package pharmacy.bus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import pharmacy.dao.Thuoc_DAO;
import pharmacy.entity.Thuoc;

public class Thuoc_BUS {
	private final Thuoc_DAO thuocDao;

	public Thuoc_BUS() throws SQLException {
		this.thuocDao = new Thuoc_DAO();
	}

	public boolean createThuoc(Thuoc thuoc) {
		return thuocDao.createThuoc(thuoc);
	}

	public Thuoc getThuocByMaThuoc(String maThuoc) {
		return thuocDao.getThuocByMaThuoc(maThuoc);
	}

	public boolean updateThuoc(Thuoc thuoc) {
		if (thuoc == null) {
			return false;
		}

		if (thuoc.getTenThuoc() == null || thuoc.getTenThuoc().isEmpty()) {
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

		return thuocDao.updateThuoc(thuoc);
	}

	public boolean deleteThuoc(String maThuoc) {
		return thuocDao.deleteThuoc(maThuoc);
	}

	public List<Thuoc> getAllThuoc() {
		return thuocDao.getAllThuoc();
	}

	public List<Thuoc> getThuocSapHetTonKho() {
		return thuocDao.getThuocSapHetTonKho();
	}

	public List<Thuoc> getThuocSapHetHanSuDung() {
		return thuocDao.getThuocSapHetHanSuDung();
	}

	public List<Thuoc> getThuocDaHetHan() {
		return thuocDao.getThuocDaHetHan();
	}

	public int countThuoc() {
		return thuocDao.countThuoc();
	}

	public int countThuocSapHetTonKho() {
		return thuocDao.countThuocSapHetTonKho();
	}

	public int countThuocSapHetHanSuDung() {
		return thuocDao.countThuocSapHetHanSuDung();
	}

	public int countThuocDaHetHan() {
		return thuocDao.countThuocDaHetHan();
	}

	public List<Thuoc> getTopSaleThuocByDate(String date) {
		return thuocDao.getTopSaleThuocByDate(date);
	}

	public int getSoldQuantityById(String maThuoc, String date) {
		return thuocDao.getSoldQuantityById(maThuoc, date);
	}

}
