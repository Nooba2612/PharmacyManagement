package pharmacy.bus;

import java.util.List;

import pharmacy.dao.ChiTietPhieuNhap_DAO;
import pharmacy.entity.ChiTietPhieuNhap;

public class ChiTietPhieuNhap_BUS {
    private final ChiTietPhieuNhap_DAO chiTietPhieuNhapDAO;

    public ChiTietPhieuNhap_BUS() {
        chiTietPhieuNhapDAO = new ChiTietPhieuNhap_DAO();
    }

    public boolean createChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
        return chiTietPhieuNhapDAO.createChiTietPhieuNhap(chiTietPhieuNhap);
    }

    public boolean updateChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
        return chiTietPhieuNhapDAO.updateChiTietPhieuNhap(chiTietPhieuNhap);
    }

    public boolean deleteChiTietPhieuNhap(String maPhieuNhap, String maThuoc) {
        return chiTietPhieuNhapDAO.deleteChiTietPhieuNhap(maPhieuNhap, maThuoc);
    }

    public List<ChiTietPhieuNhap> getChiTietPhieuNhapByMa(String maPhieuNhap) {
        return chiTietPhieuNhapDAO.getChiTietPhieuNhapByMa(maPhieuNhap);
    }

    public List<ChiTietPhieuNhap> getAllChiTietPhieuNhap() {
        return chiTietPhieuNhapDAO.getAllChiTietPhieuNhap();
    }
}
