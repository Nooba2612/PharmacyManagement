package pharmacy.entity;

import java.util.ArrayList;
import java.util.List;

public class DanhMuc {
	private String maDM; 
	private String tenDM; 
	private List<Thuoc> thuoc; 
	private String moTa; 
	private int loaiDM; 

	public DanhMuc() {
		this.thuoc = new ArrayList<>();
	}

	public DanhMuc(String maDM, String tenDM, List<Thuoc> thuoc, String moTa, int loaiDM) {
		this.maDM = maDM;
		this.tenDM = tenDM;
		this.thuoc = thuoc != null ? thuoc : new ArrayList<>();
		this.moTa = moTa;
		this.loaiDM = loaiDM;
	}

	public DanhMuc(DanhMuc original) {
		this.maDM = original.maDM;
		this.tenDM = original.tenDM;
		this.thuoc = new ArrayList<>(original.thuoc);
		this.moTa = original.moTa;
		this.loaiDM = original.loaiDM;
	}

	public void setMaDM(String maDM) {
		if (maDM == null || maDM.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã danh mục không được rỗng");
		}
		this.maDM = maDM;
	}

	public void setTenDM(String tenDM) {
		if (tenDM == null || tenDM.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên danh mục không được rỗng");
		}
		this.tenDM = tenDM;
	}

	public void setThuoc(List<Thuoc> thuoc) {
		if (thuoc == null || thuoc.isEmpty()) {
			throw new IllegalArgumentException("Thuốc không được rỗng");
		}
		this.thuoc = thuoc;
	}

	public void setMoTa(String moTa) {
		if (moTa == null || moTa.trim().isEmpty()) {
			throw new IllegalArgumentException("Mô tả không được rỗng");
		}
		this.moTa = moTa;
	}

	public void setLoaiDM(int loaiDM) {
		this.loaiDM = loaiDM;
	}

	public String getMaDM() {
		return maDM;
	}

	public String getTenDM() {
		return tenDM;
	}

	public List<Thuoc> getThuoc() {
		return thuoc;
	}

	public String getMoTa() {
		return moTa;
	}

	public int getLoaiDM() {
		return loaiDM;
	}

	// 5. Phương thức toString()
	@Override
	public String toString() {
		return "DanhMuc{" + "maDM='" + maDM + '\'' + ", tenDM='" + tenDM + '\'' + ", thuoc=" + thuoc + ", moTa='" + moTa
				+ '\'' + ", loaiDM=" + loaiDM + '}';
	}
}
