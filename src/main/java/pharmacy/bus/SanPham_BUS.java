package pharmacy.bus;

import java.sql.SQLException;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.List;

import pharmacy.dao.SanPham_DAO;
import pharmacy.entity.SanPham;

public class SanPham_BUS {
    private final SanPham_DAO sanPhamDao;

    public SanPham_BUS() throws SQLException {
        this.sanPhamDao = new SanPham_DAO();
    }

    public boolean createSanPham(SanPham sanPham) {
        if (sanPham == null) {
            return false;
        }

        if (sanPham.getTenSanPham() == null || sanPham.getTenSanPham().trim().isEmpty()) {
            return false;
        }

        if (sanPham.getNhaSX() == null || sanPham.getNhaSX().trim().isEmpty()) {
            return false;
        }

        if (sanPham.getNgaySX() == null || sanPham.getNgaySX().isAfter(LocalDate.now())) {
            return false;
        }

        if (sanPham.getDonViTinh() == null ||
                (!sanPham.getDonViTinh().equals("Viên") &&
                        !sanPham.getDonViTinh().equals("Vỉ") &&
                        !sanPham.getDonViTinh().equals("Hộp") &&
                        !sanPham.getDonViTinh().equals("Chai") &&
                        !sanPham.getDonViTinh().equals("Ống") &&
                        !sanPham.getDonViTinh().equals("Gói"))) {
            return false;
        }

        List<String> danhMucHopLe = Arrays.asList(
                "Giảm đau", "Hạ sốt", "Kháng sinh", "Chống viêm",
                "Vitamin", "An thần", "Siro",
                "Dụng cụ y tế", "Sản phẩm bảo vệ cá nhân", "Dung dịch vệ sinh", "Khác");
        if (sanPham.getDanhMuc() == null || !danhMucHopLe.contains(sanPham.getDanhMuc())) {
            return false;
        }

        if ((sanPham.getNgayCapNhat() == null && sanPham.getNgayTao() == null) ||
                (sanPham.getNgayCapNhat() != null && sanPham.getNgayTao() != null &&
                        sanPham.getNgayCapNhat().isBefore(sanPham.getNgayTao()))) {
            return false;
        }

        if (sanPham.getHanSuDung() == null || sanPham.getNgaySX() == null) {
            return false;
        }

        if (sanPham.getSoLuongTon() < 0) {
            return false;
        }

        if (sanPham.getDonGiaBan() <= 0) {
            return false;
        }

        if (sanPham.getThue() < 0) {
            return false;
        }

        return sanPhamDao.createSanPham(sanPham);
    }

    public SanPham getSanPhamByMaSanPham(String maSanPham) {
        return sanPhamDao.getSanPhamByMaSanPham(maSanPham);
    }

    public boolean updateSanPham(SanPham sanPham) {
        if (sanPham == null) {
            System.out.println("Validation failed: sanPham is null.");
            return false;
        }

        if (sanPham.getTenSanPham() == null || sanPham.getTenSanPham().trim().isEmpty()) {
            System.out.println("Validation failed: Ten san pham is null or empty.");
            return false;
        }

        if (sanPham.getNhaSX() == null || sanPham.getNhaSX().trim().isEmpty()) {
            System.out.println("Validation failed: Nha SX is null or empty.");
            return false;
        }

        if (sanPham.getNgaySX() == null || sanPham.getNgaySX().isAfter(LocalDate.now())) {
            System.out.println("Validation failed: Ngay SX is null or after today's date.");
            return false;
        }

        if (sanPham.getLoaiSanPham() == null && !sanPham.getLoaiSanPham().equals("Thuốc")
                && !sanPham.getLoaiSanPham().equals("Thiết bị y tế")) {
            System.out.println("Validation failed: Loai san pham is invalid.");
            return false;
        }

        if (sanPham.getDonViTinh() == null) {
            return false;
        }

        if (sanPham.getLoaiSanPham().equals("Thuốc")) {
            if (!sanPham.getDonViTinh().equals("Viên") &&
                    !sanPham.getDonViTinh().equals("Vỉ") &&
                    !sanPham.getDonViTinh().equals("Hộp") &&
                    !sanPham.getDonViTinh().equals("Chai") &&
                    !sanPham.getDonViTinh().equals("Ống") &&
                    !sanPham.getDonViTinh().equals("Gói")) {
                System.out.println("Validation failed: Don vi tinh thuoc is invalid.");
                return false;
            }
        } else if (sanPham.getLoaiSanPham().equals("Thiết bị y tế")) {
            if (!sanPham.getDonViTinh().equals("Cái") &&
                    !sanPham.getDonViTinh().equals("Chiếc") &&
                    !sanPham.getDonViTinh().equals("Hộp") &&
                    !sanPham.getDonViTinh().equals("Bộ")) {
                System.out.println("Validation failed: Don vi tinh tbyt is invalid.");
                return false;
            }
        }

        List<String> danhMucHopLe = Arrays.asList(
                "giảm đau", "hạ sốt", "kháng sinh", "chống viêm",
                "vitamin", "an thần", "siro",
                "dụng cụ y tế", "sản phẩm bảo vệ cá nhân", "dung dịch vệ sinh", "khác");
        if (sanPham.getDanhMuc() == null) {
            return false;
        }

        boolean isValidCategory = false;
        for (String category : danhMucHopLe) {
            if (sanPham.getDanhMuc().toLowerCase().trim().contains(category)) {
                isValidCategory = true;
                break;
            }
        }

        if (!isValidCategory) {
            return false;
        }

        if ((sanPham.getNgayCapNhat() == null && sanPham.getNgayTao() == null) || (sanPham.getNgayCapNhat() != null
                && sanPham.getNgayTao() != null && sanPham.getNgayCapNhat().isBefore(sanPham.getNgayTao()))) {
            System.out.println("Validation failed: Ngay cap nhat is before Ngay tao or both are null.");
            return false;
        }

        if (sanPham.getHanSuDung() == null || sanPham.getNgaySX() == null) {
            System.out.println("Validation failed: Han su dung or Ngay SX is null.");
            return false;
        }

        if (sanPham.getSoLuongTon() < 0) {
            System.out.println("Validation failed: So luong ton is less than 0.");
            return false;
        }

        if (sanPham.getDonGiaBan() <= 0) {
            System.out.println("Validation failed: Don gia ban must be greater than 0.");
            return false;
        }

        if (sanPham.getThue() < 0) {
            System.out.println("Validation failed: Thue cannot be less than 0.");
            return false;
        }

        boolean result = sanPhamDao.updateSanPham(sanPham);
        return result;
    }

    public boolean deleteSanPham(String maSanPham) {
        return sanPhamDao.deleteSanPham(maSanPham);
    }

    public List<SanPham> getAllSanPham() {
        return sanPhamDao.getAllSanPham();
    }

    public List<SanPham> getSanPhamSapHetTonKho() {
        return sanPhamDao.getSanPhamSapHetTonKho();
    }

    public List<SanPham> getSanPhamSapHetHanSuDung() {
        return sanPhamDao.getSanPhamSapHetHanSuDung();
    }

    public List<SanPham> getSanPhamDaHetHan() {
        return sanPhamDao.getSanPhamDaHetHan();
    }

    public int countSanPham() {
        return sanPhamDao.countSanPham();
    }

    public int countThuoc() {
        return sanPhamDao.countThuoc();
    }

    public int countThietBiYTe() {
        return sanPhamDao.countThietBiYTe();
    }

    public int countSanPhamSapHetTonKho() {
        return sanPhamDao.countSanPhamSapHetTonKho();
    }

    public int countSanPhamSapHetHanSuDung() {
        return sanPhamDao.countSanPhamSapHetHanSuDung();
    }

    public int countSanPhamDaHetHan() {
        return sanPhamDao.countSanPhamDaHetHan();
    }

    public List<SanPham> getTopSaleSanPhamByDate(String date) {
        return sanPhamDao.getTopSaleSanPhamByDate(date);
    }

    public int getSoldQuantityById(String maSanPham, String date) {
        return sanPhamDao.getSoldQuantityById(maSanPham, date);
    }

    public void refreshSanPham() {
        sanPhamDao.refreshSanPham();
    }

}
