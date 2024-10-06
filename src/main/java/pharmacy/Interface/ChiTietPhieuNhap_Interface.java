package pharmacy.Interface;

import pharmacy.entity.ChiTietPhieuNhap;
import java.util.List;

public interface ChiTietPhieuNhap_Interface {
	boolean createChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap);

	boolean updateChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap);

	boolean deleteChiTietPhieuNhap(String maPhieuNhap, String maThuoc);

	List<ChiTietPhieuNhap> getChiTietPhieuNhapByMa(String maPhieuNhap);

	List<ChiTietPhieuNhap> getAllChiTietPhieuNhap();
}
