package pharmacy.Interface;

import pharmacy.entity.ThietBiYTe;
import java.util.List;

public interface ThietBiYTe_Interface {
	boolean createThietBiYTe(ThietBiYTe thietBiYTe);

	ThietBiYTe getThietBiYTeByMaThietBi(String maThietBi);

	List<ThietBiYTe> getAllThietBiYTe();

	boolean updateThietBiYTe(ThietBiYTe thietBiYTe);

	boolean deleteThietBiYTe(String maThietBi);

	int countThietBiYTe();
}
