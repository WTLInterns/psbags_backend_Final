-- Create subcategories table
CREATE TABLE IF NOT EXISTS subcategories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL,
    subcategory_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_category_subcategory (category_name, subcategory_name),
    INDEX idx_category_name (category_name)
);

-- Add subcategory_name column to existing product table
ALTER TABLE product 
ADD COLUMN subcategory_name VARCHAR(255) NULL AFTER category;

-- Insert sample subcategories
INSERT INTO subcategories (category_name, subcategory_name) VALUES
('Shop Online', 'Bags'),
('Shop Online', 'Wallets'),
('Shop Online', 'Trolley Bags'),
('Shop Online', 'Backpacks'),
('Shop Online', 'Handbags'),
('Corporate Gifts', 'Gift Hampers'),
('Corporate Gifts', 'Office Kits'),
('Corporate Gifts', 'Branded Merchandise'),
('Corporate Gifts', 'Executive Gifts'),
('Wholesale / Distributor', 'Bulk Bags'),
('Wholesale / Distributor', 'Travel Collection'),
('Wholesale / Distributor', 'Bulk Orders'),
('Wholesale / Distributor', 'Distributor Packages');

-- Update existing products with sample subcategories (optional)
-- UPDATE product SET subcategory_name = 'Bags' WHERE category = 'Shop Online' AND subcategory_name IS NULL;
-- UPDATE product SET subcategory_name = 'Gift Hampers' WHERE category = 'Corporate Gifts' AND subcategory_name IS NULL;
-- UPDATE product SET subcategory_name = 'Bulk Bags' WHERE category = 'Wholesale / Distributor' AND subcategory_name IS NULL;