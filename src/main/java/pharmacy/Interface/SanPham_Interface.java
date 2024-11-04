package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.SanPham;

public interface SanPham_Interface {
	boolean createSanPham(SanPham thuoc);

	SanPham getSanPhamByMaSanPham(String maSanPham);

	boolean updateSanPham(SanPham thuoc);

	boolean deleteSanPham(String maSanPham);

	List<SanPham> getAllSanPham();

	int countSanPham();

	List<SanPham> getSanPhamSapHetTonKho();

	List<SanPham> getSanPhamSapHetHanSuDung();

	int countSanPhamSapHetTonKho();

	int countSanPhamSapHetHanSuDung();

	List<SanPham> getSanPhamDaHetHan();

	int countSanPhamDaHetHan();

	List<SanPham> getTopSaleSanPhamByDate(String date);

	int getSoldQuantityById(String maSanPham, String date);

	int countThuoc();

	int countThietBiYTe();

	void refreshSanPham();

	List<SanPham> getSanPhamByMaOrTenSP(String keySearch);

	List<SanPham> getTop20SanPhamTheoSLTon();

	boolean updateProductStock(String maSanPham, int newQuantity);

}