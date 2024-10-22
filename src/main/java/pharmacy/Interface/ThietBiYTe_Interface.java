package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.ThietBiYTe;

public interface ThietBiYTe_Interface {
	boolean createThietBiYTe(ThietBiYTe thietBiYTe);

	ThietBiYTe getThietBiYTeByMaThietBi(String maThietBi);

	List<ThietBiYTe> getAllThietBiYTe();

	boolean updateThietBiYTe(ThietBiYTe thietBiYTe);

	boolean deleteThietBiYTe(String maThietBi);

	int countThietBiYTe();

	List<ThietBiYTe> getTopSaleThietBiYTeByDate(String date);

	int getSoldQuantityById(String maThietBi, String date);
}
