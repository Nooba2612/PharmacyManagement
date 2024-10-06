package pharmacy.bus;

import pharmacy.dao.NhaCungCap_DAO;
import pharmacy.entity.NhaCungCap;

import java.util.List;

public class NhaCungCap_BUS {
	private NhaCungCap_DAO nhaCungCapDAO = new NhaCungCap_DAO();

	public boolean createNhaCungCap(NhaCungCap nhaCungCap) {
		return nhaCungCapDAO.createNhaCungCap(nhaCungCap);
	}

	public NhaCungCap getNhaCungCapByMaNCC(String maNCC) {
		return nhaCungCapDAO.getNhaCungCapByMaNCC(maNCC);
	}

	public List<NhaCungCap> getAllNhaCungCap() {
		return nhaCungCapDAO.getAllNhaCungCap();
	}

	public boolean updateNhaCungCap(NhaCungCap nhaCungCap) {
		return nhaCungCapDAO.updateNhaCungCap(nhaCungCap);
	}

	public boolean deleteNhaCungCap(String maNCC) {
		return nhaCungCapDAO.deleteNhaCungCap(maNCC);
	}

	public int countNhaCungCap() {
		return nhaCungCapDAO.countNhaCungCap();
	}
}
