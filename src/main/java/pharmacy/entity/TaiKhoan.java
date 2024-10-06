package pharmacy.entity;

public class TaiKhoan {
    private NhanVien tenDangNhap;
    private String matKhau;

    public TaiKhoan() {
    }

    public TaiKhoan(NhanVien tenDangNhap, String matKhau) {
        setTenDangNhap(tenDangNhap);
        setMatKhau(matKhau);
    }

    public TaiKhoan(TaiKhoan original) {
        this.tenDangNhap = original.tenDangNhap;
        this.matKhau = original.matKhau;
    }

    public NhanVien getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(NhanVien tenDangNhap) {
        if (tenDangNhap == null) {
            throw new IllegalArgumentException("Tên đăng nhập không được rỗng.");
        }
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        if (matKhau == null || matKhau.isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được rỗng.");
        }
//        if (matKhau.length() < 8 || 
//            !matKhau.matches(".*[a-z].*") || 
//            !matKhau.matches(".*[A-Z].*") || 
//            !matKhau.matches(".*[0-9].*") || 
//            !matKhau.matches(".*[!@#$%^&*()_+].*")) {
//            throw new IllegalArgumentException("Mật khẩu phải dài tối thiểu 8 ký tự và chứa chữ viết thường, chữ viết hoa, ký tự đặc biệt, và chữ số.");
//        }
        this.matKhau = matKhau;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "tenDangNhap=" + tenDangNhap +
                ", matKhau='" + matKhau + '\'' +
                '}';
    }
}
