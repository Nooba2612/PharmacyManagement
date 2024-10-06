package pharmacy.bus;

import pharmacy.dao.HoaDon_DAO;
import pharmacy.entity.HoaDon;

import java.util.List;

public class HoaDon_BUS {
	private final HoaDon_DAO hoaDonDAO;

	public HoaDon_BUS() {
		this.hoaDonDAO = new HoaDon_DAO();
	}

	public boolean createHoaDon(HoaDon hoaDon) {
		return hoaDonDAO.createHoaDon(hoaDon);
	}

	public HoaDon getHoaDonById(String maHoaDon) {
		return hoaDonDAO.getHoaDonById(maHoaDon);
	}

	public List<HoaDon> getAllHoaDon() {
		return hoaDonDAO.getAllHoaDon();
	}

	public boolean updateHoaDon(HoaDon hoaDon) {
		return hoaDonDAO.updateHoaDon(hoaDon);
	}

	public boolean deleteHoaDon(String maHoaDon) {
		return hoaDonDAO.deleteHoaDon(maHoaDon);
	}

	public int countHoaDon() {
		return hoaDonDAO.countHoaDon();
	}

	public String calculateTotalRevenue() {
		return hoaDonDAO.calculateTotalRevenue();
	}

	public String calculateRevenueByDate(String date) {
		return hoaDonDAO.calculateRevenueByDate(date);
	}

	public String calculateRevenueByMonth(int year, int month) {
		return hoaDonDAO.calculateRevenueByMonth(year, month);
	}

	public String calculateRevenueByYear(int year) {
		return hoaDonDAO.calculateRevenueByYear(year);
	}
}
