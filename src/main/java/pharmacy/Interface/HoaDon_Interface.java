package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.HoaDon;

public interface HoaDon_Interface {
	boolean createHoaDon(HoaDon hoaDon);

	HoaDon getHoaDonById(String maHoaDon);

	List<HoaDon> getAllHoaDon();

	boolean updateHoaDon(HoaDon hoaDon);

	boolean deleteHoaDon(String maHoaDon);

	int countHoaDon();

	String calculateTotalRevenue();

	double calculateRevenueByDate(String date);

	double calculateRevenueByMonth(int month, int year);

	double calculateRevenueByYear(int year);
	
	double calculateRevenueByEmployee(String maNhanVien, String date);

}
