DROP DATABASE albaraka;
use albaraka;

-- =========================
-- Table: Customer
-- =========================
CREATE TABLE Customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

-- =========================
-- Table: Account
-- =========================
CREATE TABLE Account (
  id VARCHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  number VARCHAR(24) NOT NULL UNIQUE,
  balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  customerId INT NOT NULL,
  accountType ENUM('checking', 'savings') NOT NULL,
  overdraft_limit DECIMAL(15,2) DEFAULT 0.00,
  interest_rate DECIMAL(5,2) DEFAULT 0.00,

  CONSTRAINT fk_account_customer
  FOREIGN KEY (customerId)
  REFERENCES Customer(id)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

-- =========================
-- Table: Transaction
-- =========================
CREATE TABLE Transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(15,2) NOT NULL,
    type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') NOT NULL,
    location VARCHAR(150),
    accountId VARCHAR(36) NOT NULL,

    CONSTRAINT fk_transaction_account
        FOREIGN KEY (accountId)
        REFERENCES Account(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
