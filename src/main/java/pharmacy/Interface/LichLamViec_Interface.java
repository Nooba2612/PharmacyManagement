package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.LichLamViec;

public interface LichLamViec_Interface {
	boolean createLichLamViec(LichLamViec lichLamViec);

	LichLamViec getLichLamViecById(String maLichLamViec);

	List<LichLamViec> getAllLichLamViec();

	boolean updateLichLamViec(LichLamViec lichLamViec);

	boolean deleteLichLamViec(String maLichLamViec);
}
