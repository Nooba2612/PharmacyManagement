package pharmacy.bus;

import pharmacy.dao.KhachHang_DAO;
import pharmacy.entity.KhachHang;

import java.util.List;

public class KhachHang_BUS {
    private KhachHang_DAO khachHangDAO = new KhachHang_DAO();

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
