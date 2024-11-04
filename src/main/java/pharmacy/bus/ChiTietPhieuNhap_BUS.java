package pharmacy.bus;

import java.sql.Connection;
import java.util.List;

import pharmacy.dao.ChiTietPhieuNhap_DAO;
import pharmacy.entity.ChiTietPhieuNhap;
import pharmacy.entity.PhieuNhap;

public class ChiTietPhieuNhap_BUS {
    private final ChiTietPhieuNhap_DAO chiTietPhieuNhapDAO;

    public ChiTietPhieuNhap_BUS() {
        chiTietPhieuNhapDAO = new ChiTietPhieuNhap_DAO();
    }

    public boolean createChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap, Connection connection) {
        return chiTietPhieuNhapDAO.createChiTietPhieuNhap(chiTietPhieuNhap, connection);
    }

    public boolean updateChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
        return chiTietPhieuNhapDAO.updateChiTietPhieuNhap(chiTietPhieuNhap);
    }

    public boolean deleteChiTietPhieuNhap(String maPhieuNhap, String maThuoc) {
        return chiTietPhieuNhapDAO.deleteChiTietPhieuNhap(maPhieuNhap, maThuoc);
    }

    public List<ChiTietPhieuNhap> getChiTietPhieuNhapByMa(PhieuNhap phieuNhap) {
        return chiTietPhieuNhapDAO.getChiTietPhieuNhapByMa(phieuNhap);
    }

    public List<ChiTietPhieuNhap> getAllChiTietPhieuNhap() {
        return chiTietPhieuNhapDAO.getAllChiTietPhieuNhap();
    }
}
