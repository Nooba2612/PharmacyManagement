package pharmacy.bus;

import java.time.LocalDate;
import java.util.List;

import pharmacy.dao.NhaCungCap_DAO;
import pharmacy.entity.KhachHang;
import pharmacy.entity.NhaCungCap;
import pharmacy.entity.SanPham;

public class NhaCungCap_BUS {
	private NhaCungCap_DAO nhaCungCapDAO = new NhaCungCap_DAO();

	public boolean createNhaCungCap(NhaCungCap nhaCungCap) {
		return nhaCungCapDAO.createNhaCungCap(nhaCungCap);
	}

	public NhaCungCap getNhaCungCapByMaNCC(String maNCC) {
		return nhaCungCapDAO.getNhaCungCapByMaNCC(maNCC);
	}

	public List<NhaCungCap> getAllNhaCungCap() {
		return nhaCungCapDAO.getAllNhaCungCap();
	}

	public boolean updateNhaCungCap(NhaCungCap nhaCungCap) {
		return nhaCungCapDAO.updateNhaCungCap(nhaCungCap);
	}

	public boolean deleteNhaCungCap(String maNCC) {
		return nhaCungCapDAO.deleteNhaCungCap(maNCC);
	}

	public int countNhaCungCap() {
		return nhaCungCapDAO.countNhaCungCap();
	}

    public boolean createSanPham(NhaCungCap supplier) {
        if (supplier == null){
            return false;
        }

        if (supplier.getTenNCC() == null || supplier.getTenNCC().trim().isEmpty()) {
            return false;
        }

        if (supplier.getDiaChi() == null || supplier.getDiaChi().trim().isEmpty()) {
            return false;
        }

        if (supplier.getSoDienThoai() == null || supplier.getSoDienThoai().trim().isEmpty()) {
            return false;
        }

        if (supplier.getEmail() == null || supplier.getEmail().trim().isEmpty()) {
            return false;
        }
        
        return nhaCungCapDAO.createNhaCungCap(supplier);
    }

	public boolean updateCustomer(NhaCungCap supplier) {
        if (supplier == null) {
            System.out.println("Validation failed: supplier is null.");
            return false;
        }

        if (supplier.getTenNCC() == null || supplier.getTenNCC().trim().isEmpty()) {
            System.out.println("Validation failed: Ho ten is null or empty.");
            return false;
        }

		if (supplier.getDiaChi() == null || supplier.getDiaChi().trim().isEmpty()) {
            System.out.println("Validation failed: Dia chi is null or empty.");
            return false;
        }

		if (supplier.getSoDienThoai() == null || supplier.getSoDienThoai().trim().isEmpty()) {
            System.out.println("Validation failed: So dien thoai is null or empty.");
            return false;
        }

        if (supplier.getEmail() == null || supplier.getEmail().trim().isEmpty()) {
            System.out.println("Validation failed: Email is null or empty.");
            return false;
        }

        // Perform the update using DAO
        boolean result = nhaCungCapDAO.updateNhaCungCap(supplier);
        return result;
    }
}
