CREATE DATABASE medkit_pharmacy_management;
DROP DATABASE medkit_pharmacy_management;

USE medkit_pharmacy_management;

CREATE TABLE NhanVien (
    maNhanVien NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (maNhanVien LIKE 'MK____'),
    hoTen NVARCHAR(255) NOT NULL CHECK (hoTen LIKE '%[A-Z]%'),
    chucVu NVARCHAR(255) NOT NULL ,
    soDienThoai CHAR(10) NOT NULL CHECK (LEN(soDienThoai) = 10),
    ngayVaoLam DATETIME NOT NULL,
	namSinh DATETIME NOT NULL,
    trangThai NVARCHAR(50) CHECK (trangThai IN (N'Đang làm', N'Nghỉ việc tạm thời', N'Nghỉ việc hẳn')),
    trinhDo NVARCHAR(50) CHECK (trinhDo IN (N'Cao đẳng', N'Đại học', N'Cao học')),
    gioiTinh NVARCHAR(50) CHECK (gioiTinh IN (N'Nam', N'Nữ', N'Khác'))
);

CREATE TABLE TaiKhoan (
    tenDangNhap NVARCHAR(50) NOT NULL,
    matKhau NVARCHAR(255) NOT NULL,
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (tenDangNhap) REFERENCES NhanVien(maNhanVien)
);

CREATE TABLE Thuoc (
    maThuoc NVARCHAR(50) PRIMARY KEY NOT NULL,
    tenThuoc NVARCHAR(255) NOT NULL,
    ngaySX DATETIME NOT NULL,
    nhaSX NVARCHAR(255) NOT NULL,
    ngayTao DATETIME DEFAULT GETDATE(),
    ngayCapNhat DATETIME DEFAULT GETDATE(),
    soLuongTon INT NOT NULL CHECK (soLuongTon >= 0),
    donGiaBan DECIMAL(10, 2) NOT NULL CHECK (donGiaBan >= 0),
    thue FLOAT NOT NULL CHECK (thue >= 0 AND thue <= 1),
    hanSuDung DATETIME NOT NULL,
    donViTinh NVARCHAR(50) NOT NULL,
	moTa NVARCHAR(255)
);

CREATE TABLE DanhMuc (
    maDM NVARCHAR(50) PRIMARY KEY NOT NULL,
    tenDM NVARCHAR(255) NOT NULL,
    moTa NVARCHAR(255) NOT NULL,
    loaiDM INT NOT NULL,
    maThuoc NVARCHAR(50),
    CONSTRAINT CHK_MaDM CHECK (LEN(maDM) BETWEEN 5 AND 10),
    CONSTRAINT FK_maThuoc FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc)
);

CREATE TABLE KhachHang (
    maKhachHang NVARCHAR(50) PRIMARY KEY NOT NULL,
    hoTen NVARCHAR(255) NOT NULL CHECK (hoTen LIKE '%[A-Z]%'),
    soDienThoai CHAR(10) NOT NULL CHECK (LEN(soDienThoai) = 10),
    namSinh INT CHECK (namSinh >= 1900 AND namSinh <= YEAR(GETDATE())),
    diemTichLuy INT DEFAULT 0,
    ghiChu NVARCHAR(255)
);

CREATE TABLE HoaDon (
    maHoaDon NVARCHAR(50) PRIMARY KEY NOT NULL,
    maKhachHang NVARCHAR(50) NOT NULL,
    maNhanVien NVARCHAR(50) NOT NULL,
    ngayTao DATETIME NOT NULL CHECK (ngayTao = CONVERT(DATETIME, CONVERT(NVARCHAR, ngayTao, 103), 103)),
    tienKhachDua FLOAT NOT NULL CHECK (tienKhachDua >= 0),
    tongTien FLOAT NOT NULL CHECK (tongTien >= 0),
    tienThua FLOAT NOT NULL CHECK (tienThua >= 0),
    diemSuDung FLOAT CHECK (diemSuDung >= 0),
    loaiThanhToan NVARCHAR(50) CHECK (loaiThanhToan IN (N'Tiền mặt', N'Chuyển khoản')),
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang)
);

CREATE TABLE ThietBiYTe (
    maThietBi NVARCHAR(50) PRIMARY KEY,
    tenThietBi NVARCHAR(255) NOT NULL,
    ngaySX DATETIME NOT NULL,
    moTa NVARCHAR(255),
    soLuong INT NOT NULL CHECK (soLuong >= 0),
    maDanhMuc NVARCHAR(50) NOT NULL,
    CONSTRAINT FK_ThietBiYTe_DanhMuc FOREIGN KEY (DanhMuc) REFERENCES NhanVien(maDanhMuc),
);

CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(50) NOT NULL,
    maThuoc NVARCHAR(50) NOT NULL,
    maThietBi NVARCHAR(50),
    soLuong INT NOT NULL CHECK (soLuong >= 0),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc),
    FOREIGN KEY (maThietBi) REFERENCES ThietBiYTe(maThietBi)
);

CREATE TABLE NhaCungCap (
    maNCC NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (maNCC LIKE 'NCC____'),
    tenNCC NVARCHAR(255) NOT NULL,
    soDienThoai NVARCHAR(10) NOT NULL CHECK (LEN(soDienThoai) = 10 AND soDienThoai NOT LIKE '%[^0-9]%'),
    diaChi NVARCHAR(255) NOT NULL,
    email NVARCHAR(255) NOT NULL CHECK (email NOT LIKE '%[^a-zA-Z0-9.@]%')
);

CREATE TABLE PhieuNhap (
    maPhieuNhap NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (maPhieuNhap LIKE 'PN____'),
    maNhanVien NVARCHAR(50) NOT NULL,
    maNhaCungCap NVARCHAR(50) NOT NULL,
    thoiGianNhap DATETIME NOT NULL,
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    FOREIGN KEY (maNhaCungCap) REFERENCES NhaCungCap(maNCC)
);

CREATE TABLE ChiTietPhieuNhap (
    maThietBi NVARCHAR(50) NOT NULL,
    maThuoc NVARCHAR(50) NOT NULL,
    maPhieuNhap NVARCHAR(50) NOT NULL,
    soLuong INT NOT NULL CHECK (soLuong > 0),
    donGia FLOAT NOT NULL CHECK (donGia >= 0),
    thue FLOAT NOT NULL CHECK (thue >= 0),
    FOREIGN KEY (maThietBi) REFERENCES ThietBiYTe(maThietBi),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc),
    FOREIGN KEY (maPhieuNhap) REFERENCES PhieuNhap(maPhieuNhap)
);

CREATE TABLE LichLamViec (
    maLichLamViec NVARCHAR(255) PRIMARY KEY,
    maNhanVien NVARCHAR(50) NOT NULL,
    caLam NVARCHAR(255),
    ngayLam DATETIME,
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);

-- Thêm dữ liệu vào bảng NhanVien
INSERT INTO NhanVien (maNhanVien, hoTen, email, soDienThoai, ngayVaoLam, namSinh, trangThai, trinhDo, gioiTinh) VALUES
('MK0001', N'Nguyễn Văn A', N'van.a@example.com', '0123456789', '2023-01-01', '1990-01-01', N'Đang làm', N'Đại học', N'Nam'),
('MK0002', N'Trần Thị B', N'thi.b@example.com', '0987654321', '2022-05-15', '1995-05-15', N'Nghỉ việc tạm thời', N'Cao đẳng', N'Nữ');


-- Thêm dữ liệu vào bảng TaiKhoan
INSERT INTO TaiKhoan (tenDangNhap, matKhau) VALUES
('MK0001', '$2a$10$T1TakvHHQX3JklzHDNLKK.gB/whHnMsPcZ76yrMNSK.hDbdmILrLm'),
('MK0002', 'password456');


-- Thêm dữ liệu vào bảng Thuoc
INSERT INTO Thuoc (maThuoc, tenThuoc, ngaySX, nhaSX, soLuongTon, donGiaBan, thue, hanSuDung, donViTinh) VALUES
('T0001', N'Thuốc A', '2023-01-01', N'Công ty A', 100, 20000, 0.1, '2025-01-01', N'Viên'),
('T0002', N'Thuốc B', '2022-02-01', N'Công ty B', 50, 30000, 0.2, '2024-02-01', N'Vỉ');

INSERT INTO Thuoc (maThuoc, tenThuoc, ngaySX, nhaSX, soLuongTon, donGiaBan, thue, hanSuDung, donViTinh) VALUES
('T0005', N'Thuốc F', '2023-01-01', N'Công ty A', 10, 20000, 0.1, '2024-10-30', N'Viên');


-- Thêm dữ liệu vào bảng DanhMuc
INSERT INTO DanhMuc (maDM, tenDM, moTa, loaiDM, maThuoc) VALUES
('DM0001', N'Danh mục A', N'Mô tả danh mục A', 1, 'T0001'),
('DM0002', N'Danh mục B', N'Mô tả danh mục B', 2, NULL);

-- Thêm dữ liệu vào bảng KhachHang
INSERT INTO KhachHang (maKhachHang, hoTen, soDienThoai, namSinh, diemTichLuy, ghiChu) VALUES
('KH0001', N'Nguyễn Văn C', '0123456789', 1985, 100, N'Khách hàng thường xuyên'),
('KH0002', N'Trần Thị D', '0987654321', 1990, 50, NULL);

-- Thêm dữ liệu vào bảng HoaDon
INSERT INTO HoaDon (maHoaDon, maKhachHang, maNhanVien, thuoc, ngayTao, tienKhachDua, tongTien, tienThua, diemSuDung, loaiThanhToan) VALUES
('HD0001', 'KH0001', 'MK0001', 'T0001', '2023-10-01', 50000, 45000, 5000, 10, N'Tiền mặt'),
('HD0002', 'KH0002', 'MK0002', 'T0002', '2023-10-02', 60000, 58000, 2000, 0, N'Chuyển khoản');

-- Thêm dữ liệu vào bảng ThietBiYTe
INSERT INTO ThietBiYTe (maThietBi, tenThietBi, ngaySX, moTa, soLuong) VALUES
('TB0001', N'Thiết bị A', '2023-01-01', N'Mô tả thiết bị A', 20),
('TB0002', N'Thiết bị B', '2023-02-01', N'Mô tả thiết bị B', 15);

-- Thêm dữ liệu vào bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, maThietBi, soLuong) VALUES
('HD0001', 'T0001', NULL, 2),
('HD0002', 'T0002', 'TB0001', 1);

-- Thêm dữ liệu vào bảng NhaCungCap
INSERT INTO NhaCungCap (maNCC, tenNCC, soDienThoai, diaChi, email) VALUES
('NCC0001', N'NCC A', '0123456789', N'Địa chỉ NCC A', 'ncc.a@example.com'),
('NCC0002', N'NCC B', '0987654321', N'Địa chỉ NCC B', 'ncc.b@example.com');

-- Thêm dữ liệu vào bảng PhieuNhap
INSERT INTO PhieuNhap (maPhieuNhap, maNhanVien, maNhaCungCap, thoiGianNhap) VALUES
('PN0001', 'MK0001', 'NCC0001', '2023-09-30'),
('PN0002', 'MK0002', 'NCC0002', '2023-10-01');

-- Thêm dữ liệu vào bảng ChiTietPhieuNhap
INSERT INTO ChiTietPhieuNhap (maThietBi, maThuoc, maPhieuNhap, soLuong, donGia, thue) VALUES
('TB0001', 'T0001', 'PN0001', 5, 20000, 0.1),
('TB0002', 'T0002', 'PN0002', 10, 30000, 0.2);

-- Thêm dữ liệu vào bảng LichLamViec
INSERT INTO LichLamViec (maLichLamViec, maNhanVien, caLam, ngayLam) VALUES
('LLV0001', 'MK0001', N'Ca sáng', '2023-10-03'),
('LLV0002', 'MK0002', N'Ca chiều', '2023-10-03');

-- Truy vấn tất cả dữ liệu từ bảng NhanVien
SELECT * FROM NhanVien;

-- Truy vấn tất cả dữ liệu từ bảng TaiKhoan
SELECT * FROM TaiKhoan;

-- Truy vấn tất cả dữ liệu từ bảng Thuoc
SELECT * FROM Thuoc;

-- Truy vấn tất cả dữ liệu từ bảng DanhMuc
SELECT * FROM DanhMuc;

-- Truy vấn tất cả dữ liệu từ bảng KhachHang
SELECT * FROM KhachHang;

-- Truy vấn tất cả dữ liệu từ bảng HoaDon
SELECT * FROM HoaDon;

-- Truy vấn tất cả dữ liệu từ bảng ThietBiYTe
SELECT * FROM ThietBiYTe;

-- Truy vấn tất cả dữ liệu từ bảng ChiTietHoaDon
SELECT * FROM ChiTietHoaDon;

-- Truy vấn tất cả dữ liệu từ bảng NhaCungCap
SELECT * FROM NhaCungCap;

-- Truy vấn tất cả dữ liệu từ bảng PhieuNhap
SELECT * FROM PhieuNhap;

-- Truy vấn tất cả dữ liệu từ bảng ChiTietPhieuNhap
SELECT * FROM ChiTietPhieuNhap;

-- Truy vấn tất cả dữ liệu từ bảng LichLamViec
SELECT * FROM LichLamViec;

-- Truy vấn thông tin nhân viên cùng với tài khoản của họ
SELECT nv.hoTen, tk.tenDangNhap
FROM NhanVien nv
JOIN TaiKhoan tk ON nv.maNhanVien = tk.tenDangNhap;

-- Truy vấn danh sách hóa đơn cùng với thông tin khách hàng
SELECT hd.maHoaDon, kh.hoTen, hd.ngayTao, hd.tongTien
FROM HoaDon hd
JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang;

-- Truy vấn danh sách thuốc và số lượng tồn kho
SELECT tenThuoc, soLuongTon
FROM Thuoc
WHERE soLuongTon > 0;

-- Truy vấn danh sách nhà cung cấp và số lượng thiết bị y tế
SELECT ncc.tenNCC, COUNT(tb.maThietBi) AS soLuongThietBi
FROM NhaCungCap ncc
LEFT JOIN ChiTietPhieuNhap ctpn ON ncc.maNCC = ctpn.maNhaCungCap
LEFT JOIN ThietBiYTe tb ON ctpn.maThietBi = tb.maThietBi
GROUP BY ncc.tenNCC;

-- Truy vấn thông tin lịch làm việc của nhân viên
SELECT nv.hoTen, llv.ngayLam, llv.caLam
FROM NhanVien nv
JOIN LichLamViec llv ON nv.maNhanVien = llv.maNhanVien;
