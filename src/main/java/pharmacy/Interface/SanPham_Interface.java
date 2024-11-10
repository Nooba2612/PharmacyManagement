package pharmacy.Interface;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.List;

import com.itextpdf.io.exceptions.IOException;
import com.opencsv.exceptions.CsvValidationException;

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

	boolean checkProductExist(String maSanPham, String tenSP, String nsx);

	boolean updateProductStock(String maSanPham, String tenSP, String nsx, int newQuantity);

	List<SanPham> importSanPhamFromCSV(File file) throws IOException, CsvValidationException, java.io.IOException;

}
