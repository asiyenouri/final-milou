create database finalMilou;
use finalMilou;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    read_emails TEXT
);

CREATE TABLE emails (
    id VARCHAR(6) PRIMARY KEY,
    sender VARCHAR(255) NOT NULL,
    recipients TEXT NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    date DATE NOT NULL,
    original_email_id VARCHAR(6),
    is_reply BOOLEAN DEFAULT FALSE,
    is_forward BOOLEAN DEFAULT FALSE
);

