package pharmacy.entity;

import java.time.LocalDate;

public class ThietBiYTe {
    
    private String maThietBi;     
    private String tenThietBi;     
    private LocalDate ngaySX;      
    private String moTa;           
    private int soLuong;           
    private DanhMuc danhMuc;       

    
    public ThietBiYTe() {}

    
    public ThietBiYTe(String maThietBi, String tenThietBi, LocalDate ngaySX, String moTa, int soLuong, DanhMuc danhMuc) {
        setMaThietBi(maThietBi);
        setTenThietBi(tenThietBi);
        setNgaySX(ngaySX);
        setMoTa(moTa);
        setSoLuong(soLuong);
        setDanhMuc(danhMuc);
    }

    
    public ThietBiYTe(ThietBiYTe other) {
        this.maThietBi = other.maThietBi;
        this.tenThietBi = other.tenThietBi;
        this.ngaySX = other.ngaySX;
        this.moTa = other.moTa;
        this.soLuong = other.soLuong;
        this.danhMuc = other.danhMuc;
    }

    
    public String getMaThietBi() {
        return maThietBi;
    }

    public void setMaThietBi(String maThietBi) {
        if (maThietBi == null || maThietBi.isEmpty()) {
            throw new IllegalArgumentException("Mã thiết bị không hợp lệ");
        }
        this.maThietBi = maThietBi;
    }

    public String getTenThietBi() {
        return tenThietBi;
    }

    public void setTenThietBi(String tenThietBi) {
        if (tenThietBi == null || tenThietBi.isEmpty()) {
            throw new IllegalArgumentException("Tên thiết bị không hợp lệ");
        }
        this.tenThietBi = tenThietBi;
    }

    public LocalDate getNgaySX() {
        return ngaySX;
    }

    public void setNgaySX(LocalDate ngaySX) {
        if (ngaySX == null) {
            throw new IllegalArgumentException("Ngày sản xuất không hợp lệ");
        }
        this.ngaySX = ngaySX;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa; 
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không hợp lệ");
        }
        this.soLuong = soLuong;
    }

    public DanhMuc getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(DanhMuc danhMuc) {
        this.danhMuc = danhMuc; // Giá trị danh mục từ Danh Mục
    }

    // Phương thức toString()
    @Override
    public String toString() {
        return "Thiết bị y tế [Mã: " + maThietBi + 
               ", Tên: " + tenThietBi + 
               ", Ngày SX: " + ngaySX + 
               ", Mô tả: " + moTa + 
               ", Số lượng: " + soLuong + 
               ", Danh mục: " + (danhMuc != null ? danhMuc.toString() : "Chưa có") + "]";
    }
}
