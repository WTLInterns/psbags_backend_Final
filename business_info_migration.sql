-- Business Information columns for app_settings
-- Run once against your MySQL database

ALTER TABLE app_settings
    ADD COLUMN IF NOT EXISTS business_name     VARCHAR(200) NULL,
    ADD COLUMN IF NOT EXISTS business_email    VARCHAR(200) NULL,
    ADD COLUMN IF NOT EXISTS business_mobile   VARCHAR(20)  NULL,
    ADD COLUMN IF NOT EXISTS business_whatsapp VARCHAR(20)  NULL;
