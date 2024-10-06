package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.NhanVien;

public interface NhanVien_Interface {
	boolean createEmployee(NhanVien nhanVien);

	NhanVien getEmployeeByMaNhanVien(String maNhanVien);

	boolean updateEmployee(NhanVien nhanVien);

	boolean deleteEmployee(String maNhanVien);

	List<NhanVien> getAllEmployees();
	
	int countEmployees();
}
