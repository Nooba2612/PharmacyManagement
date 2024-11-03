package pharmacy.Interface;

import java.time.LocalDate;
import java.util.List;

import pharmacy.entity.LichLamViec;

public interface LichLamViec_Interface {
	boolean createLichLamViec(LichLamViec lichLamViec);

	LichLamViec getLichLamViecByDateAndShift(LocalDate date, String shift);

	List<LichLamViec> getAllLichLamViec();

	boolean updateLichLamViec(LichLamViec lichLamViec);

	boolean deleteLichLamViec(String maLichLamViec);
}
