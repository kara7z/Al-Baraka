-- Schema actually used by the app (English tables).
-- Run: mariadb -uroot -p'Admin1234@' albaraka < schema.sql

CREATE DATABASE IF NOT EXISTS albaraka;
USE albaraka;

CREATE TABLE IF NOT EXISTS Customer (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Account (
  id VARCHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  number VARCHAR(24) NOT NULL UNIQUE,
  balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  customerId INT NOT NULL,
  accountType ENUM('checking', 'savings') NOT NULL,
  overdraft_limit DECIMAL(15,2) DEFAULT 0.00,
  interest_rate DECIMAL(5,2) DEFAULT 0.00,
  CONSTRAINT fk_account_customer FOREIGN KEY (customerId)
    REFERENCES Customer(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Transaction (
  id INT AUTO_INCREMENT PRIMARY KEY,
  date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  amount DECIMAL(15,2) NOT NULL,
  type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') NOT NULL,
  location VARCHAR(150),
  accountId VARCHAR(36) NOT NULL,
  CONSTRAINT fk_transaction_account FOREIGN KEY (accountId)
    REFERENCES Account(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT IGNORE INTO Customer (id, name, email) VALUES
  (1, 'Sara Bennani', 'sara@test.ma'),
  (2, 'Yassine El Fassi', 'yassine@test.ma'),
  (3, 'Salma Idrissi', 'salma@test.ma');
