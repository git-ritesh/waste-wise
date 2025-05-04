-- Database creation
CREATE DATABASE IF NOT EXISTS waste_wise_db;
USE waste_wise_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Stores hashed password
    full_name VARCHAR(100) NOT NULL,
    role ENUM('Resident', 'Collector', 'Admin') NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Waste types table
CREATE TABLE IF NOT EXISTS waste_types (
    type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

-- Waste requests table
CREATE TABLE IF NOT EXISTS waste_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    waste_type INT NOT NULL,
    quantity DECIMAL(10,2) NOT NULL, -- in kg
    address TEXT NOT NULL,
    status ENUM('Pending', 'Assigned', 'In Progress', 'Collected') DEFAULT 'Pending',
    requested_date DATE NOT NULL,
    pickup_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (waste_type) REFERENCES waste_types(type_id)
);

-- Assignments table
CREATE TABLE IF NOT EXISTS assignments (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL UNIQUE,
    collector_id INT NOT NULL,
    assigned_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('Assigned', 'In Progress', 'Completed') DEFAULT 'Assigned',
    FOREIGN KEY (request_id) REFERENCES waste_requests(request_id),
    FOREIGN KEY (collector_id) REFERENCES users(user_id)
);

-- Feedback table
CREATE TABLE IF NOT EXISTS feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    request_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comments TEXT,
    submitted_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (request_id) REFERENCES waste_requests(request_id)
);

-- Insert default waste types
INSERT INTO waste_types (type_name, description) VALUES
('Plastic', 'Plastic waste including bottles, containers, and packaging'),
('Paper', 'Paper waste including newspapers, magazines, and cardboard'),
('Glass', 'Glass waste including bottles and jars'),
('Metal', 'Metal waste including cans and aluminum foil'),
('Organic', 'Organic waste including food scraps and yard waste'),
('Electronic', 'Electronic waste including old devices and batteries'),
('Hazardous', 'Hazardous waste requiring special handling');

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password, full_name, role, email, phone)
VALUES ('admin', 'FS1Wsnx52MOT0OY6CAVGz5HUSp0PobkVR+FArcFR+r8u0gcFj14RHFqxXOI2MtOm', 'System Administrator', 'Admin', 'admin@wastewise.com', '1234567890');