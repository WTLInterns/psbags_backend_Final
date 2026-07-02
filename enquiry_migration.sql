-- Enquiry Management System Migration
-- Run this against your MySQL database

CREATE TABLE IF NOT EXISTS enquiries (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name   VARCHAR(150)  NOT NULL,
    mobile      VARCHAR(15)   NOT NULL,
    company_name VARCHAR(200) NOT NULL,
    product_requirement VARCHAR(500) NOT NULL,
    location    VARCHAR(200)  NOT NULL,
    product_type VARCHAR(100) NOT NULL,
    product_count VARCHAR(50) NOT NULL,
    status      VARCHAR(50)   NOT NULL DEFAULT 'NEW',
    created_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
