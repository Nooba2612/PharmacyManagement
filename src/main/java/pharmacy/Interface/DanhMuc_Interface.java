package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.DanhMuc;

public interface DanhMuc_Interface {
	boolean createDanhMuc(DanhMuc danhMuc);

	DanhMuc getDanhMucByMaDM(String maDM);

	boolean updateDanhMuc(DanhMuc danhMuc);

	boolean deleteDanhMuc(String maDM);

	List<DanhMuc> getAllDanhMuc();
}
