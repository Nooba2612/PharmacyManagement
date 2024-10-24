package pharmacy.bus;

import java.util.List;

import pharmacy.dao.LichLamViec_DAO;
import pharmacy.entity.LichLamViec;

public class LichLamViec_BUS {
	private LichLamViec_DAO lichLamViecDAO = new LichLamViec_DAO();

	public boolean createLichLamViec(LichLamViec lichLamViec) {
		return lichLamViecDAO.createLichLamViec(lichLamViec);
	}

	public LichLamViec getLichLamViecById(String maLichLamViec) {
		return lichLamViecDAO.getLichLamViecById(maLichLamViec);
	}

	public List<LichLamViec> getAllLichLamViec() {
		return lichLamViecDAO.getAllLichLamViec();
	}

	public boolean updateLichLamViec(LichLamViec lichLamViec) {
		return lichLamViecDAO.updateLichLamViec(lichLamViec);
	}

	public boolean deleteLichLamViec(String maLichLamViec) {
		return lichLamViecDAO.deleteLichLamViec(maLichLamViec);
	}

}
