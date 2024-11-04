package pharmacy.bus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import pharmacy.dao.KhachHang_DAO;
import pharmacy.entity.KhachHang;

public class KhachHang_BUS {

    private final KhachHang_DAO khachHangDAO;

    public KhachHang_BUS() throws SQLException {
        this.khachHangDAO = new KhachHang_DAO();
    }

    public boolean createKhachHang(KhachHang khachHang) {
        if (khachHang == null) {
            return false;
        }

        if (khachHang.getHoTen() == null || khachHang.getHoTen().trim().isEmpty()) {
            return false;
        }

        String phone = khachHang.getSoDienThoai();
        if (phone == null || !phone.matches("^09\\d{8}$")) {
            return false;
        }

        if (khachHang.getDiemTichLuy() < 0) {
            return false;
        }

        LocalDate birthYear = khachHang.getNamSinh();
        if (birthYear == null || birthYear.isAfter(LocalDate.now())) {
            return false;
        }

        String notes = khachHang.getGhiChu();
        if (notes != null && notes.trim().length() > 200) {
            return false;
        }

        String gender = khachHang.getGioiTinh();
        if (gender == null || (!gender.equals("Nam") && !gender.equals("Nu"))) {
            return false;
        }

        return khachHangDAO.createKhachHang(khachHang);
    }

    public boolean updateCustomer(KhachHang khachHang) {
        if (khachHang == null) {
            System.out.println("Validation failed: khachHang is null.");
            return false;
        }

        if (khachHang.getHoTen() == null || khachHang.getHoTen().trim().isEmpty()) {
            System.out.println("Validation failed: Ho ten is null or empty.");
            return false;
        }

        if (khachHang.getSoDienThoai() == null || khachHang.getSoDienThoai().trim().isEmpty()) {
            System.out.println("Validation failed: So dien thoai is null or empty.");
            return false;
        }

        if (khachHang.getNamSinh() == null || khachHang.getNamSinh().isAfter(LocalDate.now().minusYears(18))) {
            System.out.println("Validation failed: Nam sinh is null or indicates age under 22.");
            return false;
        }

        if (khachHang.getGioiTinh() == null || (!khachHang.getGioiTinh().equalsIgnoreCase("Nam")
                && !khachHang.getGioiTinh().equalsIgnoreCase("Nữ")
                && !khachHang.getGioiTinh().equalsIgnoreCase("Khác"))) {
            System.out.println("Validation failed: Gioi tinh is invalid.");
            return false;
        }

        if (khachHang.getDiemTichLuy() < 0) {
            System.out.println("Validation failed: Diem tich luy is less than 0.");
            return false;
        }

        if (khachHang.getGhiChu() != null && khachHang.getGhiChu().length() > 255) {
            System.out.println("Validation failed: Ghi chu exceeds allowed length.");
            return false;
        }

        // Perform the update using DAO
        boolean result = khachHangDAO.updateKhachHang(khachHang);
        return result;
    }

    public boolean addKhachHang(KhachHang kh) {
        return khachHangDAO.addKhachHang(kh);
    }

    public boolean updateKhachHang(KhachHang kh) {
        return khachHangDAO.updateKhachHang(kh);
    }

    public boolean deleteKhachHang(String maKhachHang) {
        return khachHangDAO.deleteKhachHang(maKhachHang);
    }

    public List<KhachHang> getAllKhachHang() {
        return khachHangDAO.getAllKhachHang();
    }

    public KhachHang getKhachHangById(String maKhachHang) {
        return khachHangDAO.getKhachHangById(maKhachHang);
    }

    public int countKhachHang() {
        return khachHangDAO.countKhachHang();
    }

    public List<KhachHang> getNewCustomerByDate(String date) {
        return khachHangDAO.getNewCustomerByDate(date);
    }

    public List<KhachHang> getTopCustomer() {
        return khachHangDAO.getTopCustomer();
    }

    public void refreshKhachHang() {
        khachHangDAO.refreshKhachHang();
    }
}
