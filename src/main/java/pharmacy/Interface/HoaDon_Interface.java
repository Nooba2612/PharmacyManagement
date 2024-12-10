package pharmacy.Interface;

import java.time.LocalDate;
import java.util.List;

import pharmacy.entity.HoaDon;

public interface HoaDon_Interface {
	boolean createHoaDon(HoaDon hoaDon);
	
	boolean createHoaDonTam(HoaDon hoaDon);

	HoaDon getHoaDonById(String maHoaDon);

	List<HoaDon> getAllHoaDon();

	List<HoaDon> getHoaDonTam();

	boolean updateHoaDon(HoaDon hoaDon);

	boolean deleteHoaDon(String maHoaDon);

	int countHoaDon();

	String calculateTotalRevenue();

	double calculateRevenueByDate(String date);

	double calculateRevenueByMonth(int month, int year);

	double calculateRevenueByYear(int year);
	
	double calculateRevenueByEmployee(String maNhanVien, String date);

	List<HoaDon> getInvoiceByDate(LocalDate fromDate, LocalDate toDate);

	boolean updateHoaDonTam(HoaDon hoaDonTam);

}
