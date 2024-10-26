CREATE DATABASE medkit_pharmacy_management;
DROP DATABASE medkit_pharmacy_management;
USE medkit_pharmacy_management;
CREATE TABLE NhanVien (
    maNhanVien NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (maNhanVien LIKE 'MK____'),
    hoTen NVARCHAR(255) NOT NULL CHECK (hoTen LIKE '%[A-Z]%'),
    chucVu NVARCHAR(255) NOT NULL,
    soDienThoai NVARCHAR(10) NOT NULL UNIQUE,
    email NVARCHAR(225) NOT NULL CHECK (email LIKE '%_@__%.__%'),
    -- Kiểm tra định dạng email cơ bản
    ngayVaoLam DATETIME NOT NULL,
    namSinh DATE NOT NULL,
    trangThai NVARCHAR(50) CHECK (
        trangThai IN (N'Còn làm việc', N 'Nghỉ phép', N'Đã nghỉ việc')
    ),
    trinhDo NVARCHAR(50) CHECK (trinhDo IN (N'Cao đẳng', N'Đại học', N'Cao học')),
    gioiTinh NVARCHAR(50) CHECK (gioiTinh IN (N 'Nam', N 'Nữ', N'Khác')),
    cccd NCHAR(50) NOT NULL,
    tienLuong FLOAT NOT NULL
);
CREATE TABLE TaiKhoan (
    tenDangNhap NVARCHAR(50) NOT NULL UNIQUE,
    matKhau NVARCHAR(255) NOT NULL,
    isLoggedIn BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (tenDangNhap) REFERENCES NhanVien(maNhanVien)
);
update TaiKhoan
set isLoggedIn = 1
where tenDangNhap = 'MK0001' CREATE TABLE SanPham (
        maSanPham NVARCHAR(50) PRIMARY KEY NOT NULL,
        tenSanPham NVARCHAR(255) NOT NULL,
        danhMuc NVARCHAR(50) NOT NULL,
        ngaySX DATETIME NOT NULL,
        nhaSX NVARCHAR(255) NOT NULL,
        ngayTao DATETIME DEFAULT GETDATE(),
        ngayCapNhat DATETIME DEFAULT GETDATE(),
        soLuongTon INT NOT NULL CHECK (soLuongTon >= 0),
        donGiaBan FLOAT NOT NULL CHECK (donGiaBan >= 0),
        thue FLOAT NOT NULL CHECK (
            thue >= 0
            AND thue <= 1
        ),
        hanSuDung DATETIME NOT NULL,
        donViTinh NVARCHAR(50) NOT NULL,
        moTa NVARCHAR(255),
        trangThai NVARCHAR(50) DEFAULT N'Có sẵn',
        loaiSanPham NVARCHAR(50) NOT NULL
    );
CREATE TABLE KhachHang (
    maKhachHang NVARCHAR(50) PRIMARY KEY NOT NULL,
    hoTen NVARCHAR(255) NOT NULL CHECK (hoTen LIKE '%[A-Z]%'),
    soDienThoai CHAR(10) NOT NULL UNIQUE CHECK (LEN(soDienThoai) = 10),
    namSinh DATE,
    diemTichLuy INT DEFAULT 0,
    gioiTinh NVARCHAR(15),
    ghiChu NVARCHAR(255)
);
CREATE TABLE HoaDon (
    maHoaDon NVARCHAR(50) PRIMARY KEY NOT NULL,
    maKhachHang NVARCHAR(50) NOT NULL,
    maNhanVien NVARCHAR(50) NOT NULL,
    ngayTao DATETIME NOT NULL,
    tienKhachDua FLOAT NOT NULL CHECK (tienKhachDua >= 0),
    tongTien FLOAT NOT NULL CHECK (tongTien >= 0),
    tienThua FLOAT NOT NULL CHECK (tienThua >= 0),
    diemSuDung FLOAT CHECK (diemSuDung >= 0),
    loaiThanhToan NVARCHAR(50) CHECK (loaiThanhToan IN (N'Tiền mặt', N'Chuyển khoản')),
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang)
);
CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(50) NOT NULL,
    maSanPham NVARCHAR(50) NOT NULL,
    soLuong INT NOT NULL CHECK (soLuong > 0),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham),
    PRIMARY KEY (maHoaDon, maSanPham) -- Thêm khóa chính cho bảng này
);
CREATE TABLE NhaCungCap (
    maNCC NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (maNCC LIKE 'NCC____'),
    tenNCC NVARCHAR(255) NOT NULL,
    soDienThoai NVARCHAR(10) NOT NULL CHECK (
        LEN(soDienThoai) = 10
        AND soDienThoai NOT LIKE '%[^0-9]%'
    ),
    diaChi NVARCHAR(255) NOT NULL,
    email NVARCHAR(255) NOT NULL CHECK (email LIKE '%_@__%.__%') -- Kiểm tra định dạng email cơ bản
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
    donGia FLOAT NOT NULL CHECK (donGia >= 0),
    thue FLOAT NOT NULL CHECK (thue >= 0),
    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham),
    FOREIGN KEY (maPhieuNhap) REFERENCES PhieuNhap(maPhieuNhap),
    PRIMARY KEY (maSanPham, maPhieuNhap) -- Thêm khóa chính cho bảng này
);
CREATE TABLE LichLamViec (
    maLichLamViec NVARCHAR(255) PRIMARY KEY,
    maNhanVien NVARCHAR(50) NOT NULL,
    caLam NVARCHAR(255),
    ngayLam DATETIME,
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);
-- Thêm dữ liệu vào bảng NhanVien
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
VALUES (
        'MK0001',
        N 'Nguyễn Văn A',
        N 'Người quản lý',
        '0123456789',
        'ccccc@gmail.com',
        '2023-01-01',
        '1990-01-01',
        N'Còn làm việc',
        N'Đại học',
        N 'Nam',
        '123456789012',
        10000000
    ),
    (
        'MK0002',
        N'Trần Thị B',
        N 'Nhân viên',
        '0987654321',
        'hehehe@gmail.com',
        '2022-05-15',
        '1995-05-15',
        N 'Nghỉ phép',
        N'Cao đẳng',
        N 'Nữ',
        '098765432109',
        8000000
    ),
    (
        'MK0003',
        N 'Nguyễn Văn C',
        N 'Người quản lý',
        '12312311',
        'ccccc@gmail.com',
        '2023-01-01',
        '1990-01-01',
        N'Còn làm việc',
        N'Đại học',
        N 'Nam',
        '112233445566',
        12000000
    ),
    (
        'MK0004',
        N'Trần Thị D',
        N 'Nhân viên',
        '12331223',
        'hehehe@gmail.com',
        '2022-05-15',
        '1995-05-15',
        N 'Nghỉ phép',
        N'Cao đẳng',
        N 'Nữ',
        '223344556677',
        7000000
    ),
    (
        'MK0005',
        N 'Nguyễn Văn E',
        N 'Người quản lý',
        '1231231233',
        'ccccc@gmail.com',
        '2023-01-01',
        '1990-01-01',
        N'Còn làm việc',
        N'Đại học',
        N 'Nam',
        '334455667788',
        11000000
    );
select *
from NhanVien -- Thêm dữ liệu vào bảng TaiKhoan
INSERT INTO TaiKhoan (tenDangNhap, matKhau)
VALUES (
        'MK0001',
        '$2a$10$T1TakvHHGfbcU38U34gCiuKHz8eFwxU5f6d/G7SuwgEQUHga4ubKC'
    ),
    -- Mật khẩu: 123456
    (
        'MK0002',
        '$2a$10$T1TakvHHGfbcU38U34gCiuKHz8eFwxU5f6d/G7SuwgEQUHga4ubKC'
    ),
    -- Mật khẩu: 123456
    (
        'MK0003',
        '$2a$10$T1TakvHHGfbcU38U34gCiuKHz8eFwxU5f6d/G7SuwgEQUHga4ubKC'
    ),
    -- Mật khẩu: 123456
    (
        'MK0004',
        '$2a$10$T1TakvHHGfbcU38U34gCiuKHz8eFwxU5f6d/G7SuwgEQUHga4ubKC'
    ),
    -- Mật khẩu: 123456
    (
        'MK0005',
        '$2a$10$T1TakvHHGfbcU38U34gCiuKHz8eFwxU5f6d/G7SuwgEQUHga4ubKC'
    );
-- Mật khẩu: 123456
-- Thêm dữ liệu vào bảng SanPham
INSERT INTO SanPham (
        maSanPham,
        tenSanPham,
        danhMuc,
        ngaySX,
        nhaSX,
        soLuongTon,
        donGiaBan,
        thue,
        hanSuDung,
        donViTinh,
        moTa,
        loaiSanPham
    )
VALUES (
        'SP0003',
        N'Thuốc giảm đau',
        N'Thuốc',
        '2023-01-01',
        N'Công ty A',
        100,
        50000,
        0.1,
        '2025-01-01',
        N'Viên',
        N'Thuốc giảm đau cho người lớn.',
        N'Thuốc uống'
    ),
    (
        'SP0004',
        N'Thuốc cảm cúm',
        N'Thuốc',
        '2023-01-02',
        N'Công ty B',
        200,
        30000,
        0.1,
        '2025-02-01',
        N'Viên',
        N'Thuốc cảm cúm cho trẻ em.',
        N'Thiết bị'
    ),
    (
        'SP0005',
        N'Thuốc cảm cúm',
        N'Thuốc',
        '2023-01-02',
        N'Công ty B',
        200,
        30000,
        0.1,
        '2025-02-01',
        N'Viên',
        N'Thuốc cảm cúm cho trẻ em.',
        N'Thiết bị'
    );
-- Thêm dữ liệu vào bảng KhachHang
INSERT INTO KhachHang (
        maKhachHang,
        hoTen,
        soDienThoai,
        namSinh,
        diemTichLuy,
        gioiTinh
    )
VALUES (
        'KH0001',
        N 'Nguyễn Văn X',
        '0123456789',
        '1985-05-05',
        100,
        N 'Nam'
    ),
    (
        'KH0002',
        N'Trần Thị Y',
        '0987654321',
        '1990-07-07',
        200,
        N 'Nữ'
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
        '2023-10-01',
        50000,
        50000,
        0,
        0,
        N'Tiền mặt'
    ),
    (
        'HD0002',
        'KH0002',
        'MK0002',
        '2023-10-02',
        30000,
        30000,
        0,
        0,
        N'Chuyển khoản'
    );
-- Thêm dữ liệu vào bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (
        maHoaDon,
        maSanPham,
        soLuong
    )
VALUES (
        'HD0001',
        'SP0001',
        1
    ),
    (
        'HD0001',
        'SP0002',
        1
    ),
    (
        'HD0002',
        'SP0001',
        1
    );
-- Thêm dữ liệu vào bảng NhaCungCap
INSERT INTO NhaCungCap (
        maNCC,
        tenNCC,
        soDienThoai,
        diaChi,
        email
    )
VALUES (
        'NCC0001',
        N 'Công ty TNHH A',
        '0123456789',
        N'123 Đường ABC, TP.HCM',
        'ncc1@gmail.com'
    ),
    (
        'NCC0002',
        N 'Công ty TNHH B',
        '0987654321',
        N'456 Đường XYZ, TP.HCM',
        'ncc2@gmail.com'
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
        '2023-10-01'
    ),
    (
        'PN0002',
        'MK0002',
        'NCC0002',
        '2023-10-02'
    );
-- Thêm dữ liệu vào bảng ChiTietPhieuNhap
INSERT INTO ChiTietPhieuNhap (
        maSanPham,
        maPhieuNhap,
        soLuong,
        donGia,
        thue
    )
VALUES (
        'SP0001',
        'PN0001',
        10,
        50000,
        0.1
    ),
    (
        'SP0002',
        'PN0002',
        20,
        30000,
        0.1
    );
-- Thêm dữ liệu vào bảng LichLamViec
INSERT INTO LichLamViec (
        maLichLamViec,
        maNhanVien,
        caLam,
        ngayLam
    )
VALUES (
        'LLV0001',
        'MK0001',
        N'Sáng',
        '2023-10-01'
    ),
    (
        'LLV0002',
        'MK0002',
        N'Tối',
        '2023-10-02'
    );