package pharmacy.bus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.io.exceptions.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import pharmacy.dao.SanPham_DAO;
import pharmacy.entity.SanPham;
import java.util.Date;
import java.text.ParseException;

public class SanPham_BUS {
    private final SanPham_DAO sanPhamDao;

    public SanPham_BUS() throws SQLException {
        this.sanPhamDao = new SanPham_DAO();
    }

    public boolean createSanPham(SanPham sanPham) {
        if (sanPham == null) {
            return false;
        }

        System.out.println(sanPham);

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
                sanPham.getNgayCapNhat().isBefore(sanPham.getNgayTao().atStartOfDay()))) {
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
                && sanPham.getNgayTao() != null && sanPham.getNgayCapNhat().isBefore(sanPham.getNgayTao().atStartOfDay()))) {
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

    public List<SanPham> getSanPhamByMaOrTenSP(String keySearch) {
        return sanPhamDao.getSanPhamByMaOrTenSP(keySearch);
    }

    public List<SanPham> getTop20SanPhamTheoSLTon() {
        return sanPhamDao.getTop20SanPhamTheoSLTon();
    }

    public boolean updateProductStock(String maSanPham, String tenSP, String nsx, int newQuantity, Connection connection) {
        return sanPhamDao.updateProductStock(maSanPham, tenSP, nsx, newQuantity, connection);
    }

//    public void importSanPhamFromCSV(File file) throws FileNotFoundException, java.io.IOException {
//         try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
//             String[] line;
//             csvReader.readNext(); // Bỏ qua dòng tiêu đề

//             while ((line = csvReader.readNext()) != null) {
//                 SanPham sanPham = parseSanPhamFromCSV(line);
//                                 sanPhamDao.createSanPham(sanPham);
//                             }
//                         } catch (IOException | CsvValidationException e) {
//                             e.printStackTrace();
//                         }
//                     }
                
//                     private static SanPham parseSanPhamFromCSV(String[] data) {
//         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//         SanPham sanPham = new SanPham();

//         try {
//             sanPham.setMaSanPham(data[0]);
//             sanPham.setTenSanPham(data[1]);
//             sanPham.setDanhMuc(data[2]);
//             sanPham.setLoaiSanPham(data[3]);
//             Date ngaySXDate = (Date) dateFormat.parse(data[4]);
//             sanPham.setNgaySX(ngaySXDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());            sanPham.setNhaSX(data[5]);
//             Date ngayTaoDate = (Date) dateFormat.parse(data[6]);
//             sanPham.setNgayTao(ngayTaoDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());            sanPham.setSoLuongTon(Integer.parseInt(data[7]));
//             sanPham.setDonGiaBan(Double.parseDouble(data[8]));
//             sanPham.setThue(Float.parseFloat(data[9]));
//             Date hanSuDungDate = (Date) dateFormat.parse(data[10]);
//             sanPham.setHanSuDung(hanSuDungDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());            sanPham.setDonViTinh(data[11]);
//             sanPham.setMoTa(data[12]);
//             sanPham.setTrangThai(data[13]);
//         } catch (ParseException | NumberFormatException e) {
//             e.printStackTrace();
//         }

//         return sanPham;
//     }

//     public class SanPham_BUS {
//     private final SanPham_DAO sanPhamDao = new SanPham_DAO();

    // public Void importSanPhamFromCSV(File file) throws FileNotFoundException, java.io.IOException {
    //     List<SanPham> sanPhamList = new ArrayList<>();
    //     SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    //     try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
    //         String[] line;
    //         csvReader.readNext(); // Bỏ qua dòng tiêu đề

    //         while ((line = csvReader.readNext()) != null) {
    //             SanPham sanPham = new SanPham();
    //             sanPham.setMaSanPham(line[0]);
    //             sanPham.setTenSanPham(line[1]);
    //             sanPham.setDanhMuc(line[2]);
    //             sanPham.setLoaiSanPham(line[3]);
                
    //             try {
    //                 Date ngaySX = dateFormat.parse(line[4]);
    //                 sanPham.setNgaySX(ngaySX.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
    //             } catch (ParseException e) {
    //                 System.out.println("Lỗi định dạng ngày sản xuất: " + line[4]);
    //             }

    //             sanPham.setNhaSX(line[5]);
    //             sanPham.setSoLuongTon(Integer.parseInt(line[7]));
    //             sanPham.setDonGiaBan(Double.parseDouble(line[8]));
    //             sanPham.setThue(Float.parseFloat(line[9]));
    //             try {
    //                 Date hsd = dateFormat.parse(line[10]);
    //                 sanPham.setHanSuDung(hsd.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
    //             } catch (ParseException e) {
    //                 System.out.println("Lỗi định dạng hạn sử dụng: " + line[10]);
    //             }
    //             sanPham.setDonViTinh(line[11]);
    //             sanPham.setMoTa(line[12]);
    //             sanPham.setTrangThai(line[13]);

    //             sanPhamList.add(sanPham);
    //         }

    //         return sanPhamDao.batchInsertSanPham(sanPhamList);
    //         // System.out.println("Đã nhập thành công dữ liệu từ file CSV.");

    //     } catch (IOException | CsvValidationException e) {
    //         System.out.println("Lỗi khi đọc file CSV: " + e.getMessage());
    //     }
    //     return null;
    // }

    public List<SanPham> importSanPhamFromCSV(File file) throws IOException, CsvValidationException, java.io.IOException {
        List<SanPham> sanPhamList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] line;

            while ((line = csvReader.readNext()) != null) {
                SanPham sanPham = new SanPham();
                sanPham.setMaSanPham(line[0]);
                sanPham.setTenSanPham(line[1]);
                sanPham.setDanhMuc(line[2]);
                sanPham.setLoaiSanPham(line[3]);
                try {
                    Date ngaySX = dateFormat.parse(line[4]);
                    sanPham.setNgaySX(ngaySX.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                } catch (ParseException e) {
                    System.out.println("Lỗi định dạng ngày sản xuất: " + line[4]);
                }
                
                sanPham.setNhaSX(line[5]);
                try {
                    Date ngayTao = dateFormat.parse(line[6]);
                    sanPham.setNgayTao(ngayTao.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                } catch (ParseException e) {
                    System.out.println("Lỗi định dạng ngày tạo " + line[6]);
                }
                sanPham.setSoLuongTon(Integer.parseInt(line[7]));
                sanPham.setDonGiaBan(Double.parseDouble(line[8]));
                sanPham.setThue(Float.parseFloat(line[9]));
                try {
                    Date hsd = dateFormat.parse(line[10]);
                    sanPham.setHanSuDung(hsd.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                } catch (ParseException e) {
                    System.out.println("Lỗi định dạng hạn sử dụng " + line[10]);
                }
                sanPham.setDonViTinh(line[11]);
                sanPham.setMoTa(line[12]);
                sanPham.setTrangThai(line[13]);

                sanPhamList.add(sanPham);
            }
        }
        return sanPhamList; // Trả về danh sách sản phẩm để hiển thị trên bảng
    }

    public boolean checkProductExist(String maSanPham, String tenSP, String nsx){
        return sanPhamDao.checkProductExist(maSanPham, tenSP, nsx);
    }

}
