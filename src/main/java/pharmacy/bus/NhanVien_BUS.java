package pharmacy.bus;

import java.time.LocalDate;
import java.util.List;

import pharmacy.dao.NhanVien_DAO;
import pharmacy.entity.NhanVien;

public class NhanVien_BUS {
    private NhanVien_DAO nhanVienDAO = new NhanVien_DAO();

    public boolean createEmployee(NhanVien nhanVien) {
        return nhanVienDAO.createEmployee(nhanVien);
    }

    public NhanVien getEmployeeByMaNhanVien(String maNhanVien) {
        return nhanVienDAO.getEmployeeByMaNhanVien(maNhanVien);
    }

    public boolean updateEmployee(NhanVien nhanVien) {
        if (nhanVien == null) {
            return false;
        }

        if (nhanVien.getHoTen() == null || nhanVien.getHoTen().isEmpty()) {
            return false;
        }

        if (nhanVien.getEmail() == null || !nhanVien.getEmail().matches("^[\\w-\\.]+@[\\w-\\.]+\\.[a-zA-Z]{2,4}$")) {
            return false;
        }

        if (nhanVien.getSoDienThoai() == null || !nhanVien.getSoDienThoai().matches("^0\\\\d{9}$")) {
            return false;
        }

        if (nhanVien.getNamSinh() != null || nhanVien.getNamSinh().isAfter(LocalDate.now())) {
            return false;
        }

        if (nhanVien.getNgayVaoLam() != null || nhanVien.getNgayVaoLam().isAfter(LocalDate.now())) {
            return false;
        }

        if (nhanVien.getChucVu().isEmpty() || !nhanVien.getChucVu().equals("Nhân viên")
                || !nhanVien.getChucVu().equals("Người quản lý")) {
            return false;
        }

        if (nhanVien.getTrangThai().isEmpty() || !nhanVien.getTrangThai().equals("Còn làm việc")
                || !nhanVien.getTrangThai().equals("Nghỉ việc tạm thời")
                || !nhanVien.getTrangThai().equals("Nghỉ việc hẳn")) {
            return false;
        }

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

    public List<NhanVien> getEmployeesByStatus(String status) {
        return nhanVienDAO.getEmployeesByStatus(status);
    }
}
