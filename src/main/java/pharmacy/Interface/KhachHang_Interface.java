package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.KhachHang;

public interface KhachHang_Interface {

    boolean addKhachHang(KhachHang kh);

    boolean updateKhachHang(KhachHang kh);

    boolean deleteKhachHang(String maKhachHang);

    List<KhachHang> getAllKhachHang();

    KhachHang getKhachHangById(String maKhachHang);

    KhachHang getKhachHangByPhone(String phone);

    int countCustomer();

    List<KhachHang> getNewCustomerByDate(String date);

    List<KhachHang> getTopCustomer();

    boolean createKhachHang(KhachHang customer);

}
