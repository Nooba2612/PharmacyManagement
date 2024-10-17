package pharmacy.bus;

import pharmacy.dao.NhanVien_DAO;
import pharmacy.entity.NhanVien;

import java.util.List;

public class NhanVien_BUS {
    private NhanVien_DAO nhanVienDAO = new NhanVien_DAO();

    public boolean createEmployee(NhanVien nhanVien) {
        return nhanVienDAO.createEmployee(nhanVien);
    }

    public NhanVien getEmployeeByMaNhanVien(String maNhanVien) {
        return nhanVienDAO.getEmployeeByMaNhanVien(maNhanVien);
    }

    public boolean updateEmployee(NhanVien nhanVien) {
        return nhanVienDAO.updateEmployee(nhanVien);
    }

    public boolean deleteEmployee(String maNhanVien) {
        return nhanVienDAO.deleteEmployee(maNhanVien);
    }

    public List<NhanVien> getAllEmployees() {
        return nhanVienDAO.getAllEmployees();
    }

    public int countEmployees() {
        return nhanVienDAO.countEmployees();
    }

    public List<NhanVien> getTopRevenueEmployees(String date) {
        return nhanVienDAO.getTopRevenueEmployees(date);
    }

    public int getOrderQuantityOfEmployee(String maNhanVien) {
        return nhanVienDAO.getOrderQuantityOfEmployee(maNhanVien);
    }
}
