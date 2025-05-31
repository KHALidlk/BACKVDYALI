-- Drop the table if it exists to recreate with proper structure
DROP TABLE IF EXISTS users CASCADE;

-- Create the users table with proper ID column and constraints
CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255),
    password_salt VARCHAR(255),
    role VARCHAR(50),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(50),
    language VARCHAR(10),
    gdpr_consent BOOLEAN,
    gdpr_consent_date TIMESTAMP,
    is_active BOOLEAN,
    identity_type VARCHAR(50),
    identity_number VARCHAR(100),
    two_factor_enabled BOOLEAN,
    two_factor_method VARCHAR(50),
    last_login TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255)
);

-- Add some comments for documentation
COMMENT ON TABLE users IS 'Stores user information for authentication and profile management';
COMMENT ON COLUMN users.id IS 'Primary key, UUID format';
COMMENT ON COLUMN users.email IS 'User email address, must be unique';
COMMENT ON COLUMN users.role IS 'User role (client, agent, admin)';
