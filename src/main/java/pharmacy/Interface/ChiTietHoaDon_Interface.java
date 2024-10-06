package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.ChiTietHoaDon;

public interface ChiTietHoaDon_Interface {
	boolean createChiTietHoaDon(ChiTietHoaDon chiTietHoaDon);
	List<ChiTietHoaDon> getChiTietHoaDonByMaHoaDon(String maHoaDon);
    boolean updateChiTietHoaDon(ChiTietHoaDon chiTietHoaDon);
    boolean deleteChiTietHoaDon(String maHoaDon);
}
