-- Migration Script: Add Image 5 Support to Product Table
-- Date: December 2024
-- Description: Adds image_url5 and image_public_id5 columns to support 5th product image

-- Check current table structure before modification
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'product' AND TABLE_SCHEMA = DATABASE()
ORDER BY ORDINAL_POSITION;

-- Add Image 5 columns to product table
ALTER TABLE `product` 
ADD COLUMN `image_url5` varchar(255) DEFAULT NULL COMMENT 'URL for 5th product image',
ADD COLUMN `image_public_id5` varchar(255) DEFAULT NULL COMMENT 'Cloudinary public ID for 5th product image';

-- Verify the new columns have been added
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'product' 
  AND TABLE_SCHEMA = DATABASE()
  AND COLUMN_NAME IN ('image_url5', 'image_public_id5');

-- Optional: Show updated table structure
DESCRIBE `product`;

-- Migration completion message
SELECT 'Image 5 columns successfully added to product table' AS migration_status;