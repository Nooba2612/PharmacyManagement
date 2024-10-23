package pharmacy.entity;

public class NhaCungCap {
	private String maNCC;
	private String tenNCC;
	private String soDienThoai;
	private String diaChi;
	private String email;

	public NhaCungCap() {
	}

	public NhaCungCap(String maNCC, String tenNCC, String soDienThoai, String diaChi, String email) {
		setMaNCC(maNCC);
		setTenNCC(tenNCC);
		setSoDienThoai(soDienThoai);
		setDiaChi(diaChi);
		setEmail(email);
	}

	public NhaCungCap(NhaCungCap nhaCungCap) {
		this(nhaCungCap.getMaNCC(), nhaCungCap.getTenNCC(), nhaCungCap.getSoDienThoai(), nhaCungCap.getDiaChi(),
				nhaCungCap.getEmail());
	}

	public String getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(String maNCC) {
		if (maNCC == null || !maNCC.matches("NCC\\d{4}")) {
			throw new IllegalArgumentException("Mã nhà cung cấp không hợp lệ");
		}
		this.maNCC = maNCC;
	}

	public String getTenNCC() {
		return tenNCC;
	}

	public void setTenNCC(String tenNCC) {
		if (tenNCC == null || !Character.isUpperCase(tenNCC.charAt(0))) {
			throw new IllegalArgumentException("Tên nhà cung cấp không hợp lệ");
		}
		this.tenNCC = tenNCC;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		if (soDienThoai == null || soDienThoai.length() != 10 || !soDienThoai.matches("\\d{10}")) {
			throw new IllegalArgumentException("Số điện thoại nhà cung cấp không hợp lệ");
		}
		this.soDienThoai = soDienThoai;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		if (diaChi == null || diaChi.isEmpty()) {
			throw new IllegalArgumentException("Địa chỉ không hợp lệ");
		}
		this.diaChi = diaChi;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null || !email.matches("^[^@.]+@[^@]+\\.[^@]+$")) {
			throw new IllegalArgumentException("Email nhà cung cấp không hợp lệ");
		}
		this.email = email;
	}

	@Override
	public String toString() {
		return "NhaCungCap{" + "maNCC='" + maNCC + '\'' + ", tenNCC='" + tenNCC + '\'' + ", soDienThoai='" + soDienThoai
				+ '\'' + ", diaChi='" + diaChi + '\'' + ", email='" + email + '\'' + '}';
	}
}
