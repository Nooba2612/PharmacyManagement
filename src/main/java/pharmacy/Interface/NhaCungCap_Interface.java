package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.NhaCungCap;

public interface NhaCungCap_Interface {
	boolean createNhaCungCap(NhaCungCap nhaCungCap);

	NhaCungCap getNhaCungCapByMaNCC(String maNCC);

	List<NhaCungCap> getAllNhaCungCap();

	boolean updateNhaCungCap(NhaCungCap nhaCungCap);

	boolean deleteNhaCungCap(String maNCC);
	
	int countNhaCungCap();
}
