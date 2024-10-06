package pharmacy.Interface;

import pharmacy.entity.HoaDon;
import java.util.List;

public interface HoaDon_Interface {
	boolean createHoaDon(HoaDon hoaDon);

	HoaDon getHoaDonById(String maHoaDon);

	List<HoaDon> getAllHoaDon();

	boolean updateHoaDon(HoaDon hoaDon);

	boolean deleteHoaDon(String maHoaDon);

	int countHoaDon();

	String calculateTotalRevenue();

	String calculateRevenueByDate(String date);

	String calculateRevenueByMonth(int year, int month);

	String calculateRevenueByYear(int year);

}
