
package pharmacy.bus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import pharmacy.dao.HoaDon_DAO;
import pharmacy.entity.HoaDon;


public class HoaDon_BUS {
	private final HoaDon_DAO hoaDonDAO;

	public HoaDon_BUS() throws SQLException {
		this.hoaDonDAO = new HoaDon_DAO();
	}

	public boolean createHoaDon(HoaDon hoaDon) {
		return hoaDonDAO.createHoaDon(hoaDon);
	}
	public boolean createHoaDonTam(HoaDon hoaDon) {
		return hoaDonDAO.createHoaDonTam(hoaDon);
	}

	public HoaDon getHoaDonById(String maHoaDon) {
		return hoaDonDAO.getHoaDonById(maHoaDon);
	}

	public List<HoaDon> getAllHoaDon() {
		return hoaDonDAO.getAllHoaDon();
	}
	public List<HoaDon> getHoaDonTam() {
		return hoaDonDAO.getHoaDonTam();
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

	public double calculateRevenueByDate(String date) {
		return hoaDonDAO.calculateRevenueByDate(date);
	}

	public double calculateRevenueByMonth(int month, int year) {
		return hoaDonDAO.calculateRevenueByMonth(month, year);
	}

	public double calculateRevenueByYear(int year) {
		return hoaDonDAO.calculateRevenueByYear(year);
	}

	public double calculateRevenueByEmployee(String maNhanVien, String date) {
		return hoaDonDAO.calculateRevenueByEmployee(maNhanVien, date);
	}

	public List<HoaDon> getInvoiceByDate(LocalDate fromDate, LocalDate toDate) {
		return hoaDonDAO.getInvoiceByDate(fromDate, toDate);
	}

}
