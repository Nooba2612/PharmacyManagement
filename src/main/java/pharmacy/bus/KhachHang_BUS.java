package pharmacy.bus;

import java.time.LocalDate;
import java.util.List;

import pharmacy.dao.KhachHang_DAO;
import pharmacy.entity.KhachHang;

public class KhachHang_BUS {
    private KhachHang_DAO khachHangDAO = new KhachHang_DAO();

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

    public List<KhachHang> getNewCustomerByDate(String date){
        return khachHangDAO.getNewCustomerByDate(date);
    }

    public List<KhachHang> getTopCustomer() {
        return khachHangDAO.getTopCustomer();
    }
}
