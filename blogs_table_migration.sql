-- Migration Script: Create Blogs Table
-- Date: December 2024
-- Description: Creates blogs table following Product table conventions

-- Create blogs table matching Product table structure and conventions
CREATE TABLE `blogs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `slug` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `content` longtext DEFAULT NULL,
  `thumbnail_url` varchar(255) DEFAULT NULL,
  `thumbnail_public_id` varchar(255) DEFAULT NULL,
  `video_url` varchar(500) DEFAULT NULL,
  `video_public_id` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `featured` varchar(255) DEFAULT '0',
  `is_active` varchar(255) DEFAULT '1',
  `date` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_slug` (`slug`),
  INDEX `idx_category` (`category`),
  INDEX `idx_featured` (`featured`),
  INDEX `idx_is_active` (`is_active`),
  INDEX `idx_date_time` (`date`, `time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Verify the table has been created
SELECT 'Blogs table successfully created' AS migration_status;

-- Show table structure
DESCRIBE `blogs`;