package pharmacy.bus;

import pharmacy.dao.ChiTietHoaDon_DAO;
import pharmacy.entity.ChiTietHoaDon;

import java.util.List;

public class ChiTietHoaDon_BUS {
	private ChiTietHoaDon_DAO chiTietHoaDonDAO;

	public ChiTietHoaDon_BUS() {
		chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
	}

	public boolean createChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		return chiTietHoaDonDAO.createChiTietHoaDon(chiTietHoaDon);
	}

	public List<ChiTietHoaDon> getChiTietHoaDonByMaHoaDon(String maHoaDon) {
		return chiTietHoaDonDAO.getChiTietHoaDonByMaHoaDon(maHoaDon);
	}

	public boolean updateChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		return chiTietHoaDonDAO.updateChiTietHoaDon(chiTietHoaDon);
	}

	public boolean deleteChiTietHoaDon(String maHoaDon) {
		return chiTietHoaDonDAO.deleteChiTietHoaDon(maHoaDon);
	}
}
