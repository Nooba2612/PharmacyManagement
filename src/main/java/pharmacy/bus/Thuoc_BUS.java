package pharmacy.bus;

import pharmacy.dao.Thuoc_DAO;
import pharmacy.entity.Thuoc;

import java.util.List;

public class Thuoc_BUS {
	private final Thuoc_DAO thuocDao;

	public Thuoc_BUS() {
		this.thuocDao = new Thuoc_DAO();
	}

	public boolean createThuoc(Thuoc thuoc) {
		return thuocDao.createThuoc(thuoc);
	}

	public Thuoc getThuocByMaThuoc(String maThuoc) {
		return thuocDao.getThuocByMaThuoc(maThuoc);
	}

	public boolean updateThuoc(Thuoc thuoc) {
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
}
