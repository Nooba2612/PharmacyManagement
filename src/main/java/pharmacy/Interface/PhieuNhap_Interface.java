package pharmacy.Interface;

import pharmacy.entity.PhieuNhap;
import java.util.List;

public interface PhieuNhap_Interface {
	boolean createPhieuNhap(PhieuNhap phieuNhap);

	PhieuNhap getPhieuNhapByMaPhieuNhap(String maPhieuNhap);

	List<PhieuNhap> getAllPhieuNhap();

	boolean updatePhieuNhap(PhieuNhap phieuNhap);

	boolean deletePhieuNhap(String maPhieuNhap);

	int countPhieuNhap();
}
