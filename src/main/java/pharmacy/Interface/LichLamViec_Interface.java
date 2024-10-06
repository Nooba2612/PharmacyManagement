package pharmacy.Interface;

import pharmacy.entity.LichLamViec;
import java.util.List;

public interface LichLamViec_Interface {
	boolean createLichLamViec(LichLamViec lichLamViec);

	LichLamViec getLichLamViecById(String maLichLamViec);

	List<LichLamViec> getAllLichLamViec();

	boolean updateLichLamViec(LichLamViec lichLamViec);

	boolean deleteLichLamViec(String maLichLamViec);
}
