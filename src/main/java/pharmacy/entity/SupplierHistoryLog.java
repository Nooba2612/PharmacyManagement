package pharmacy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SupplierHistoryLog {
    private NhaCungCap ncc;
    private NhanVien nv;
    private LocalDateTime ngayCapNhat;

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public SupplierHistoryLog(){
    }
    
    public SupplierHistoryLog(NhaCungCap ncc, NhanVien nv, LocalDateTime ngayCapNhat) {
        this.ncc = ncc;
        this.nv = nv;
        this.ngayCapNhat = ngayCapNhat;
    }

    public NhaCungCap getNcc() {
        return ncc;
    }

    public NhanVien getNv() {
        return nv;
    }

    public void setNcc(NhaCungCap ncc) {
        this.ncc = ncc;
    }

    public void setNv(NhanVien nv) {
        this.nv = nv;
    }

    public String getMaNCC() {
        return ncc != null ? ncc.getMaNCC() : null;
    }

    public String getTenNCC() {
        return ncc != null ? ncc.getTenNCC() : null;
    }

    public String getDiaChi() {
        return ncc != null ? ncc.getDiaChi() : null;
    }

    public String getSoDienThoai() {
        return ncc != null ? ncc.getSoDienThoai() : null;
    }

    public String getEmail() {
        return ncc != null ? ncc.getEmail() : null;
    }

    public String getHoTen() {
        return nv != null ? nv.getHoTen() : null;
    }
}
