-- ============================================================
-- PRICING ARCHITECTURE MIGRATION
-- Run this ONCE against the psbags database
-- ============================================================

-- 1. Add shipping fields to product table
ALTER TABLE product
  ADD COLUMN IF NOT EXISTS shipping_type VARCHAR(10) NOT NULL DEFAULT 'FREE',
  ADD COLUMN IF NOT EXISTS shipping_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00;

-- 2. Create global settings table (single-row)
CREATE TABLE IF NOT EXISTS app_settings (
  id INT PRIMARY KEY DEFAULT 1,
  gst_percentage DECIMAL(5,2) NOT NULL DEFAULT 18.00,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Seed default GST
INSERT INTO app_settings (id, gst_percentage)
VALUES (1, 18.00)
ON DUPLICATE KEY UPDATE id = id;

-- 3. Add pricing snapshot columns to user_orders
ALTER TABLE user_orders
  ADD COLUMN IF NOT EXISTS subtotal DECIMAL(10,2),
  ADD COLUMN IF NOT EXISTS shipping_cost DECIMAL(10,2),
  ADD COLUMN IF NOT EXISTS gst_percentage DECIMAL(5,2),
  ADD COLUMN IF NOT EXISTS gst_amount DECIMAL(10,2),
  ADD COLUMN IF NOT EXISTS grand_total DECIMAL(10,2);

-- 4. Back-fill existing orders: grand_total = total_amount (legacy, no breakdown)
UPDATE user_orders
SET grand_total = total_amount,
    subtotal = total_amount,
    shipping_cost = 0,
    gst_percentage = 18.00,
    gst_amount = 0
WHERE grand_total IS NULL;
