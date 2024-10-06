package pharmacy.Interface;

import pharmacy.entity.NhaCungCap;
import java.util.List;

public interface NhaCungCap_Interface {
	boolean createNhaCungCap(NhaCungCap nhaCungCap);

	NhaCungCap getNhaCungCapByMaNCC(String maNCC);

	List<NhaCungCap> getAllNhaCungCap();

	boolean updateNhaCungCap(NhaCungCap nhaCungCap);

	boolean deleteNhaCungCap(String maNCC);
	
	int countNhaCungCap();
}
