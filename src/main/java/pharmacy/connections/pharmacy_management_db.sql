CREATE DATABASE pharmacy_management;
USE pharmacy_management;


-- Create Users Table
CREATE TABLE Users (
    user_id NVARCHAR(36) PRIMARY KEY DEFAULT NEWID(), -- UUID format (GUID)
    username NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    role NVARCHAR(50) NOT NULL,
    email NVARCHAR(100),
    phone_number NVARCHAR(15),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- Create Medicines Table
CREATE TABLE Medicines (
    medicine_id NVARCHAR(36) PRIMARY KEY DEFAULT NEWID(), -- UUID format (GUID)
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255),
    manufacturer NVARCHAR(100),
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL,
    expiry_date DATE,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- Create Customers Table
CREATE TABLE Customers (
    customer_id NVARCHAR(36) PRIMARY KEY DEFAULT NEWID(), -- UUID format (GUID)
    full_name NVARCHAR(100) NOT NULL,
    date_of_birth DATE,
    email NVARCHAR(100),
    phone_number NVARCHAR(15),
    address NVARCHAR(255),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- Create Orders Table
CREATE TABLE Orders (
    order_id NVARCHAR(36) PRIMARY KEY DEFAULT NEWID(), -- UUID format (GUID)
    customer_id NVARCHAR(36) FOREIGN KEY REFERENCES Customers(customer_id),
    user_id NVARCHAR(36) FOREIGN KEY REFERENCES Users(user_id),
    order_date DATETIME DEFAULT GETDATE(),
    total_amount DECIMAL(10, 2) NOT NULL,
    status NVARCHAR(50),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- Create OrderDetails Table
CREATE TABLE OrderDetails (
    order_detail_id NVARCHAR(36) PRIMARY KEY DEFAULT NEWID(), -- UUID format (GUID)
    order_id NVARCHAR(36) FOREIGN KEY REFERENCES Orders(order_id),
    medicine_id NVARCHAR(36) FOREIGN KEY REFERENCES Medicines(medicine_id),
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- Create Reports Table
CREATE TABLE Reports (
    report_id NVARCHAR(36) PRIMARY KEY DEFAULT NEWID(), -- UUID format (GUID)
    report_type NVARCHAR(50),
    report_data NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    generated_by NVARCHAR(36) FOREIGN KEY REFERENCES Users(user_id)
);

-- Create Suppliers Table (Optional)
CREATE TABLE Suppliers (
    supplier_id NVARCHAR(36) PRIMARY KEY DEFAULT NEWID(), -- UUID format (GUID)
    name NVARCHAR(100) NOT NULL,
    contact_person NVARCHAR(100),
    phone_number NVARCHAR(15),
    email NVARCHAR(100),
    address NVARCHAR(255),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- Create SupplierMedicines Table (Optional)
CREATE TABLE SupplierMedicines (
    supplier_medicine_id NVARCHAR(36) PRIMARY KEY DEFAULT NEWID(), -- UUID format (GUID)
    supplier_id NVARCHAR(36) FOREIGN KEY REFERENCES Suppliers(supplier_id),
    medicine_id NVARCHAR(36) FOREIGN KEY REFERENCES Medicines(medicine_id),
    price DECIMAL(10, 2) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);


INSERT INTO Users
VALUES ('ajhdahdahsd', 'admin', '$2a$10$lcF8e6E46hbGBxkaBKe7GuBD3p5E0CDZH8I7D732EJwkQRItlSEaq', N'Đặng Phúc Nguyên', 'admin', '', '', null, null);

DELETE FROM Users WHERE username='admin';

select * from Users