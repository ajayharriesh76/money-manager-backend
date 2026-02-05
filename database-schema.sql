-- Money Manager Database Schema for Oracle DB

-- Create Sequences
CREATE SEQUENCE TRANSACTION_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE ACCOUNT_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Create Accounts Table
CREATE TABLE accounts (
    id NUMBER(19) PRIMARY KEY,
    account_name VARCHAR2(255) NOT NULL UNIQUE,
    balance NUMBER(15,2) NOT NULL,
    account_type VARCHAR2(50) NOT NULL CHECK (account_type IN ('CASH', 'BANK', 'CREDIT_CARD', 'WALLET')),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create Transactions Table
CREATE TABLE transactions (
    id NUMBER(19) PRIMARY KEY,
    type VARCHAR2(50) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE', 'TRANSFER')),
    amount NUMBER(15,2) NOT NULL,
    category VARCHAR2(255),
    division VARCHAR2(50) CHECK (division IN ('OFFICE', 'PERSONAL')),
    description VARCHAR2(500) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    from_account VARCHAR2(255),
    to_account VARCHAR2(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    is_editable NUMBER(1) DEFAULT 1 NOT NULL
);

-- Create Indexes for better query performance
CREATE INDEX idx_transaction_date ON transactions(transaction_date);
CREATE INDEX idx_transaction_type ON transactions(type);
CREATE INDEX idx_transaction_category ON transactions(category);
CREATE INDEX idx_transaction_division ON transactions(division);
CREATE INDEX idx_account_name ON accounts(account_name);

-- Insert Sample Accounts
INSERT INTO accounts (id, account_name, balance, account_type, created_at, updated_at)
VALUES (ACCOUNT_SEQ.NEXTVAL, 'Main Savings', 50000, 'BANK', SYSTIMESTAMP, SYSTIMESTAMP);

INSERT INTO accounts (id, account_name, balance, account_type, created_at, updated_at)
VALUES (ACCOUNT_SEQ.NEXTVAL, 'Cash Wallet', 5000, 'CASH', SYSTIMESTAMP, SYSTIMESTAMP);

INSERT INTO accounts (id, account_name, balance, account_type, created_at, updated_at)
VALUES (ACCOUNT_SEQ.NEXTVAL, 'Credit Card', 0, 'CREDIT_CARD', SYSTIMESTAMP, SYSTIMESTAMP);

-- Insert Sample Transactions
INSERT INTO transactions (id, type, amount, category, division, description, transaction_date, to_account, created_at, updated_at, is_editable)
VALUES (TRANSACTION_SEQ.NEXTVAL, 'INCOME', 75000, 'Salary', 'PERSONAL', 'Monthly Salary', SYSTIMESTAMP - INTERVAL '5' DAY, 'Main Savings', SYSTIMESTAMP - INTERVAL '5' DAY, SYSTIMESTAMP - INTERVAL '5' DAY, 1);

INSERT INTO transactions (id, type, amount, category, division, description, transaction_date, from_account, created_at, updated_at, is_editable)
VALUES (TRANSACTION_SEQ.NEXTVAL, 'EXPENSE', 2500, 'Food', 'PERSONAL', 'Grocery Shopping', SYSTIMESTAMP - INTERVAL '3' DAY, 'Cash Wallet', SYSTIMESTAMP - INTERVAL '3' DAY, SYSTIMESTAMP - INTERVAL '3' DAY, 1);

INSERT INTO transactions (id, type, amount, category, division, description, transaction_date, from_account, created_at, updated_at, is_editable)
VALUES (TRANSACTION_SEQ.NEXTVAL, 'EXPENSE', 1500, 'Fuel', 'OFFICE', 'Petrol for office commute', SYSTIMESTAMP - INTERVAL '2' DAY, 'Cash Wallet', SYSTIMESTAMP - INTERVAL '2' DAY, SYSTIMESTAMP - INTERVAL '2' DAY, 1);

INSERT INTO transactions (id, type, amount, category, division, description, transaction_date, from_account, created_at, updated_at, is_editable)
VALUES (TRANSACTION_SEQ.NEXTVAL, 'EXPENSE', 800, 'Movie', 'PERSONAL', 'Movie tickets', SYSTIMESTAMP - INTERVAL '1' DAY, 'Credit Card', SYSTIMESTAMP - INTERVAL '1' DAY, SYSTIMESTAMP - INTERVAL '1' DAY, 1);

INSERT INTO transactions (id, type, amount, category, division, description, transaction_date, to_account, created_at, updated_at, is_editable)
VALUES (TRANSACTION_SEQ.NEXTVAL, 'INCOME', 15000, 'Freelance', 'PERSONAL', 'Website development project', SYSTIMESTAMP, 'Main Savings', SYSTIMESTAMP, SYSTIMESTAMP, 1);

COMMIT;
