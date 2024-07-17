CREATE DATABASE digital_marketplace;

USE digital_marketplace;

CREATE TABLE Product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    price DECIMAL(10, 2),
    quantity_available INT,
    category VARCHAR(50)
);

CREATE TABLE Seller (
    seller_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    address TEXT,
    phone_number VARCHAR(15)
);

CREATE TABLE Transaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    seller_id INT,
    buyer_id INT,
    quantity INT,
    transaction_date DATETIME,
    status VARCHAR(50),
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (seller_id) REFERENCES Seller(seller_id)
);
