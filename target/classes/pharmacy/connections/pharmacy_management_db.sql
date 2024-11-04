USE MASTER 
DROP DATABASE medkit_pharmacy_management;
CREATE DATABASE medkit_pharmacy_management;
USE medkit_pharmacy_management;
select * from LichLamViec
select * from ChiTietHoaDon
select * from NhatKyThayDoiSanPham
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
	CONSTRAINT FK_NhatKyThayDoiNhanVien_NhanVien FOREIGN KEY (maNhanVienDaSua) REFERENCES NhanVien(maNhanVien)
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
CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(50) NOT NULL,
    maSanPham NVARCHAR(50) NOT NULL,
    soLuong INT NOT NULL CHECK (soLuong > 0),
    donGiaNhap FLOAT NOT NULL CHECK (donGiaNhap > 0),
    thue FLOAT NOT NULL CHECK (
        thue >= 0
        AND thue <= 1
    ),
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
    )
VALUES -- Nhân viên
    (
        'MK0001',
        N'Đặng Nam Kỳ',
        N'Nhân viên',
        '0912345671',
        N'kydan1@gmail.com',
        '2020-06-01',
        '2000-03-15',
        N'Còn làm việc',
        N'Đại học',
        N'Nam',
        '079200002212',
        7000000
    ),
    (
        'MK0002',
        N'Đoàn Băng Băng',
        N'Nhân viên',
        '0912345672',
        N'bangbang@gmail.com',
        '2019-08-12',
        '1999-08-22',
        N'Còn làm việc',
        N'Cao đẳng',
        N'Nữ',
        '080299035112',
        6500000
    ),
    (
        'MK0003',
        N'Nguyễn Hữu Sang',
        N'Nhân viên',
        '0912345673',
        N'sang3007@gmail.com',
        '2021-05-03',
        '1998-02-10',
        N'Nghỉ phép',
        N'Đại học',
        N'Nam',
        '080298005512',
        6800000
    ),
    (
        'MK0004',
        N'Dăm Bụt',
        N'Nhân viên',
        '0912345674',
        N'phamthid@gmail.com',
        '2021-10-22',
        '1997-06-30',
        N'Còn làm việc',
        N'Cao đẳng',
        N'Nữ',
        '080297000132',
        7000000
    ),
    (
        'MK0005',
        N'Phạm Văn Đồng',
        N'Nhân viên',
        '0912345675',
        N'dovane@gmail.com',
        '2022-01-10',
        '1996-09-12',
        N'Nghỉ phép',
        N'Cao học',
        N'Nam',
        '079296008012',
        7200000
    ),
    (
        'MK0006',
        N'Thiên Thiên',
        N'Nhân viên',
        '0912345676',
        N'thienthein@gmail.com',
        '2023-03-15',
        '1995-12-05',
        N'Còn làm việc',
        N'Đại học',
        N'Nữ',
        '080295000597',
        7100000
    ),
    (
        'MK0007',
        N'Cao Kỳ Luân',
        N'Nhân viên',
        '0912345677',
        N'caoluan001@gmail.com',
        '2018-07-19',
        '1994-10-25',
        N'Đã nghỉ việc',
        N'Cao học',
        N'Nam',
        '079294008812',
        7500000
    ),
    (
        'MK0008',
        N'Kỳ Kỳ',
        N'Nhân viên',
        '0912345678',
        N'kkkkk1@gmail.com',
        '2017-04-30',
        '1993-04-17',
        N'Còn làm việc',
        N'Đại học',
        N'Nữ',
        '079293025912',
        6900000
    ),
    -- Quản lý
    (
        'MK0009',
        N'Đặng Phúc Nguyên',
        N'Quản lý',
        '0912345679',
        'nguyendang0100@gmail.com',
        '2015-09-10',
        '1992-05-10',
        N'Còn làm việc',
        N'Cao học',
        N'Nam',
        '080204000112',
        10000000
    ),
    (
        'MK0010',
        N'Nguyễn Phan Minh Mẫn',
        N'Quản lý',
        '0912345680',
        'manminh992@gmail.com',
        '2016-11-20',
        '1991-03-28',
        N'Còn làm việc',
        N'Đại học',
        N'Nam',
        '080204000512',
        10500000
    );
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
VALUES (
        'SP0001',
        N'Paracetamol 500mg',
        N'Giảm đau, hạ sốt',
        N'Thuốc',
        '2024-01-01',
        N'Công ty Dược ABC',
        50,
        10000,
        0.08,
        '2025-01-01',
        N'Viên',
        N'Thuốc Paracetamol 500mg giúp giảm đau và hạ sốt hiệu quả cho cả trẻ em và người lớn.',
        N'Còn hàng'
    ),
    (
        'SP0002',
        N'Amoxicillin 500mg',
        N'Kháng sinh',
        N'Thuốc',
        '2024-02-01',
        N'Công ty Dược XYZ',
        30,
        15000,
        0.08,
        '2025-02-01',
        N'Viên',
        N'Kháng sinh Amoxicillin 500mg hỗ trợ điều trị nhiễm khuẩn do vi khuẩn nhạy cảm.',
        N'Còn hàng'
    ),
    (
        'SP0003',
        N'Ibuprofen 400mg',
        N'Giảm đau, chống viêm',
        N'Thuốc',
        '2024-03-01',
        N'Công ty Dược DEF',
        5,
        12000,
        0.08,
        '2025-03-01',
        N'Viên',
        N'Ibuprofen 400mg giúp giảm đau, giảm sốt và chống viêm hiệu quả.',
        N'Hết hàng'
    ),
    (
        'SP0004',
        N'Vitamin C 1000mg',
        N'Vitamin',
        N'Thuốc',
        '2024-04-01',
        N'Công ty Dược GHI',
        20,
        8000,
        0.08,
        '2025-04-01',
        N'Viên',
        N'Vitamin C 1000mg hỗ trợ tăng cường hệ miễn dịch và chống oxi hóa.',
        N'Còn hàng'
    ),
    (
        'SP0005',
        N'Diazepam 5mg',
        N'An thần, giảm lo âu',
        N'Thuốc',
        '2024-05-01',
        N'Công ty Dược JKL',
        10,
        20000,
        0.08,
        '2025-05-01',
        N'Viên',
        N'Diazepam 5mg giúp an thần và giảm lo âu, thường được dùng trong các trường hợp căng thẳng.',
        N'Còn hàng'
    ),
    (
        'SP0006',
        N'Paracetamol 500mg',
        N'Giảm đau, hạ sốt',
        N'Thuốc',
        '2024-01-01',
        N'Công ty Dược ABC',
        15,
        50000,
        0.08,
        '2025-01-01',
        N'Vỉ',
        N'Thuốc Paracetamol 500mg giúp giảm đau và hạ sốt hiệu quả cho cả trẻ em và người lớn.',
        N'Còn hàng'
    ),
    (
        'SP0007',
        N'Amoxicillin 500mg',
        N'Kháng sinh',
        N'Thuốc',
        '2024-02-01',
        N'Công ty Dược XYZ',
        25,
        75000,
        0.08,
        '2025-02-01',
        N'Vỉ',
        N'Kháng sinh Amoxicillin 500mg hỗ trợ điều trị nhiễm khuẩn do vi khuẩn nhạy cảm.',
        N'Còn hàng'
    ),
    (
        'SP0008',
        N'Ibuprofen 400mg',
        N'Giảm đau, chống viêm',
        N'Thuốc',
        '2024-03-01',
        N'Công ty Dược DEF',
        10,
        60000,
        0.08,
        '2025-03-01',
        N'Vỉ',
        N'Ibuprofen 400mg giúp giảm đau, giảm sốt và chống viêm hiệu quả.',
        N'Hết hàng'
    ),
    (
        'SP0009',
        N'Vitamin C 1000mg',
        N'Vitamin',
        N'Thuốc',
        '2024-04-01',
        N'Công ty Dược GHI',
        50,
        40000,
        0.08,
        '2025-04-01',
        N'Vỉ',
        N'Vitamin C 1000mg hỗ trợ tăng cường hệ miễn dịch và chống oxi hóa.',
        N'Còn hàng'
    ),
    (
        'SP0010',
        N'Diazepam 5mg',
        N'An thần, giảm lo âu',
        N'Thuốc',
        '2024-05-01',
        N'Công ty Dược JKL',
        20,
        100000,
        0.08,
        '2025-05-01',
        N'Vỉ',
        N'Diazepam 5mg giúp an thần và giảm lo âu, thường được dùng trong các trường hợp căng thẳng.',
        N'Còn hàng'
    ),
    (
        'SP0011',
        N'Siro Ho',
        N'Siro',
        N'Thuốc',
        '2024-01-01',
        N'Công ty Dược ABC',
        30,
        60000,
        0.08,
        '2025-01-01',
        N'Chai',
        N'Siro ho giúp làm giảm ho và dịu họng cho trẻ em.',
        N'Còn hàng'
    ),
    (
        'SP0012',
        N'Siro Kháng sinh - Điều trị nhiễm khuẩn',
        N'Kháng sinh',
        N'Thuốc',
        '2024-02-01',
        N'Công ty Dược XYZ',
        25,
        90000,
        0.08,
        '2025-02-01',
        N'Chai',
        N'Siro kháng sinh hỗ trợ điều trị nhiễm khuẩn hiệu quả.',
        N'Hết hàng'
    ),
    (
        'SP0013',
        N'Siro Hạ sốt',
        N'Siro',
        N'Thuốc',
        '2024-03-01',
        N'Công ty Dược DEF',
        10,
        80000,
        0.08,
        '2025-03-01',
        N'Chai',
        N'Siro hạ sốt giúp giảm sốt nhanh chóng cho trẻ em.',
        N'Còn hàng'
    ),
    (
        'SP0014',
        N'Siro Vitamin - Bổ sung dinh dưỡng',
        N'Siro',
        N'Thuốc',
        '2024-04-01',
        N'Công ty Dược GHI',
        40,
        70000,
        0.08,
        '2025-04-01',
        N'Chai',
        N'Siro vitamin giúp bổ sung dinh dưỡng cần thiết cho trẻ em.',
        N'Còn hàng'
    ),
    (
        'SP0015',
        N'Siro An thần - Giúp trẻ em ngủ ngon',
        N'Siro',
        N'Thuốc',
        '2024-05-01',
        N'Công ty Dược JKL',
        5,
        120000,
        0.08,
        '2025-05-01',
        N'Chai',
        N'Siro an thần giúp trẻ em ngủ ngon và sâu giấc.',
        N'Hết hàng'
    ),
    (
        'SP0016',
        N'Paracetamol - Hộp 20 viên',
        N'Giảm đau, chống viêm',
        N'Thuốc',
        '2024-01-01',
        N'Công ty Dược ABC',
        60,
        70000,
        0.08,
        '2025-01-01',
        N'Hộp',
        N'Thuốc giảm đau Paracetamol 500mg trong hộp 20 viên, hiệu quả nhanh chóng.',
        N'Còn hàng'
    ),
    (
        'SP0017',
        N'kháng sinh Amoxicillin - Hộp 15 viên',
        N'Kháng sinh',
        N'Thuốc',
        '2024-02-01',
        N'Công ty Dược XYZ',
        10,
        95000,
        0.08,
        '2025-02-01',
        N'Hộp',
        N'Kháng sinh Amoxicillin 500mg trong hộp 15 viên, điều trị nhiễm khuẩn hiệu quả.',
        N'Hết hàng'
    ),
    (
        'SP0018',
        N'Ibuprofen - Hộp 20 viên',
        N'',
        N'Chống viêm',
        '2024-03-01',
        N'Công ty Dược DEF',
        20,
        85000,
        0.08,
        '2025-03-01',
        N'Hộp',
        N'Thuốc hạ sốt Ibuprofen 400mg trong hộp 20 viên, hiệu quả giảm sốt.',
        N'Còn hàng'
    ),
    (
        'SP0019',
        N'Bổ sung vitamin C - Hộp 30 viên',
        N'Vitamin',
        N'Thuốc',
        '2024-04-01',
        N'Công ty Dược GHI',
        30,
        75000,
        0.08,
        '2025-04-01',
        N'Hộp',
        N'Viên bổ sung vitamin C 100.',
        N'Còn hàng'
    ),
    (
        'SP0020',
        N'Vitamin B12 1000mg - Tăng cường năng lượng',
        N'Vitamin',
        N'Thuốc',
        '2024-06-01',
        N'Công ty Dược MNO',
        40,
        30000,
        0.08,
        '2025-06-01',
        N'Ống',
        N'Vitamin B12 1000mcg giúp tăng cường năng lượng và hỗ trợ chức năng thần kinh.',
        N'Còn hàng'
    ),
    (
        'SP0021',
        N'Sắt dạng ống - Bổ sung vi chất cho cơ thể',
        N'Vitamin',
        N'Thuốc',
        '2024-07-01',
        N'Công ty Dược PQR',
        50,
        25000,
        0.08,
        '2025-07-01',
        N'Ống',
        N'Ống sắt giúp bổ sung sắt cần thiết cho cơ thể, ngăn ngừa thiếu máu.',
        N'Còn hàng'
    ),
    (
        'SP0022',
        N'Vitamin D3 dạng ống - Hỗ trợ xương chắc khỏe',
        N'Vitamin',
        N'Thuốc',
        '2024-08-01',
        N'Công ty Dược STU',
        60,
        35000,
        0.08,
        '2025-08-01',
        N'Ống',
        N'Vitamin D3 dạng ống giúp hấp thụ canxi, hỗ trợ xương phát triển.',
        N'Còn hàng'
    ),
    (
        'SP0023',
        N'Vitamin C dạng ống - Bổ sung đề kháng',
        N'Vitamin',
        N'Thuốc',
        '2024-09-01',
        N'Công ty Dược VWX',
        45,
        32000,
        0.08,
        '2025-09-01',
        N'Ống',
        N'Vitamin C dạng ống tăng cường hệ miễn dịch.',
        N'Còn hàng'
    ),
    (
        'SP0024',
        N'Magie dạng ống - Hỗ trợ chức năng cơ và thần kinh',
        N'Vitamin',
        N'Thuốc',
        '2024-10-01',
        N'Công ty Dược YZ',
        70,
        28000,
        0.08,
        '2025-10-01',
        N'Ống',
        N'Magie dạng ống giúp hỗ trợ chức năng cơ và thần kinh hiệu quả.',
        N'Còn hàng'
    ),
    (
        'SP0025',
        N'Thực phẩm bổ sung Vitamin tổng hợp',
        N'Vitamin',
        N'Thuốc',
        '2024-08-01',
        N'Công ty Dược STU',
        100,
        15000,
        0.08,
        '2025-08-01',
        N'Gói',
        N'Gói thực phẩm bổ sung Vitamin tổng hợp, hỗ trợ tăng cường sức khỏe hàng ngày.',
        N'Còn hàng'
    ),
    (
        'SP0026',
        N'Gói điện giải - Bổ sung nước và khoáng chất',
        N'Vitamin',
        N'Thuốc',
        '2024-09-01',
        N'Công ty Dược VWX',
        200,
        5000,
        0.08,
        '2025-09-01',
        N'Gói',
        N'Gói điện giải giúp bổ sung nước và khoáng chất cho cơ thể.',
        N'Còn hàng'
    ),
    (
        'SP0027',
        N'Gói dinh dưỡng bổ sung - Cung cấp protein',
        N'Vitamin',
        N'Thuốc',
        '2024-10-01',
        N'Công ty Dược ABC',
        150,
        17000,
        0.08,
        '2025-10-01',
        N'Gói',
        N'Gói dinh dưỡng bổ sung protein hỗ trợ phát triển cơ.',
        N'Còn hàng'
    ),
    (
        'SP0028',
        N'Gói chất xơ - Hỗ trợ tiêu hóa',
        N'Vitamin',
        N'Thuốc',
        '2024-11-01',
        N'Công ty Dược DEF',
        120,
        13000,
        0.08,
        '2025-11-01',
        N'Gói',
        N'Gói chất xơ giúp hỗ trợ hệ tiêu hóa.',
        N'Còn hàng'
    ),
    (
        'SP0029',
        N'Gói Omega-3 - Tăng cường chức năng não',
        N'Vitamin',
        N'Thuốc',
        '2024-12-01',
        N'Công ty Dược GHI',
        90,
        19000,
        0.08,
        '2025-12-01',
        N'Gói',
        N'Gói Omega-3 giúp tăng cường chức năng não và tim mạch.',
        N'Còn hàng'
    ),
    (
        'SP0030',
        N'Máy siêu âm cầm tay',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-06-01',
        N'Công ty Thiết bị Y tế ABC',
        10,
        25000000,
        0.1,
        '2029-06-01',
        N'Cái',
        N'Máy siêu âm cầm tay phục vụ chẩn đoán hình ảnh.',
        N'Còn hàng'
    ),
    (
        'SP0031',
        N'Máy đo huyết áp điện tử',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-07-01',
        N'Công ty Thiết bị Y tế DEF',
        15,
        1200000,
        0.1,
        '2026-07-01',
        N'Cái',
        N'Máy đo huyết áp điện tử chính xác và dễ sử dụng.',
        N'Còn hàng'
    ),
    (
        'SP0032',
        N'Máy xông khí dung',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-08-01',
        N'Công ty Thiết bị Y tế GHI',
        8,
        3500000,
        0.1,
        '2026-08-01',
        N'Cái',
        N'Máy xông khí dung cho bệnh nhân hen suyễn.',
        N'Còn hàng'
    ),
    (
        'SP0033',
        N'Máy điện tim',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-09-01',
        N'Công ty Thiết bị Y tế JKL',
        5,
        15000000,
        0.1,
        '2029-09-01',
        N'Cái',
        N'Máy điện tim phục vụ theo dõi nhịp tim.',
        N'Còn hàng'
    ),
    (
        'SP0034',
        N'Máy đo đường huyết',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-10-01',
        N'Công ty Thiết bị Y tế MNO',
        20,
        800000,
        0.1,
        '2026-10-01',
        N'Cái',
        N'Máy đo đường huyết nhanh chóng và chính xác.',
        N'Còn hàng'
    ),
    (
        'SP0035',
        N'Nhiệt kế điện tử',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-06-01',
        N'Công ty Dược XYZ',
        50,
        150000,
        0.05,
        '2026-06-01',
        N'Chiếc',
        N'Nhiệt kế điện tử chính xác, dễ sử dụng.',
        N'Còn hàng'
    ),
    (
        'SP0036',
        N'Máy đo oxy cầm tay',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-07-01',
        N'Công ty Dược UVW',
        30,
        500000,
        0.05,
        '2026-07-01',
        N'Chiếc',
        N'Máy đo oxy cầm tay phục vụ đo nồng độ oxy máu.',
        N'Còn hàng'
    ),
    (
        'SP0037',
        N'Máy thử đường huyết cá nhân',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-08-01',
        N'Công ty Dược RST',
        40,
        600000,
        0.05,
        '2026-08-01',
        N'Chiếc',
        N'Máy thử đường huyết nhỏ gọn, dễ sử dụng.',
        N'Còn hàng'
    ),
    (
        'SP0038',
        N'Ống nghe y tế',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-09-01',
        N'Công ty Dược PQR',
        25,
        200000,
        0.05,
        '2026-09-01',
        N'Chiếc',
        N'Ống nghe y tế chất lượng cao.',
        N'Còn hàng'
    ),
    (
        'SP0039',
        N'Nhiệt kế hồng ngoại',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-10-01',
        N'Công ty Dược OPQ',
        35,
        350000,
        0.05,
        '2026-10-01',
        N'Chiếc',
        N'Nhiệt kế hồng ngoại đo nhiệt độ nhanh chóng.',
        N'Còn hàng'
    ),
    (
        'SP0040',
        N'Bộ dụng cụ phẫu thuật cơ bản',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-06-01',
        N'Công ty Dụng cụ Y tế ABC',
        10,
        5000000,
        0.1,
        '2028-06-01',
        N'Bộ',
        N'Bộ dụng cụ phẫu thuật cơ bản dành cho phòng khám.',
        N'Còn hàng'
    ),
    (
        'SP0041',
        N'Bộ sơ cứu y tế',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-07-01',
        N'Công ty Dụng cụ Y tế DEF',
        25,
        750000,
        0.1,
        '2026-07-01',
        N'Bộ',
        N'Bộ sơ cứu đầy đủ dụng cụ cần thiết.',
        N'Còn hàng'
    ),
    (
        'SP0042',
        N'Bộ dụng cụ khám tai',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-08-01',
        N'Công ty Dụng cụ Y tế GHI',
        15,
        2000000,
        0.1,
        '2026-08-01',
        N'Bộ',
        N'Bộ dụng cụ khám tai tiêu chuẩn.',
        N'Còn hàng'
    ),
    (
        'SP0043',
        N'Bộ đo huyết áp cơ học',
        N'Dụng cụ y tế',
        N'Thiết bị y tế',
        '2024-09-01',
        N'Công ty Dụng cụ Y tế JKL',
        20,
        1500000,
        0.1,
        '2026-09-01',
        N'Bộ',
        N'Bộ đo huyết áp cơ học chính xác cao.',
        N'Còn hàng'
    ),
    (
        'SP0044',
        N'Bộ dụng cụ khám mắt',
        N'Sản phẩm bảo vệ cá nhân',
        N'Thiết bị y tế',
        '2024-10-01',
        N'Công ty Dụng cụ Y tế MNO',
        12,
        3000000,
        0.1,
        '2026-10-01',
        N'Bộ',
        N'Bộ dụng cụ khám mắt đầy đủ.',
        N'Còn hàng'
    ),
    (
        'SP0045',
        N'Hộp khẩu trang y tế 4 lớp',
        N'Sản phẩm bảo vệ cá nhân',
        N'Thiết bị y tế',
        '2024-06-01',
        N'Công ty Dược STU',
        500,
        100000,
        0.05,
        '2025-06-01',
        N'Hộp',
        N'Khẩu trang y tế 4 lớp đạt chuẩn.',
        N'Còn hàng'
    ),
    (
        'SP0046',
        N'Hộp găng tay y tế',
        N'Sản phẩm bảo vệ cá nhân',
        N'Thiết bị y tế',
        '2024-07-01',
        N'Công ty Dược VWX',
        300,
        80000,
        0.05,
        '2025-07-01',
        N'Hộp',
        N'Găng tay y tế dùng 1 lần.',
        N'Còn hàng'
    ),
    (
        'SP0047',
        N'Hộp bông băng y tế',
        N'Sản phẩm bảo vệ cá nhân',
        N'Thiết bị y tế',
        '2024-08-01',
        N'Công ty Dược YZ',
        250,
        60000,
        0.05,
        '2025-08-01',
        N'Hộp',
        N'Bông băng y tế đảm bảo vệ sinh.',
        N'Còn hàng'
    ),
    (
        'SP0048',
        N'Hộp que thử đường huyết',
        N'Que thử',
        N'Thiết bị y tế',
        '2024-09-01',
        N'Công ty Dược ABC',
        200,
        90000,
        0.05,
        '2025-09-01',
        N'Hộp',
        N'Que thử đường huyết dùng cho máy đo.',
        N'Còn hàng'
    ),
    (
        'SP0049',
        N'Hộp khẩu trang N95',
        N'Sản phẩm bảo vệ cá nhân',
        N'Thiết bị y tế',
        '2024-10-01',
        N'Công ty Dược DEF',
        400,
        120000,
        0.05,
        '2025-10-01',
        N'Hộp',
        N'Khẩu trang N95 bảo vệ hô hấp.',
        N'Còn hàng'
    ),
    (
        'SP0050',
        N'Dung dịch rửa tay sát khuẩn',
        N'Dung dịch vệ sinh',
        N'Thiết bị y tế',
        '2024-06-01',
        N'Công ty Dược GHI',
        300,
        50000,
        0.05,
        '2025-06-01',
        N'Chai',
        N'Dung dịch rửa tay sát khuẩn nhanh.',
        N'Còn hàng'
    ),
    (
        'SP0051',
        N'Nước muối sinh lý',
        N'Dung dịch vệ sinh',
        N'Thiết bị y tế',
        '2024-07-01',
        N'Công ty Dược JKL',
        500,
        25000,
        0.05,
        '2025-07-01',
        N'Chai',
        N'Nước muối sinh lý dùng cho mắt và mũi.',
        N'Còn hàng'
    ),
    (
        'SP0052',
        N'Dung dịch sát khuẩn dụng cụ',
        N'Dung dịch vệ sinh',
        N'Thiết bị y tế',
        '2024-08-01',
        N'Công ty Dược MNO',
        200,
        75000,
        0.05,
        '2025-08-01',
        N'Chai',
        N'Dung dịch sát khuẩn dụng cụ y tế.',
        N'Còn hàng'
    ),
    (
        'SP0053',
        N'Dung dịch rửa vết thương',
        N'Dung dịch vệ sinh',
        N'Thiết bị y tế',
        '2024-09-01',
        N'Công ty Dược OPQ',
        150,
        65000,
        0.05,
        '2025-09-01',
        N'Chai',
        N'Dung dịch rửa vết thương nhanh.',
        N'Còn hàng'
    ),
    (
        'SP0054',
        N'Dung dịch vệ sinh tay khô',
        N'Dung dịch vệ sinh',
        N'Thiết bị y tế',
        '2024-10-01',
        N'Công ty Dược UVW',
        250,
        55000,
        0.05,
        '2025-10-01',
        N'Chai',
        N'Dung dịch vệ sinh tay khô tiện lợi.',
        N'Còn hàng'
    );
-- Khách hàng
INSERT INTO KhachHang (
        maKhachHang,
        hoTen,
        soDienThoai,
        namSinh,
        diemTichLuy,
        gioiTinh,
        ghiChu
    )
VALUES (
        'KH0001',
        N'Nguyễn Văn An',
        '0912345678',
        '1990-01-15',
        19300,
        N'Nam',
        N'Khách hàng thân thiết'
    ),
    (
        'KH0002',
        N'Trần Thị Bích',
        '0923456781',
        '1985-05-20',
        20000,
        N'Nữ',
        N'Khách hàng thân thiết'
    ),
    (
        'KH0003',
        N'Phạm Thùy Dung',
        '0934567892',
        '1992-08-13',
        50789,
        N'Nữ',
        N'Khách hàng lẻ'
    ),
    (
        'KH0004',
        N'Lê Văn Bình',
        '0945678913',
        '1988-03-10',
        3099,
        N'Nam',
        N'Khách hàng thân thiết'
    ),
    (
        'KH0005',
        N'Hoàng Mai Anh',
        '0956789124',
        '1995-12-25',
        1201,
        N'Nữ',
        N'Khách hàng lẻ'
    ),
    (
        'KH0006',
        N'Nguyễn Đức Cường',
        '0967891235',
        '1993-07-22',
        2250,
        N'Nam',
        N'Khách hàng thân thiết'
    ),
    (
        'KH0007',
        N'Trịnh Thị Hồng',
        '0978912346',
        '1989-11-30',
        182220,
        N'Nữ',
        N'Khách hàng lẻ'
    ),
    (
        'KH0008',
        N'Bùi Quốc Huy',
        '0989123457',
        '1994-09-16',
        90331,
        N'Nam',
        N'Khách hàng thân thiết'
    ),
    (
        'KH0009',
        N'Đỗ Minh Tuấn',
        '0991234568',
        '1987-02-05',
        210222,
        N'Nam',
        N'Khách hàng lẻ'
    ),
    (
        'KH0010',
        N'Vũ Ngọc Lan',
        '0902345679',
        '1991-04-18',
        6022919,
        N'Nữ',
        N'Khách hàng thân thiết'
    );
-- Thêm dữ liệu vào bảng HoaDon
INSERT INTO HoaDon (
        maHoaDon,
        maKhachHang,
        maNhanVien,
        ngayTao,
        tienKhachDua,
        tongTien,
        tienThua,
        diemSuDung,
        loaiThanhToan
    )
VALUES (
        'HD0001',
        'KH0001',
        'MK0001',
        '2024-10-01 10:00:00',
        200000,
        180000,
        20000,
        0,
        N'Tiền mặt'
    ),
    (
        'HD0002',
        'KH0002',
        'MK0002',
        '2024-10-02 11:00:00',
        300000,
        250000,
        50000,
        10,
        N'Chuyển khoản'
    ),
    (
        'HD0003',
        'KH0003',
        'MK0001',
        '2024-10-03 12:30:00',
        150000,
        130000,
        20000,
        0,
        N'Tiền mặt'
    ),
    (
        'HD0004',
        'KH0004',
        'MK0003',
        '2024-10-04 14:15:00',
        500000,
        450000,
        50000,
        5,
        N'Chuyển khoản'
    ),
    (
        'HD0005',
        'KH0005',
        'MK0002',
        '2024-10-05 09:45:00',
        250000,
        200000,
        50000,
        0,
        N'Tiền mặt'
    ),
    (
        'HD0006',
        'KH0006',
        'MK0003',
        '2024-10-06 15:30:00',
        600000,
        580000,
        20000,
        15,
        N'Chuyển khoản'
    ),
    (
        'HD0007',
        'KH0007',
        'MK0001',
        '2024-10-07 13:20:00',
        800000,
        700000,
        100000,
        0,
        N'Tiền mặt'
    ),
    (
        'HD0008',
        'KH0008',
        'MK0002',
        '2024-10-08 10:10:00',
        1000000,
        950000,
        50000,
        0,
        N'Chuyển khoản'
    ),
    (
        'HD0009',
        'KH0009',
        'MK0003',
        '2024-10-09 11:00:00',
        200000,
        180000,
        20000,
        20,
        N'Tiền mặt'
    ),
    (
        'HD0010',
        'KH0010',
        'MK0001',
        '2024-10-10 16:00:00',
        300000,
        280000,
        20000,
        10,
        N'Chuyển khoản'
    )
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
    )
VALUES (
        'PN0001',
        'MK0001',
        'NCC0001',
        '2024-10-01 08:30:00'
    ),
    (
        'PN0002',
        'MK0002',
        'NCC0002',
        '2024-10-02 09:45:00'
    ),
    (
        'PN0003',
        'MK0003',
        'NCC0003',
        '2024-10-03 10:00:00'
    ),
    (
        'PN0004',
        'MK0004',
        'NCC0004',
        '2024-10-04 11:15:00'
    ),
    (
        'PN0005',
        'MK0005',
        'NCC0005',
        '2024-10-05 13:20:00'
    ),
    (
        'PN0006',
        'MK0006',
        'NCC0001',
        '2024-10-06 14:35:00'
    ),
    (
        'PN0007',
        'MK0007',
        'NCC0002',
        '2024-10-07 15:50:00'
    ),
    (
        'PN0008',
        'MK0008',
        'NCC0003',
        '2024-10-08 16:05:00'
    ),
    (
        'PN0009',
        'MK0009',
        'NCC0004',
        '2024-10-09 17:15:00'
    ),
    (
        'PN0010',
        'MK0010',
        'NCC0005',
        '2024-10-10 18:25:00'
    );
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
