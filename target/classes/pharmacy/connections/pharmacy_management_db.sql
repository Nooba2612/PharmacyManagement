create database medkit_pharmacy_management
use medkit_pharmacy_management
-- Nhân Viên
CREATE TABLE NhanVien (
    maNhanVien NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (maNhanVien LIKE 'MK____'),
    hoTen NVARCHAR(255) NOT NULL CHECK (hoTen LIKE '%[A-Z]%'),
    chucVu NVARCHAR(255) NOT NULL,
    soDienThoai NVARCHAR(10) NOT NULL UNIQUE,
    email NVARCHAR(225) NOT NULL CHECK (email LIKE '%_@gmail.com'),
    ngayVaoLam DATETIME NOT NULL,
    namSinh DATE NOT NULL CHECK (YEAR(GETDATE()) - YEAR(namSinh) >= 22),
    trangThai NVARCHAR(50) CHECK (
        trangThai IN (N'Còn làm việc', N'Nghỉ phép', N'Đã nghỉ việc')
    ),
    trinhDo NVARCHAR(50) CHECK (trinhDo IN (N'Cao đẳng', N'Đại học', N'Cao học')),
    gioiTinh NVARCHAR(50) CHECK (gioiTinh IN (N'Nam', N'Nữ', N'Khác')),
    cccd NCHAR(12) NOT NULL,
    tienLuong FLOAT NOT NULL
);

-- Tài khoản
CREATE TABLE TaiKhoan (
    tenDangNhap NVARCHAR(50) NOT NULL UNIQUE,
    matKhau NVARCHAR(255) NOT NULL,
    isLoggedIn BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (tenDangNhap) REFERENCES NhanVien(maNhanVien)
);
-- ISLOGGEDIN : 1
UPDATE TaiKhoan
SET isLoggedIn = 1
WHERE tenDangNhap = 'MK0001' -- Sản Phẩm
CREATE TABLE SanPham (
    maSanPham NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (maSanPham LIKE 'SP____'),
    tenSanPham NVARCHAR(255) NOT NULL,
    danhMuc NVARCHAR(50) NOT NULL,
    loaiSanPham NVARCHAR(50) NOT NULL,
    ngaySX DATETIME NOT NULL,
    nhaSX NVARCHAR(255) NOT NULL,
    ngayTao DATETIME DEFAULT GETDATE(),
    ngayCapNhat DATETIME DEFAULT GETDATE(),
    soLuongTon INT NOT NULL CHECK (soLuongTon >= 0),
    donGiaBan FLOAT NOT NULL CHECK (donGiaBan > 0),
    thue FLOAT NOT NULL CHECK (
        thue >= 0
        AND thue <= 1
    ),
    hanSuDung DATETIME NOT NULL,
    donViTinh NVARCHAR(50) NOT NULL CHECK (
        donViTinh IN (
            N'Cái',
            N'Chiếc',
            N'Bộ',
            N'Hộp',
            N'Gói',
            N'Viên',
            N'Vỉ',
            N'Chai',
            N'Ống',
            N'Gói'
        )
    ),
    moTa NVARCHAR(255),
    trangThai NVARCHAR(50) NOT NULL DEFAULT N'Có sẵn'
);


CREATE TABLE NhatKyThayDoiNhanVien (
	maNhanVien NVARCHAR(50) NOT NULL,
    hoTen NVARCHAR(255) NOT NULL CHECK (hoTen LIKE '%[A-Z]%'),
    chucVu NVARCHAR(255) NOT NULL,
    soDienThoai NVARCHAR(10) NOT NULL,
    email NVARCHAR(225) NOT NULL CHECK (email LIKE '%_@gmail.com'),
    ngayVaoLam DATETIME NOT NULL,
    namSinh DATE NOT NULL CHECK (YEAR(GETDATE()) - YEAR(namSinh) >= 22),
    trangThai NVARCHAR(50) CHECK (
        trangThai IN (N'Còn làm việc', N'Nghỉ phép', N'Đã nghỉ việc')
    ),
    trinhDo NVARCHAR(50) CHECK (trinhDo IN (N'Cao đẳng', N'Đại học', N'Cao học')),
    gioiTinh NVARCHAR(50) CHECK (gioiTinh IN (N'Nam', N'Nữ', N'Khác')),
    cccd NCHAR(12) NOT NULL,
    tienLuong FLOAT NOT NULL,
	ngayCapNhat DATETIME NOT NULL,
	maNhanVienDaSua NVARCHAR(50) NOT NULL,
	CONSTRAINT FK_NhatKyThayDoiNhanVien_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
)

CREATE TABLE NhatKyThayDoiSanPham (
    maSanPham NVARCHAR(50) NOT NULL,
    tenSanPham NVARCHAR(255) NOT NULL,
    danhMuc NVARCHAR(50) NOT NULL,
    loaiSanPham NVARCHAR(50) NOT NULL,
    ngaySX DATETIME NOT NULL,
    nhaSX NVARCHAR(255) NOT NULL,
    ngayTao DATETIME DEFAULT GETDATE(),
    ngayCapNhat DATETIME DEFAULT GETDATE(),
    soLuongTon INT NOT NULL CHECK (soLuongTon >= 0),
    donGiaBan FLOAT NOT NULL CHECK (donGiaBan > 0),
    thue FLOAT NOT NULL CHECK (
        thue >= 0
        AND thue <= 1
    ),
    hanSuDung DATETIME NOT NULL,
    donViTinh NVARCHAR(50) NOT NULL CHECK (
        donViTinh IN (
            N'Cái',
            N'Chiếc',
            N'Bộ',
            N'Hộp',
            N'Gói',
            N'Viên',
            N'Vỉ',
            N'Chai',
            N'Ống',
            N'Gói'
        )
    ),
    moTa NVARCHAR(255),
    trangThai NVARCHAR(50) NOT NULL DEFAULT N'Có sẵn',
	maNhanVien NVARCHAR(50) NOT NULL,
	CONSTRAINT FK_NhatKyThayDoiSanPham_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);


-- Cập nhật trạng thái sản phẩm
/*
 -- Hết hạn
 UPDATE SanPham
 SET trangThai = N'Hết hạn'
 WHERE hanSuDung < GETDATE();
 
 -- Hết hàng
 UPDATE SanPham
 SET trangThai = N'Hết hàng'
 WHERE soLuongTon = 0;
 
 -- Sắp hết hạn (còn dưới 30 ngày)
 UPDATE SanPham
 SET trangThai = N'Sắp hết hạn'
 WHERE DATEDIFF(day, GETDATE(), hanSuDung) <= 30 AND hanSuDung >= GETDATE();
 
 -- Tồn kho thấp (số lượng tồn dưới 10)
 UPDATE SanPham
 SET trangThai = N'Tồn kho thấp'
 WHERE soLuongTon < 10 AND soLuongTon > 0 AND hanSuDung >= GETDATE();
 */
-- Khách Hàng
CREATE TABLE KhachHang (
    maKhachHang NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (maKhachHang LIKE 'KH____'),
    hoTen NVARCHAR(255) NOT NULL CHECK (hoTen LIKE '%[A-Z]%'),
    soDienThoai CHAR(10) NOT NULL UNIQUE CHECK (LEN(soDienThoai) = 10),
    namSinh DATE,
    diemTichLuy INT DEFAULT 0,
    gioiTinh NVARCHAR(15) CHECK (gioiTinh IN (N'Nam', N'Nữ', N'Khác')),
    ghiChu NVARCHAR(255)
);
CREATE TABLE NhatKyThayDoiKhachHang (
    maKhachHang NVARCHAR(50) NOT NULL,
    hoTen NVARCHAR(255) NOT NULL CHECK (hoTen LIKE '%[A-Z]%'),
    soDienThoai CHAR(10) NOT NULL CHECK (LEN(soDienThoai) = 10),
    namSinh DATE,
    diemTichLuy INT DEFAULT 0,
    gioiTinh NVARCHAR(15) CHECK (gioiTinh IN (N'Nam', N'Nữ', N'Khác')),
    ghiChu NVARCHAR(255),
    ngayCapNhat DATETIME NOT NULL,
    maNhanVien NVARCHAR(50) NOT NULL,
	CONSTRAINT FK_NhatKyThayDoiKhachHang_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);

CREATE TABLE HoaDon (
    maHoaDon NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (MaHoaDon LIKE 'HD____'),
    maKhachHang NVARCHAR(50) NOT NULL,
    maNhanVien NVARCHAR(50) NOT NULL,
    ngayTao DATETIME NOT NULL,
    tienKhachDua FLOAT NOT NULL CHECK (tienKhachDua > 0),
    tongTien FLOAT NOT NULL CHECK (tongTien > 0),
    tienThua FLOAT NOT NULL CHECK (tienThua > 0),
    diemSuDung FLOAT CHECK (diemSuDung >= 0),
    loaiThanhToan NVARCHAR(50) CHECK (loaiThanhToan IN (N'Tiền mặt', N'Chuyển khoản')),
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang)
);

alter table HoaDon add isTemp int default 0

CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(50) NOT NULL,
    maSanPham NVARCHAR(50) NOT NULL,
    soLuong INT NOT NULL CHECK (soLuong > 0),
    donGiaNhap FLOAT NOT NULL CHECK (donGiaNhap > 0),
    thue FLOAT NOT NULL CHECK (
        thue >= 0
        AND thue <= 1
    ),
	lieuLuong NVARCHAR(255),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham),
    PRIMARY KEY (maHoaDon, maSanPham)
);
CREATE TABLE NhaCungCap (
    maNCC NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (maNCC LIKE 'NCC____'),
    tenNCC NVARCHAR(255) NOT NULL,
    soDienThoai NVARCHAR(10) NOT NULL CHECK (
        LEN(soDienThoai) = 10
        AND soDienThoai NOT LIKE '%[^0-9]%'
    ),
    diaChi NVARCHAR(255) NOT NULL,
    email NVARCHAR(225) NOT NULL CHECK (email LIKE '%_@gmail.com'),
);
CREATE TABLE NhatKyThayDoiNhaCungCap (
    maNCC NVARCHAR(50) NOT NULL,
    tenNCC NVARCHAR(255) NOT NULL,
    soDienThoai NVARCHAR(10) NOT NULL CHECK (
        LEN(soDienThoai) = 10
        AND soDienThoai NOT LIKE '%[^0-9]%'
    ),
    diaChi NVARCHAR(255) NOT NULL,
    email NVARCHAR(225) NOT NULL CHECK (email LIKE '%_@gmail.com'),
    ngayTao DATE,
    ngayCapNhat DATETIME NOT NULL,
    maNhanVien NVARCHAR(50) NOT NULL,
	CONSTRAINT FK_NhatKyThayDoiNhaCungCap_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
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
    maSanPham NVARCHAR(50) NOT NULL,
    maPhieuNhap NVARCHAR(50) NOT NULL,
    soLuong INT NOT NULL CHECK (soLuong > 0),
    donGia FLOAT NOT NULL CHECK (donGia > 0),
    thue FLOAT NOT NULL CHECK (
        thue >= 0
        AND thue <= 1
    ),
    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham),
    FOREIGN KEY (maPhieuNhap) REFERENCES PhieuNhap(maPhieuNhap),
    PRIMARY KEY (maSanPham, maPhieuNhap)
);
CREATE TABLE LichLamViec (
    maLichLamViec NVARCHAR(255) PRIMARY KEY,
    maNhanVien NVARCHAR(50) NOT NULL,
    caLam NVARCHAR(255),
    ngayLam DATETIME,
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);
-- Nhân viên
INSERT INTO NhanVien (
    maNhanVien,
    hoTen,
    chucVu,
    soDienThoai,
    email,
    ngayVaoLam,
    namSinh,
    trangThai,
    trinhDo,
    gioiTinh,
    cccd,
    tienLuong
) VALUES
    ('MK0001', N'Đặng Nam Kỳ', N'Nhân viên', '0912345671', N'kydan1@gmail.com', '2020-06-01', '2000-03-15', N'Còn làm việc', N'Đại học', N'Nam', '079200002212', 7000000),
    ('MK0002', N'Đoàn Băng Băng', N'Nhân viên', '0912345672', N'bangbang@gmail.com', '2019-08-12', '1999-08-22', N'Còn làm việc', N'Cao đẳng', N'Nữ', '080299035112', 6500000),
    ('MK0003', N'Nguyễn Hữu Sang', N'Nhân viên', '0912345673', N'sang3007@gmail.com', '2021-05-03', '1998-02-10', N'Nghỉ phép', N'Đại học', N'Nam', '080298005512', 6800000),
    ('MK0004', N'Dăm Bụt', N'Nhân viên', '0912345674', N'phamthid@gmail.com', '2021-10-22', '1997-06-30', N'Còn làm việc', N'Cao đẳng', N'Nữ', '080297000132', 7000000),
    ('MK0005', N'Phạm Văn Đồng', N'Nhân viên', '0912345675', N'dovane@gmail.com', '2022-01-10', '1996-09-12', N'Nghỉ phép', N'Cao học', N'Nam', '079296008012', 7200000),
    ('MK0006', N'Thiên Thiên', N'Nhân viên', '0912345676', N'thienthein@gmail.com', '2023-03-15', '1995-12-05', N'Còn làm việc', N'Đại học', N'Nữ', '080295000597', 7100000),
    ('MK0007', N'Cao Kỳ Luân', N'Nhân viên', '0912345677', N'caoluan001@gmail.com', '2018-07-19', '1994-10-25', N'Đã nghỉ việc', N'Cao học', N'Nam', '079294008812', 7500000),
    ('MK0008', N'Kỳ Kỳ', N'Nhân viên', '0912345678', N'kkkkk1@gmail.com', '2017-04-30', '1993-04-17', N'Còn làm việc', N'Đại học', N'Nữ', '079293025912', 6900000),
    ('MK0009', N'Đặng Phúc Nguyên', N'Quản lý', '0912345679', 'nguyendang0100@gmail.com', '2015-09-10', '1992-05-10', N'Còn làm việc', N'Cao học', N'Nam', '080204000112', 10000000),
    ('MK0010', N'Nguyễn Phan Minh Mẫn', N'Quản lý', '0912345680', 'manminh992@gmail.com', '2016-11-20', '1991-03-28', N'Còn làm việc', N'Đại học', N'Nam', '080204000512', 10500000);

-- Tài khoản
-- Mật khẩu: 123456
select * from TaiKhoan
update TaiKhoan set isLoggedIn = 1
update TaiKhoan set matKhau = '$2a$10$T1TakvHHQX3JklzHDNLKK.gB/whHnMsPcZ76yrMNSK.hDbdmILrLm'
INSERT INTO TaiKhoan (tenDangNhap, matKhau)
VALUES (
        'MK0001',
        '$2a$10$T1TakvHHGfbcU38U34gCiuKHz8eFwxU5f6d/G7SuwgEQUHga4ubKC'
    ),
    (
        'MK0002',
        '$2a$10$T1TakvHHGfbcU38U34gCiuKHz8eFwxU5f6d/G7SuwgEQUHga4ubKC'
    ),
    (
        'MK0003',
        '$2a$10$T1TakvHHGfbcU38U34gCiuKHz8eFwxU5f6d/G7SuwgEQUHga4ubKC'
    ),
    (
        'MK0004',
        '$2a$10$T1TakvHHGfbcU38U34gCiuKHz8eFwxU5f6d/G7SuwgEQUHga4ubKC'
    ),
    (
        'MK0005',
        '$2a$10$T1TakvHHGfbcU38U34gCiuKHz8eFwxU5f6d/G7SuwgEQUHga4ubKC'
    );
-- Sản phẩm

INSERT INTO SanPham (
        maSanPham,
        tenSanPham,
        danhMuc,
        loaiSanPham,
        ngaySX,
        nhaSX,
        soLuongTon,
        donGiaBan,
        thue,
        hanSuDung,
        donViTinh,
        moTa,
        trangThai
    )
VALUES 
    ('SP0001', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Tâm Anh', 50, 10000, 0.08, '2025-01-01', N'Viên', N'Thuốc Paracetamol 500mg giúp giảm đau và hạ sốt hiệu quả cho cả trẻ em và người lớn.', N'Còn hàng'),
    ('SP0002', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Hà Tây', 30, 15000, 0.08, '2025-02-01', N'Viên', N'Kháng sinh Amoxicillin 500mg hỗ trợ điều trị nhiễm khuẩn do vi khuẩn nhạy cảm.', N'Còn hàng'),
    ('SP0003', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Hải Dương', 5, 12000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen 400mg giúp giảm đau, giảm sốt và chống viêm hiệu quả.', N'Hết hàng'),
    ('SP0004', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Tân Hiệp Phát', 20, 8000, 0.08, '2025-04-01', N'Viên', N'Vitamin C 1000mg hỗ trợ tăng cường hệ miễn dịch và chống oxi hóa.', N'Còn hàng'),
    ('SP0005', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Mediplantex', 10, 20000, 0.08, '2025-05-01', N'Viên', N'Diazepam 5mg giúp an thần và giảm lo âu, thường được dùng trong các trường hợp căng thẳng.', N'Còn hàng'),
    ('SP0006', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược FPT', 15, 50000, 0.08, '2025-01-01', N'Vỉ', N'Thuốc Paracetamol 500mg giúp giảm đau và hạ sốt hiệu quả cho cả trẻ em và người lớn.', N'Còn hàng'),
    ('SP0007', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Trung ương 3', 25, 75000, 0.08, '2025-02-01', N'Vỉ', N'Kháng sinh Amoxicillin 500mg hỗ trợ điều trị nhiễm khuẩn do vi khuẩn nhạy cảm.', N'Còn hàng'),
    ('SP0008', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Phương Đông', 10, 60000, 0.08, '2025-03-01', N'Vỉ', N'Ibuprofen 400mg giúp giảm đau, giảm sốt và chống viêm hiệu quả.', N'Hết hàng'),
    ('SP0009', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Nam Hà', 50, 40000, 0.08, '2025-04-01', N'Vỉ', N'Vitamin C 1000mg hỗ trợ tăng cường hệ miễn dịch và chống oxi hóa.', N'Còn hàng'),
    ('SP0010', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Vinpharm', 15, 25000, 0.08, '2025-05-01', N'Viên', N'Diazepam 5mg giúp an thần và giảm lo âu, giúp điều trị mất ngủ và lo âu.', N'Còn hàng'),
    ('SP0011', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Đạt Vi Phú', 40, 15000, 0.08, '2025-01-01', N'Viên', N'Thuốc Paracetamol 500mg giúp giảm đau và hạ sốt hiệu quả cho cả trẻ em và người lớn.', N'Còn hàng'),
    ('SP0012', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Bình Định', 35, 20000, 0.08, '2025-02-01', N'Viên', N'Kháng sinh Amoxicillin 500mg hỗ trợ điều trị nhiễm khuẩn do vi khuẩn nhạy cảm.', N'Còn hàng'),
    ('SP0013', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Hoàng Anh', 20, 15000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen 400mg giúp giảm đau, giảm sốt và chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0014', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Việt Nam', 30, 12000, 0.08, '2025-04-01', N'Viên', N'Vitamin C 1000mg giúp tăng cường sức đề kháng, hỗ trợ chống oxi hóa.', N'Còn hàng'),
    ('SP0015', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Vĩnh Phúc', 25, 22000, 0.08, '2025-05-01', N'Viên', N'Diazepam 5mg hỗ trợ điều trị căng thẳng, lo âu, và giúp ngủ ngon.', N'Còn hàng'),
    ('SP0016', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Hậu Giang', 60, 11000, 0.08, '2025-01-01', N'Viên', N'Paracetamol 500mg giúp giảm đau và hạ sốt hiệu quả cho cả trẻ em và người lớn.', N'Còn hàng'),
    ('SP0017', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Lâm Đồng', 45, 18000, 0.08, '2025-02-01', N'Viên', N'Kháng sinh Amoxicillin giúp điều trị nhiễm khuẩn hiệu quả.', N'Còn hàng'),
    ('SP0018', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Đông Dược', 25, 18000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen 400mg giúp giảm đau, hạ sốt và chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0019', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Cửu Long', 50, 9500, 0.08, '2025-04-01', N'Viên', N'Vitamin C giúp tăng cường miễn dịch và chống oxi hóa hiệu quả.', N'Còn hàng'),
    ('SP0020', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Quảng Ninh', 10, 23000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp điều trị lo âu và rối loạn giấc ngủ.', N'Còn hàng'),
	 ('SP0021', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Sài Gòn', 55, 10000, 0.08, '2025-01-01', N'Viên', N'Thuốc Paracetamol 500mg giúp giảm đau và hạ sốt hiệu quả cho cả trẻ em và người lớn.', N'Còn hàng'),
    ('SP0022', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Hòa Bình', 35, 13000, 0.08, '2025-02-01', N'Viên', N'Amoxicillin 500mg giúp điều trị các bệnh nhiễm khuẩn do vi khuẩn nhạy cảm.', N'Còn hàng'),
    ('SP0023', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Tây Nguyên', 25, 14000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen 400mg giúp giảm đau và chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0024', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Tiền Giang', 45, 10000, 0.08, '2025-04-01', N'Viên', N'Vitamin C 1000mg giúp tăng cường miễn dịch và chống oxi hóa.', N'Còn hàng'),
    ('SP0025', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Bình Phước', 40, 25000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp giảm lo âu và giúp ngủ ngon.', N'Còn hàng'),
    ('SP0026', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Vĩnh Long', 60, 11000, 0.08, '2025-01-01', N'Viên', N'Paracetamol giúp giảm đau và hạ sốt cho cả trẻ em và người lớn.', N'Còn hàng'),
    ('SP0027', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Bắc Giang', 30, 17000, 0.08, '2025-02-01', N'Viên', N'Kháng sinh Amoxicillin hỗ trợ điều trị nhiễm khuẩn hiệu quả.', N'Còn hàng'),
    ('SP0028', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Bình Dương', 50, 12000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen 400mg giảm đau, hạ sốt và chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0029', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Đồng Nai', 35, 9000, 0.08, '2025-04-01', N'Viên', N'Vitamin C giúp tăng cường sức đề kháng và chống oxi hóa.', N'Còn hàng'),
    ('SP0030', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Hà Nam', 20, 23000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp an thần, điều trị lo âu và mất ngủ.', N'Còn hàng'),
    ('SP0031', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược An Giang', 70, 10000, 0.08, '2025-01-01', N'Viên', N'Thuốc Paracetamol giúp giảm đau, hạ sốt cho mọi lứa tuổi.', N'Còn hàng'),
    ('SP0032', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Quảng Bình', 40, 15000, 0.08, '2025-02-01', N'Viên', N'Amoxicillin giúp điều trị nhiễm khuẩn do vi khuẩn nhạy cảm.', N'Còn hàng'),
    ('SP0033', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Sơn La', 25, 13000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen giúp giảm đau, chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0034', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Hải Phòng', 50, 9500, 0.08, '2025-04-01', N'Viên', N'Vitamin C giúp tăng cường miễn dịch và chống oxi hóa.', N'Còn hàng'),
    ('SP0035', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Cần Thơ', 30, 22000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp điều trị lo âu, căng thẳng và mất ngủ.', N'Còn hàng'),
    ('SP0036', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược TP HCM', 65, 10500, 0.08, '2025-01-01', N'Viên', N'Paracetamol 500mg giúp giảm đau, hạ sốt cho trẻ em và người lớn.', N'Còn hàng'),
    ('SP0037', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Phú Yên', 20, 16000, 0.08, '2025-02-01', N'Viên', N'Amoxicillin giúp điều trị nhiễm khuẩn hiệu quả.', N'Còn hàng'),
    ('SP0038', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Nghệ An', 40, 14000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen giúp giảm đau và chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0039', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Thanh Hóa', 50, 10500, 0.08, '2025-04-01', N'Viên', N'Vitamin C giúp tăng cường hệ miễn dịch và chống oxi hóa.', N'Còn hàng'),
    ('SP0040', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Hà Nội', 45, 25000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp giảm lo âu và giúp ngủ ngon.', N'Còn hàng'),
    ('SP0041', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Vĩnh Phúc', 50, 12000, 0.08, '2025-01-01', N'Viên', N'Paracetamol 500mg hỗ trợ giảm đau, hạ sốt hiệu quả.', N'Còn hàng'),
    ('SP0042', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Quảng Trị', 35, 15500, 0.08, '2025-02-01', N'Viên', N'Amoxicillin hỗ trợ điều trị các bệnh nhiễm khuẩn hiệu quả.', N'Còn hàng'),
	  ('SP0043', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Thái Bình', 55, 12500, 0.08, '2025-03-01', N'Viên', N'Ibuprofen 400mg giảm đau, hạ sốt và chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0044', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Tuyên Quang', 60, 9500, 0.08, '2025-04-01', N'Viên', N'Vitamin C 1000mg hỗ trợ tăng cường sức đề kháng và chống oxi hóa.', N'Còn hàng'),
    ('SP0045', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Nghệ An', 70, 23000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp giảm lo âu và trị mất ngủ.', N'Còn hàng'),
    ('SP0046', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Hải Dương', 80, 11000, 0.08, '2025-01-01', N'Viên', N'Paracetamol 500mg giúp giảm đau, hạ sốt cho người lớn và trẻ em.', N'Còn hàng'),
    ('SP0047', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Quảng Ninh', 45, 16000, 0.08, '2025-02-01', N'Viên', N'Amoxicillin 500mg hỗ trợ điều trị nhiễm khuẩn.', N'Còn hàng'),
    ('SP0048', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Hà Giang', 65, 13500, 0.08, '2025-03-01', N'Viên', N'Ibuprofen 400mg giúp giảm đau, chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0049', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Cao Bằng', 40, 10500, 0.08, '2025-04-01', N'Viên', N'Vitamin C giúp cải thiện sức khỏe và tăng cường miễn dịch.', N'Còn hàng'),
    ('SP0050', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Nam Định', 50, 22000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp giảm lo âu, căng thẳng và hỗ trợ giấc ngủ.', N'Còn hàng'),
    ('SP0051', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Bình Thuận', 75, 11500, 0.08, '2025-01-01', N'Viên', N'Paracetamol 500mg giúp giảm đau, hạ sốt hiệu quả.', N'Còn hàng'),
    ('SP0052', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Hà Tây', 55, 15500, 0.08, '2025-02-01', N'Viên', N'Amoxicillin 500mg điều trị các bệnh nhiễm khuẩn do vi khuẩn nhạy cảm.', N'Còn hàng'),
    ('SP0053', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Bình Dương', 60, 12500, 0.08, '2025-03-01', N'Viên', N'Ibuprofen giảm đau, hạ sốt và chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0054', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Hưng Yên', 50, 9900, 0.08, '2025-04-01', N'Viên', N'Vitamin C hỗ trợ tăng cường sức đề kháng và chống oxi hóa.', N'Còn hàng'),
    ('SP0055', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Bắc Giang', 40, 24000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp giảm lo âu và cải thiện giấc ngủ.', N'Còn hàng'),
    ('SP0056', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Long An', 65, 10000, 0.08, '2025-01-01', N'Viên', N'Paracetamol giúp giảm đau và hạ sốt nhanh chóng.', N'Còn hàng'),
    ('SP0057', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Lâm Đồng', 55, 14500, 0.08, '2025-02-01', N'Viên', N'Amoxicillin giúp điều trị nhiễm khuẩn hiệu quả.', N'Còn hàng'),
    ('SP0058', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Quảng Bình', 45, 12000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen 400mg giúp giảm đau, chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0059', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Lạng Sơn', 60, 9800, 0.08, '2025-04-01', N'Viên', N'Vitamin C giúp hỗ trợ miễn dịch và chống oxi hóa.', N'Còn hàng'),
    ('SP0060', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Vĩnh Long', 40, 21000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp giảm lo âu và cải thiện chất lượng giấc ngủ.', N'Còn hàng'),
    ('SP0061', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Cà Mau', 50, 10500, 0.08, '2025-01-01', N'Viên', N'Paracetamol giảm đau, hạ sốt cho trẻ em và người lớn.', N'Còn hàng'),
    ('SP0062', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Bà Rịa', 30, 15000, 0.08, '2025-02-01', N'Viên', N'Amoxicillin điều trị nhiễm khuẩn do vi khuẩn nhạy cảm.', N'Còn hàng'),
    ('SP0063', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Tây Ninh', 60, 12000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen giúp giảm đau, chống viêm hiệu quả.', N'Còn hàng'),
    ('SP0064', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Đắk Lắk', 50, 10000, 0.08, '2025-04-01', N'Viên', N'Vitamin C giúp chống lại các bệnh nhiễm trùng và cải thiện sức khỏe.', N'Còn hàng'),
	('SP0065', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Quảng Ngãi', 45, 25000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp giảm lo âu và cải thiện giấc ngủ.', N'Còn hàng'),
    ('SP0066', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Đà Nẵng', 55, 11500, 0.08, '2025-01-01', N'Viên', N'Paracetamol giảm đau, hạ sốt nhanh chóng cho người lớn và trẻ em.', N'Còn hàng'),
    ('SP0067', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Sóc Trăng', 50, 14000, 0.08, '2025-02-01', N'Viên', N'Amoxicillin hỗ trợ điều trị các bệnh nhiễm khuẩn phổ biến.', N'Còn hàng'),
    ('SP0068', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Vĩnh Phúc', 60, 12500, 0.08, '2025-03-01', N'Viên', N'Ibuprofen giảm đau và chống viêm hiệu quả cho các trường hợp cảm cúm, đau cơ thể.', N'Còn hàng'),
    ('SP0069', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược An Giang', 70, 9900, 0.08, '2025-04-01', N'Viên', N'Vitamin C giúp tăng cường sức đề kháng và chống oxi hóa.', N'Còn hàng'),
    ('SP0070', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Thái Nguyên', 50, 23000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp giảm lo âu và giúp cải thiện giấc ngủ cho người lớn.', N'Còn hàng'),
    ('SP0071', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Bắc Ninh', 80, 11000, 0.08, '2025-01-01', N'Viên', N'Paracetamol 500mg giảm đau, hạ sốt cho người lớn và trẻ em.', N'Còn hàng'),
    ('SP0072', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Đồng Nai', 65, 15000, 0.08, '2025-02-01', N'Viên', N'Amoxicillin hỗ trợ điều trị nhiễm khuẩn hô hấp và nhiễm trùng đường tiểu.', N'Còn hàng'),
    ('SP0073', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Bình Phước', 55, 13000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen giúp giảm đau, chống viêm cho các bệnh viêm khớp, cảm cúm.', N'Còn hàng'),
    ('SP0074', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Hà Tĩnh', 45, 10500, 0.08, '2025-04-01', N'Viên', N'Vitamin C 1000mg tăng cường sức khỏe, chống oxi hóa và hỗ trợ miễn dịch.', N'Còn hàng'),
    ('SP0075', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Phú Thọ', 60, 24000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp thư giãn cơ thể và cải thiện giấc ngủ cho người lo âu và căng thẳng.', N'Còn hàng'),
    ('SP0076', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Lào Cai', 70, 9800, 0.08, '2025-01-01', N'Viên', N'Paracetamol giúp giảm đau và hạ sốt nhanh chóng, an toàn cho người lớn và trẻ em.', N'Còn hàng'),
    ('SP0077', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Bắc Kạn', 50, 14500, 0.08, '2025-02-01', N'Viên', N'Amoxicillin điều trị nhiễm khuẩn cho cả trẻ em và người lớn.', N'Còn hàng'),
    ('SP0078', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Quảng Trị', 60, 12000, 0.08, '2025-03-01', N'Viên', N'Ibuprofen giảm đau và chống viêm cho người bị đau nhức cơ thể và đau đầu.', N'Còn hàng'),
    ('SP0079', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Hà Nam', 55, 9700, 0.08, '2025-04-01', N'Viên', N'Vitamin C giúp duy trì sức khỏe và hỗ trợ miễn dịch hiệu quả.', N'Còn hàng'),
    ('SP0080', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Bến Tre', 45, 23000, 0.08, '2025-05-01', N'Viên', N'Diazepam giúp giảm lo âu và hỗ trợ cải thiện giấc ngủ cho người căng thẳng.', N'Còn hàng'),
	('SP0081', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Bình Dương', 20, 10000, 0.08, '2024-11-30', N'Viên', N'Paracetamol 500mg giúp giảm đau, hạ sốt hiệu quả.', N'Sắp hết hạn'),
    ('SP0082', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Hòa Bình', 15, 15000, 0.08, '2024-11-29', N'Viên', N'Amoxicillin điều trị nhiễm khuẩn.', N'Sắp hết hạn'),
    ('SP0083', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Phú Thọ', 25, 12000, 0.08, '2024-11-28', N'Viên', N'Ibuprofen giảm đau và viêm.', N'Sắp hết hạn'),
    ('SP0084', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Hà Nội', 30, 12000, 0.08, '2024-11-27', N'Viên', N'Vitamin C tăng cường miễn dịch.', N'Sắp hết hạn'),
    ('SP0085', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Cần Thơ', 18, 20000, 0.08, '2024-11-26', N'Viên', N'Diazepam giúp giảm lo âu.', N'Sắp hết hạn'),
    ('SP0086', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Tân Bình', 22, 10500, 0.08, '2024-11-25', N'Vỉ', N'Paracetamol giúp giảm đau và hạ sốt.', N'Sắp hết hạn'),
    ('SP0087', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Sài Gòn', 28, 14000, 0.08, '2024-11-24', N'Vỉ', N'Amoxicillin điều trị các bệnh nhiễm khuẩn.', N'Sắp hết hạn'),
    ('SP0088', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Đồng Nai', 12, 12500, 0.08, '2024-11-23', N'Vỉ', N'Ibuprofen giảm đau, chống viêm.', N'Sắp hết hạn'),
    ('SP0089', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Quảng Ngãi', 35, 9000, 0.08, '2024-11-22', N'Vỉ', N'Vitamin C chống oxi hóa.', N'Sắp hết hạn'),
    ('SP0090', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Hải Phòng', 10, 18000, 0.08, '2024-11-21', N'Vỉ', N'Diazepam hỗ trợ giảm căng thẳng.', N'Sắp hết hạn'),
	('SP0091', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Bình Dương', 20, 10000, 0.08, '2024-11-30', N'Viên', N'Paracetamol 500mg giúp giảm đau, hạ sốt hiệu quả.', N'Sắp hết hạn'),
    ('SP0092', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Hòa Bình', 15, 15000, 0.08, '2024-11-29', N'Viên', N'Amoxicillin điều trị nhiễm khuẩn.', N'Sắp hết hạn'),
    ('SP0093', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Phú Thọ', 25, 12000, 0.08, '2024-11-28', N'Viên', N'Ibuprofen giảm đau và viêm.', N'Sắp hết hạn'),
    ('SP0094', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Hà Nội', 30, 12000, 0.08, '2024-11-27', N'Viên', N'Vitamin C tăng cường miễn dịch.', N'Sắp hết hạn'),
    ('SP0095', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Cần Thơ', 18, 20000, 0.08, '2024-11-26', N'Viên', N'Diazepam giúp giảm lo âu.', N'Sắp hết hạn'),
    ('SP0096', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2024-01-01', N'Công ty Dược Tân Bình', 22, 10500, 0.08, '2024-11-25', N'Vỉ', N'Paracetamol giúp giảm đau và hạ sốt.', N'Sắp hết hạn'),
    ('SP0097', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2024-02-01', N'Công ty Dược Sài Gòn', 28, 14000, 0.08, '2024-11-24', N'Vỉ', N'Amoxicillin điều trị các bệnh nhiễm khuẩn.', N'Sắp hết hạn'),
    ('SP0098', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2024-03-01', N'Công ty Dược Đồng Nai', 12, 12500, 0.08, '2024-11-23', N'Vỉ', N'Ibuprofen giảm đau, chống viêm.', N'Sắp hết hạn'),
    ('SP0099', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2024-04-01', N'Công ty Dược Quảng Ngãi', 35, 9000, 0.08, '2024-11-22', N'Vỉ', N'Vitamin C chống oxi hóa.', N'Sắp hết hạn'),
    ('SP0100', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2024-05-01', N'Công ty Dược Hải Phòng', 10, 18000, 0.08, '2024-11-21', N'Vỉ', N'Diazepam hỗ trợ giảm căng thẳng.', N'Sắp hết hạn'),
	('SP0101', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2023-01-01', N'Công ty Dược Bình Dương', 20, 10000, 0.08, '2024-11-01', N'Viên', N'Paracetamol giúp giảm đau, hạ sốt.', N'Đã hết hạn'),
    ('SP0102', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2023-02-01', N'Công ty Dược Hòa Bình', 15, 15000, 0.08, '2024-11-01', N'Viên', N'Amoxicillin điều trị nhiễm khuẩn.', N'Đã hết hạn'),
    ('SP0103', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2023-03-01', N'Công ty Dược Phú Thọ', 25, 12000, 0.08, '2024-11-01', N'Viên', N'Ibuprofen giảm đau, chống viêm.', N'Đã hết hạn'),
    ('SP0104', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2023-04-01', N'Công ty Dược Hà Nội', 30, 12000, 0.08, '2024-11-01', N'Viên', N'Vitamin C giúp tăng cường miễn dịch.', N'Đã hết hạn'),
    ('SP0105', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2023-05-01', N'Công ty Dược Cần Thơ', 18, 20000, 0.08, '2024-11-01', N'Viên', N'Diazepam giúp giảm lo âu.', N'Đã hết hạn'),
    ('SP0106', N'Paracetamol 500mg', N'Giảm đau, hạ sốt', N'Thuốc', '2023-01-01', N'Công ty Dược Tân Bình', 22, 10500, 0.08, '2024-11-01', N'Vỉ', N'Paracetamol giúp giảm đau và hạ sốt.', N'Đã hết hạn'),
    ('SP0107', N'Amoxicillin 500mg', N'Kháng sinh', N'Thuốc', '2023-02-01', N'Công ty Dược Sài Gòn', 28, 14000, 0.08, '2024-11-01', N'Vỉ', N'Amoxicillin điều trị các bệnh nhiễm khuẩn.', N'Đã hết hạn'),
    ('SP0108', N'Ibuprofen 400mg', N'Giảm đau, chống viêm', N'Thuốc', '2023-03-01', N'Công ty Dược Đồng Nai', 12, 12500, 0.08, '2024-11-01', N'Vỉ', N'Ibuprofen giảm đau, chống viêm.', N'Đã hết hạn'),
    ('SP0109', N'Vitamin C 1000mg', N'Vitamin', N'Thuốc', '2023-04-01', N'Công ty Dược Quảng Ngãi', 35, 9000, 0.08, '2024-11-01', N'Vỉ', N'Vitamin C chống oxi hóa.', N'Đã hết hạn'),
    ('SP0110', N'Diazepam 5mg', N'An thần, giảm lo âu', N'Thuốc', '2023-05-01', N'Công ty Dược Hải Phòng', 10, 18000, 0.08, '2024-11-01', N'Vỉ', N'Diazepam hỗ trợ giảm căng thẳng.', N'Đã hết hạn'
	);

UPDATE SanPham
SET trangThai = CASE
        WHEN hanSuDung < GETDATE() THEN N'Hết hạn'
        WHEN DATEDIFF(DAY, GETDATE(), hanSuDung) <= 30 THEN N'Sắp hết hạn'
        WHEN soLuongTon = 0 THEN N'Hết hàng'
        WHEN soLuongTon > 0
        AND soLuongTon <= 10 THEN N'Tồn kho thấp'
        ElSE N'Có sẵn'
    END,
    ngayCapNhat = GETDATE()
WHERE maSanPham IN (
        SELECT maSanPham
        FROM SanPham
    );

-- Khách hàng
INSERT INTO KhachHang (maKhachHang, hoTen, soDienThoai, namSinh, diemTichLuy, gioiTinh, ghiChu) VALUES 
('KH0001', N'Nguyễn Văn An', '0912345678', '1990-01-15', 19300, N'Nam', N'Sốt, ho suyễn'),
('KH0002', N'Trần Thị Bích', '0923456781', '1985-05-20', 20000, N'Nữ', N'Đau bụng, nhức đầu'),
('KH0003', N'Phạm Thùy Dung', '0934567892', '1992-08-13', 50789, N'Nữ', N'Hen suyễn'),
('KH0004', N'Lê Văn Bình', '0945678913', '1988-03-10', 3099, N'Nam', N'Đau họng'),
('KH0005', N'Hoàng Mai Anh', '0956789124', '1995-12-25', 1201, N'Nữ', N'U chân'),
('KH0006', N'Nguyễn Đức Cường', '0967891235', '1993-07-22', 2250, N'Nam', N'Ho lao'),
('KH0007', N'Trịnh Thị Hồng', '0978912346', '1989-11-30', 182220, N'Nữ', N'Chướng bụng'),
('KH0008', N'Bùi Quốc Huy', '0989123457', '1994-09-16', 90331, N'Nam', N'Khách hàng thân thiết'),
('KH0009', N'Đỗ Minh Tuấn', '0991234568', '1987-02-05', 210222, N'Nam', N'Khó tiêu'),
('KH0010', N'Vũ Ngọc Lan', '0902345679', '1991-04-18', 6022919, N'Nữ', N'Đau dạ dày');

insert KhachHang (maKhachHang, hoTen, soDienThoai, namSinh, diemTichLuy, gioiTinh, ghiChu) VALUES 
('KH0000', N'Khách hàng lẻ', '0000000000', '1111-11-11', 0, N'Khác', N'Khách hàng lẻ')

-- Thêm dữ liệu vào bảng HoaDon
INSERT INTO HoaDon (maHoaDon, maKhachHang, maNhanVien, ngayTao, tienKhachDua, tongTien, tienThua, diemSuDung, loaiThanhToan) VALUES 
('HD0001', 'KH0001', 'MK0001', '2024-10-01 10:00:00', 200000, 180000, 20000, 0, N'Tiền mặt'),
('HD0002', 'KH0002', 'MK0002', '2024-10-02 11:00:00', 300000, 250000, 50000, 10, N'Chuyển khoản'),
('HD0003', 'KH0003', 'MK0001', '2024-10-03 12:30:00', 150000, 130000, 20000, 0, N'Tiền mặt'),
('HD0004', 'KH0004', 'MK0003', '2024-10-04 14:15:00', 500000, 450000, 50000, 5, N'Chuyển khoản'),
('HD0005', 'KH0005', 'MK0002', '2024-10-05 09:45:00', 250000, 200000, 50000, 0, N'Tiền mặt'),
('HD0006', 'KH0006', 'MK0003', '2024-10-06 15:30:00', 600000, 580000, 20000, 15, N'Chuyển khoản'),
('HD0007', 'KH0007', 'MK0001', '2024-10-07 13:20:00', 800000, 700000, 100000, 0, N'Tiền mặt'),
('HD0008', 'KH0008', 'MK0002', '2024-10-08 10:10:00', 1000000, 950000, 50000, 0, N'Chuyển khoản'),
('HD0009', 'KH0009', 'MK0003', '2024-10-09 11:00:00', 200000, 180000, 20000, 20, N'Tiền mặt'),
('HD0010', 'KH0010', 'MK0001', '2024-10-10 16:00:00', 300000, 280000, 20000, 10, N'Chuyển khoản');
-- Thêm dữ liệu vào bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHoaDon, maSanPham, soLuong, donGiaNhap, thue)
VALUES ('HD0001', 'SP0001', 5, 10000, 0.08),
    ('HD0001', 'SP0002', 3, 15000, 0.08),
    ('HD0002', 'SP0003', 10, 12000, 0.08),
    ('HD0002', 'SP0004', 2, 8000, 0.08),
    ('HD0003', 'SP0005', 1, 20000, 0.08),
    ('HD0003', 'SP0006', 7, 50000, 0.08),
    ('HD0004', 'SP0007', 4, 75000, 0.08),
    ('HD0004', 'SP0008', 6, 60000, 0.08),
    ('HD0005', 'SP0009', 8, 40000, 0.08),
    ('HD0005', 'SP0010', 3, 100000, 0.08),
    ('HD0006', 'SP0001', 9, 10000, 0.08),
    ('HD0007', 'SP0002', 2, 15000, 0.08),
    ('HD0008', 'SP0003', 1, 12000, 0.08),
    ('HD0009', 'SP0004', 5, 8000, 0.08),
    ('HD0010', 'SP0005', 4, 20000, 0.08),
    ('HD0010', 'SP0006', 2, 50000, 0.08),
    ('HD0010', 'SP0007', 3, 75000, 0.08),
    ('HD0010', 'SP0008', 10, 60000, 0.08),
    ('HD0010', 'SP0009', 6, 40000, 0.08),
    ('HD0010', 'SP0010', 7, 100000, 0.08);
-- Thêm dữ liệu vào bảng NhaCungCap
INSERT INTO NhaCungCap (maNCC, tenNCC, soDienThoai, diaChi, email)
VALUES (
        'NCC0001',
        N'Công ty Dược ABC',
        '0912345678',
        N'123 Đường Lý Thường Kiệt, TP.HCM',
        N'samsung@gmail.com'
    ),
    (
        'NCC0002',
        N'Công ty Dược XYZ',
        '0923456789',
        N'45 Đường Trần Hưng Đạo, Hà Nội',
        N'sony@gmail.com'
    ),
    (
        'NCC0003',
        N'Công ty Dược DEF',
        '0934567890',
        N'678 Đường Nguyễn Huệ, TP.HCM',
        N'apple@gmail.com'
    ),
    (
        'NCC0004',
        N'Công ty Dược GHI',
        '0945678901',
        N'789 Đường Cộng Hòa, TP.HCM',
        N'lg@gmail.com'
    ),
    (
        'NCC0005',
        N'Công ty Dược JKL',
        '0956789012',
        N'123 Đường Hoàng Diệu, Đà Nẵng',
        N'asus@gmail.com'
    );
-- Thêm dữ liệu vào bảng PhieuNhap
INSERT INTO PhieuNhap (
    maPhieuNhap,
    maNhanVien,
    maNhaCungCap,
    thoiGianNhap
) VALUES
    ('PN0001', 'MK0001', 'NCC0001', '2024-10-01 08:30:00'),
    ('PN0002', 'MK0002', 'NCC0002', '2024-10-02 09:45:00'),
    ('PN0003', 'MK0003', 'NCC0003', '2024-10-03 10:00:00'),
    ('PN0004', 'MK0004', 'NCC0004', '2024-10-04 11:15:00'),
    ('PN0005', 'MK0005', 'NCC0005', '2024-10-05 13:20:00'),
    ('PN0006', 'MK0006', 'NCC0001', '2024-10-06 14:35:00'),
    ('PN0007', 'MK0007', 'NCC0002', '2024-10-07 15:50:00'),
    ('PN0008', 'MK0008', 'NCC0003', '2024-10-08 16:05:00'),
    ('PN0009', 'MK0009', 'NCC0004', '2024-10-09 17:15:00'),
    ('PN0010', 'MK0010', 'NCC0005', '2024-10-10 18:25:00');

-- Thêm dữ liệu vào bảng ChiTietPhieuNhap
INSERT INTO ChiTietPhieuNhap (maSanPham, maPhieuNhap, soLuong, donGia, thue)
VALUES ('SP0001', 'PN0001', 50, 50000, 0.08),
    ('SP0002', 'PN0002', 30, 75000, 0.08),
    ('SP0003', 'PN0003', 40, 12000, 0.08),
    ('SP0004', 'PN0004', 20, 8000, 0.08),
    ('SP0005', 'PN0005', 60, 20000, 0.08),
    ('SP0006', 'PN0006', 35, 50000, 0.08),
    ('SP0007', 'PN0007', 45, 75000, 0.08),
    ('SP0008', 'PN0008', 25, 60000, 0.08),
    ('SP0009', 'PN0009', 55, 40000, 0.08),
    ('SP0010', 'PN0010', 65, 100000, 0.08);
-- Thêm dữ liệu vào bảng LichLamViec
INSERT INTO LichLamViec (maLichLamViec, maNhanVien, caLam, ngayLam)
VALUES ('LLV0001', 'MK0001', N'Ca 1', '2024-11-06'),
    ('LLV0002', 'MK0002', N'Ca 2', '2024-11-07');

ALTER TABLE ChiTietPhieuNhap
ADD 
    ngaySX datetime,
    hanSuDung datetime; 

UPDATE ChiTietPhieuNhap
SET 
    ngaySX = '2024-01-01',
    hanSuDung = '2025-01-01'
