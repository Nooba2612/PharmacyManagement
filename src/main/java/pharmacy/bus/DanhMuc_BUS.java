package pharmacy.bus;

import pharmacy.dao.DanhMuc_DAO;
import pharmacy.entity.DanhMuc;

import java.util.List;

public class DanhMuc_BUS {
    private DanhMuc_DAO danhMucDAO;

    public DanhMuc_BUS() {
        danhMucDAO = new DanhMuc_DAO();
    }

    public boolean createDanhMuc(DanhMuc danhMuc) {
        return danhMucDAO.createDanhMuc(danhMuc);
    }

    public DanhMuc getDanhMucByMaDM(String maDM) {
        return danhMucDAO.getDanhMucByMaDM(maDM);
    }

    public boolean updateDanhMuc(DanhMuc danhMuc) {
        return danhMucDAO.updateDanhMuc(danhMuc);
    }

    public boolean deleteDanhMuc(String maDM) {
        return danhMucDAO.deleteDanhMuc(maDM);
    }

    public List<DanhMuc> getAllDanhMuc() {
        return danhMucDAO.getAllDanhMuc();
    }
}
