package pharmacy.bus;

import java.util.List;

import pharmacy.dao.ChiTietHoaDon_DAO;
import pharmacy.entity.ChiTietHoaDon;

public class ChiTietHoaDon_BUS {

    private ChiTietHoaDon_DAO chiTietHoaDonDAO;

    public ChiTietHoaDon_BUS() {
        chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
    }

    public boolean createChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        if (chiTietHoaDon == null) {
            System.out.println("Chi tiết hóa đơn không hợp lệ.");
            return false;
        }

        if (chiTietHoaDon.getMaHoaDon() == null || chiTietHoaDon.getMaSanPham() == null) {
            System.out.println("Mã hóa đơn hoặc mã sản phẩm không hợp lệ.");
            return false;
        }

        if (chiTietHoaDon.getSoLuong() <= 0) {
            System.out.println("Số lượng phải lớn hơn 0.");
            return false;
        }

        if (chiTietHoaDon.getDonGiaNhap() <= 0) {
            System.out.println("Đơn giá nhập phải lớn hơn 0.");
            return false;
        }

        if (chiTietHoaDon.getThue() < 0 || chiTietHoaDon.getThue() > 1) {
            System.out.println("Thuế không hợp lệ, phải nằm trong khoảng từ 0 đến 1.");
            return false;
        }

        try {
            return chiTietHoaDonDAO.createChiTietHoaDon(chiTietHoaDon);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo chi tiết hóa đơn: " + e.getMessage());
            return false;
        }
    }

    public List<ChiTietHoaDon> getChiTietHoaDonByMaHoaDon(String maHoaDon) {
        return chiTietHoaDonDAO.getChiTietHoaDonByMaHoaDon(maHoaDon);
    }

    public boolean updateChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        return chiTietHoaDonDAO.updateChiTietHoaDon(chiTietHoaDon);
    }

    public boolean deleteChiTietHoaDon(String maHoaDon) {
        return chiTietHoaDonDAO.deleteChiTietHoaDon(maHoaDon);
    }

}
