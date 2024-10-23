CREATE DATABASE medkit_pharmacy_management;
DROP DATABASE medkit_pharmacy_management;
USE medkit_pharmacy_management;
CREATE TABLE NhanVien (
    maNhanVien NVARCHAR(50) PRIMARY KEY NOT NULL CHECK (maNhanVien LIKE 'MK____'),
    hoTen NVARCHAR(255) NOT NULL CHECK (hoTen LIKE '%[A-Z]%'),
    chucVu NVARCHAR(255) NOT NULL,
    soDienThoai NVARCHAR(10) NOT NULL UNIQUE,
    email NVARCHAR(225) NOT NULL,
    ngayVaoLam DATETIME NOT NULL,
    namSinh DATETIME NOT NULL,
    trangThai NVARCHAR(50) CHECK (
        trangThai IN (
            N'Còn làm việc',
            N'Nghỉ việc tạm thời',
            N'Đã nghĩ việc'
        )
    ),
    trinhDo NVARCHAR(50) CHECK (trinhDo IN (N'Cao đẳng', N'Đại học', N'Cao học')),
    gioiTinh NVARCHAR(50) CHECK (gioiTinh IN (N'Nam', N'Nữ', N'Khác'))
);
CREATE TABLE TaiKhoan (
    tenDangNhap NVARCHAR(50) NOT NULL UNIQUE,
    matKhau NVARCHAR(255) NOT NULL,
    isLoggedIn BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (tenDangNhap) REFERENCES NhanVien(maNhanVien)
);
CREATE TABLE DanhMuc (
    maDM NVARCHAR(50) PRIMARY KEY NOT NULL,
    tenDM NVARCHAR(255) NOT NULL,
    moTa NVARCHAR(255) NOT NULL,
    loaiDM INT NOT NULL,
    CONSTRAINT CHK_MaDM CHECK (
        LEN(maDM) BETWEEN 5 AND 10
    )
);
CREATE TABLE Thuoc (
    maThuoc NVARCHAR(50) PRIMARY KEY NOT NULL,
    tenThuoc NVARCHAR(255) NOT NULL,
    maDanhMuc NVARCHAR(50) NOT NULL,
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
    trangThai nvarchar(50) DEFAULT N'Có sẵn',
    CONSTRAINT FK_Thuoc_DanhMuc FOREIGN KEY (maDanhMuc) REFERENCES DanhMuc(maDM),
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
CREATE TABLE ThietBiYTe (
    maThietBi NVARCHAR(50) PRIMARY KEY,
    tenThietBi NVARCHAR(255) NOT NULL,
    ngaySX DATETIME NOT NULL,
    moTa NVARCHAR(255),
    soLuongTon INT NOT NULL CHECK (soLuongTon >= 0),
    donGiaBan FLOAT NOT NULL CHECK (donGiaBan >= 0),
    maDanhMuc NVARCHAR(50) NOT NULL,
    CONSTRAINT FK_ThietBiYTe_DanhMuc FOREIGN KEY (maDanhMuc) REFERENCES DanhMuc(maDM),
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
    soDienThoai NVARCHAR(10) NOT NULL CHECK (
        LEN(soDienThoai) = 10
        AND soDienThoai NOT LIKE '%[^0-9]%'
    ),
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
        gioiTinh
    )
VALUES (
        'MK0001',
        N'Nguyễn Văn A',
        N'Người quản lý',
        '0123456789',
        'ccccc@gmail.com',
        '2023-01-01',
        '1990-01-01',
        N'Đang làm',
        N'Đại học',
        N'Nam'
    ),
    (
        'MK0002',
        N'Trần Thị B',
        N'Nhân viên',
        '0987654321',
        'hehehe@gmail.com',
        '2022-05-15',
        '1995-05-15',
        N'Nghỉ việc tạm thời',
        N'Cao đẳng',
        N'Nữ'
    ),
    (
        'MK0003',
        N'Nguyễn Văn C',
        N'Người quản lý',
        '12312311',
        'ccccc@gmail.com',
        '2023-01-01',
        '1990-01-01',
        N'Đang làm',
        N'Đại học',
        N'Nam'
    ),
    (
        'MK0004',
        N'Trần Thị D',
        N'Nhân viên',
        '12331223',
        'hehehe@gmail.com',
        '2022-05-15',
        '1995-05-15',
        N'Nghỉ việc tạm thời',
        N'Cao đẳng',
        N'Nữ'
    ),
    (
        'MK0005',
        N'Nguyễn Văn E',
        N'Người quản lý',
        '1231231233',
        'ccccc@gmail.com',
        '2023-01-01',
        '1990-01-01',
        N'Đang làm',
        N'Đại học',
        N'Nam'
    );
-- Thêm dữ liệu vào bảng TaiKhoan
INSERT INTO TaiKhoan (tenDangNhap, matKhau)
VALUES (
        'MK0001',
        '$2a$10$T1TakvHHQX3JklzHDNLKK.gB/whHnMsPcZ76yrMNSK.hDbdmILrLm'
    ),
    (
        'MK0002',
        '$2a$10$T1TakvHHQX3JklzHDNLKK.gB/whHnMsPcZ76yrMNSK.hDbdmILrLm'
    );
-- Thêm dữ liệu vào bảng DanhMuc
INSERT INTO DanhMuc (maDM, tenDM, moTa, loaiDM)
VALUES ('DM0001', N'Danh mục A', N'Mô tả danh mục A', 1),
    ('DM0002', N'Danh mục B', N'Mô tả danh mục B', 2);
-- Thêm dữ liệu vào bảng Thuoc
INSERT INTO Thuoc (
        maThuoc,
        tenThuoc,
        maDanhMuc,
        ngaySX,
        nhaSX,
        soLuongTon,
        donGiaBan,
        thue,
        hanSuDung,
        donViTinh
    )
VALUES (
        'T0001',
        N'Thuốc A',
        'DM0001',
        '2023-01-01',
        N'Công ty A',
        100,
        20000,
        0.1,
        '2025-01-01',
        N'Viên'
    ),
    (
        'T0002',
        N'Thuốc B',
        'DM0002',
        '2022-02-01',
        N'Công ty B',
        50,
        30000,
        0.2,
        '2024-02-01',
        N'Vỉ'
    ),
    (
        'T0005',
        N'Thuốc F',
        'DM0001',
        '2023-01-01',
        N'Công ty A',
        10,
        20000,
        0.1,
        '2024-10-30',
        N'Viên'
    );
-- Thêm dữ liệu vào bảng KhachHang
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
        N'Nguyễn Văn C',
        '0123456789',
        '12-12-1999',
        100,
        'Nam',
        N'Khách hàng thường xuyên'
    ),
    (
        'KH0002',
        N'Trần Thị D',
        '0987654321',
        '12-12-1980',
        50,
        'Nữ',
        'heheh'
    );
ALTER TABLE KhachHang
ADD gioiTinh NVARCHAR(15) DEFAULT 'Nam';
UPDATE KhachHang
SET gioiTinh = 'Nam';
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
        'HD0008',
        'KH0001',
        'MK0001',
        '2024-10-30',
        50000,
        45000,
        5000,
        10,
        N'Tiền mặt'
    ),
    (
        'HD0009',
        'KH0002',
        'MK0002',
        '2024-10-25',
        60000,
        58000,
        2000,
        0,
        N'Chuyển khoản'
    ),
    (
        'HD0003',
        'KH0002',
        'MK0002',
        '2024-10-20',
        60000,
        58000,
        2000,
        0,
        N'Chuyển khoản'
    ),
    (
        'HD0004',
        'KH0002',
        'MK0002',
        '2024-10-15',
        60000,
        58000,
        2000,
        0,
        N'Chuyển khoản'
    ),
    (
        'HD0005',
        'KH0002',
        'MK0002',
        '2024-10-10',
        60000,
        58000,
        2000,
        0,
        N'Chuyển khoản'
    ),
    (
        'HD0006',
        'KH0002',
        'MK0002',
        '2024-10-01',
        60000,
        58000,
        2000,
        0,
        N'Chuyển khoản'
    );
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
        'HD0010',
        'KH0002',
        'MK0001',
        '2024-10-07',
        60000,
        50000,
        2000,
        0,
        N'Chuyển khoản'
    );
-- Thêm dữ liệu vào bảng ThietBiYTe
INSERT INTO ThietBiYTe (
        maThietBi,
        tenThietBi,
        ngaySX,
        moTa,
        soLuongTon,
        donGiaBan,
        maDanhMuc
    )
VALUES (
        'TB0001',
        N'Thiết bị A',
        '2023-01-01',
        N'Mô tả thiết bị A',
        20,
        20000,
        'DM0001'
    ),
    (
        'TB0002',
        N'Thiết bị B',
        '2023-02-01',
        N'Mô tả thiết bị B',
        15,
        30000,
        'DM0002'
    );
-- Thêm dữ liệu vào bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, maThietBi, soLuong)
VALUES ('HD0004', 'T0001', 'TB0001', 2),
    ('HD0004', 'T0002', 'TB0001', 1);
-- Thêm dữ liệu vào bảng NhaCungCap
INSERT INTO NhaCungCap (maNCC, tenNCC, soDienThoai, diaChi, email)
VALUES (
        'NCC0001',
        N'NCC A',
        '0123456789',
        N'Địa chỉ NCC A',
        'ncc.a@example.com'
    ),
    (
        'NCC0002',
        N'NCC B',
        '0987654321',
        N'Địa chỉ NCC B',
        'ncc.b@example.com'
    );

-- Thêm dữ liệu vào bảng PhieuNhap
INSERT INTO PhieuNhap (
        maPhieuNhap,
        maNhanVien,
        maNhaCungCap,
        thoiGianNhap
    )
VALUES ('PN0001', 'MK0001', 'NCC0001', '2023-09-30'),
    ('PN0002', 'MK0002', 'NCC0002', '2023-10-01');
-- Thêm dữ liệu vào bảng ChiTietPhieuNhap
INSERT INTO ChiTietPhieuNhap (
        maThietBi,
        maThuoc,
        maPhieuNhap,
        soLuong,
        donGia,
        thue
    )
VALUES ('TB0001', 'T0001', 'PN0001', 5, 20000, 0.1),
    ('TB0002', 'T0002', 'PN0002', 10, 30000, 0.2);
-- Thêm dữ liệu vào bảng LichLamViec
INSERT INTO LichLamViec (maLichLamViec, maNhanVien, caLam, ngayLam)
VALUES ('LLV0001', 'MK0001', N'Ca sáng', '2023-10-03'),
    ('LLV0002', 'MK0002', N'Ca chiều', '2023-10-03');
-- Truy vấn tất cả dữ liệu từ bảng NhanVien
SELECT *
FROM NhanVien;
-- Truy vấn tất cả dữ liệu từ bảng TaiKhoan
SELECT *
FROM TaiKhoan;
-- Truy vấn tất cả dữ liệu từ bảng Thuoc
SELECT *
FROM Thuoc;
-- Truy vấn tất cả dữ liệu từ bảng DanhMuc
SELECT *
FROM DanhMuc;
-- Truy vấn tất cả dữ liệu từ bảng KhachHang
SELECT *
FROM KhachHang;
-- Truy vấn tất cả dữ liệu từ bảng HoaDon
SELECT *
FROM HoaDon;
-- Truy vấn tất cả dữ liệu từ bảng ThietBiYTe
SELECT *
FROM ThietBiYTe;
-- Truy vấn tất cả dữ liệu từ bảng ChiTietHoaDon
SELECT *
FROM ChiTietHoaDon;
-- Truy vấn tất cả dữ liệu từ bảng NhaCungCap
SELECT *
FROM NhaCungCap;
-- Truy vấn tất cả dữ liệu từ bảng PhieuNhap
SELECT *
FROM PhieuNhap;
-- Truy vấn tất cả dữ liệu từ bảng ChiTietPhieuNhap
SELECT *
FROM ChiTietPhieuNhap;
-- Truy vấn tất cả dữ liệu từ bảng LichLamViec
SELECT *
FROM LichLamViec;
SELECT t.*,
    SUM(cthd.soLuong) AS soLuongBan
FROM Thuoc t
    JOIN ChiTietHoaDon cthd ON t.maThuoc = cthd.maThuoc
    JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon
WHERE (CAST(hd.ngayTao AS DATE)) = '2024-10-15'
GROUP BY t.maThuoc,
    t.tenThuoc,
    t.maDanhMuc,
    t.ngaySX,
    t.nhaSX,
    t.ngayTao,
    t.ngayCapNhat,
    t.donViTinh,
    t.soLuongTon,
    t.donGiaBan,
    t.thue,
    t.hanSuDung,
    t.moTa
ORDER BY soLuongBan DESC << << << < HEAD == == == = -- sửa cái islogged thành 1 cho MK0001
UPDATE TaiKhoan
SET isLoggedIn = 1
WHERE tenDangNhap = 'MK0001' >> >> >> > 9330a8e3eafe4d4a597977cdb0d23e1509768721