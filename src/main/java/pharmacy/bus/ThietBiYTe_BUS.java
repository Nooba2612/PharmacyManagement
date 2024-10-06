package pharmacy.bus;

import pharmacy.dao.ThietBiYTe_DAO;
import pharmacy.entity.ThietBiYTe;
import java.util.List;

public class ThietBiYTe_BUS {
    private ThietBiYTe_DAO thietBiYTeDAO;

    public ThietBiYTe_BUS() {
        this.thietBiYTeDAO = new ThietBiYTe_DAO();
    }

    public boolean createThietBiYTe(ThietBiYTe thietBiYTe) {
        return thietBiYTeDAO.createThietBiYTe(thietBiYTe);
    }

    public ThietBiYTe getThietBiYTeByMaThietBi(String maThietBi) {
        return thietBiYTeDAO.getThietBiYTeByMaThietBi(maThietBi);
    }

    public List<ThietBiYTe> getAllThietBiYTe() {
        return thietBiYTeDAO.getAllThietBiYTe();
    }

    public boolean updateThietBiYTe(ThietBiYTe thietBiYTe) {
        return thietBiYTeDAO.updateThietBiYTe(thietBiYTe);
    }

    public boolean deleteThietBiYTe(String maThietBi) {
        return thietBiYTeDAO.deleteThietBiYTe(maThietBi);
    }

    public int countThietBiYTe() {
        return thietBiYTeDAO.countThietBiYTe();
    }
}
