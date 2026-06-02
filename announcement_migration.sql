-- Create announcements table
CREATE TABLE announcements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text1 VARCHAR(500),
    text2 VARCHAR(500), 
    text3 VARCHAR(500),
    text4 VARCHAR(500),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

-- Insert current announcements as default data
INSERT INTO announcements (text1, text2, text3, text4, is_active, created_by) VALUES 
('10% off when you subscribe to our emails. Brand exclusions apply. T&Cs apply',
 'Guess what\'s just landed? Discover the latest arrivals now',
 'All over india delivery and free returns - shop now',
 NULL,
 true,
 'system');