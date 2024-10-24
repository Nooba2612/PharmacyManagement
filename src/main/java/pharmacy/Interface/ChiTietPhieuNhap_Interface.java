package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.ChiTietPhieuNhap;

public interface ChiTietPhieuNhap_Interface {
	boolean createChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap);

	boolean updateChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap);

	boolean deleteChiTietPhieuNhap(String maPhieuNhap, String maThuoc);

	List<ChiTietPhieuNhap> getChiTietPhieuNhapByMa(String maPhieuNhap);

	List<ChiTietPhieuNhap> getAllChiTietPhieuNhap();
}
