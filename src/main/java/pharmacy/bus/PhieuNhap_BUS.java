package pharmacy.bus;

import java.sql.Connection;
import java.util.List;

import pharmacy.dao.PhieuNhap_DAO;
import pharmacy.entity.PhieuNhap;

public class PhieuNhap_BUS {
	private PhieuNhap_DAO phieuNhapDAO;

	public PhieuNhap_BUS() {
		this.phieuNhapDAO = new PhieuNhap_DAO();
	}

	public boolean createPhieuNhap(PhieuNhap phieuNhap, Connection connection) {
		return phieuNhapDAO.createPhieuNhap(phieuNhap, connection);
	}

	public PhieuNhap getPhieuNhapByMaPhieuNhap(String maPhieuNhap) {
		return phieuNhapDAO.getPhieuNhapByMaPhieuNhap(maPhieuNhap);
	}

	public List<PhieuNhap> getAllPhieuNhap() {
		return phieuNhapDAO.getAllPhieuNhap();
	}

	public boolean updatePhieuNhap(PhieuNhap phieuNhap) {
		return phieuNhapDAO.updatePhieuNhap(phieuNhap);
	}

	public boolean deletePhieuNhap(String maPhieuNhap) {
		return phieuNhapDAO.deletePhieuNhap(maPhieuNhap);
	}

	public int countPhieuNhap() {
		return phieuNhapDAO.countPhieuNhap();
	}
}
