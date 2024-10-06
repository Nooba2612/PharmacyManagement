package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.Thuoc;

public interface Thuoc_Interface {
	boolean createThuoc(Thuoc thuoc);

	Thuoc getThuocByMaThuoc(String maThuoc);

	boolean updateThuoc(Thuoc thuoc);

	boolean deleteThuoc(String maThuoc);

	List<Thuoc> getAllThuoc();

	int countThuoc();

	List<Thuoc> getThuocSapHetTonKho();

	List<Thuoc> getThuocSapHetHanSuDung();

	int countThuocSapHetTonKho();

	int countThuocSapHetHanSuDung();

	List<Thuoc> getThuocDaHetHan();

	int countThuocDaHetHan();
}
