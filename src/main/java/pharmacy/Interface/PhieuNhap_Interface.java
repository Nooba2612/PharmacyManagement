package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.PhieuNhap;

public interface PhieuNhap_Interface {
	boolean createPhieuNhap(PhieuNhap phieuNhap);

	PhieuNhap getPhieuNhapByMaPhieuNhap(String maPhieuNhap);

	List<PhieuNhap> getAllPhieuNhap();

	boolean updatePhieuNhap(PhieuNhap phieuNhap);

	boolean deletePhieuNhap(String maPhieuNhap);

	int countPhieuNhap();
}
