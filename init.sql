-- Create roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_HR');
INSERT INTO roles (name) VALUES ('ROLE_MANAGER');

-- Create admin user (password: admin123)
INSERT INTO users (username, email, password, department)
VALUES ('admin', 'admin@erms.com', '$2a$10$3w8WCFjClqnEhDrn6T4vCu0bNqj8.oAy8H5p8H5LeSPe3uj1u5.8G', 'ADMIN');

-- Create HR user (password: hr123)
INSERT INTO users (username, email, password, department)
VALUES ('hr', 'hr@erms.com', '$2a$10$YourEncryptedPasswordHere', 'HR');

-- Assign roles
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'hr' AND r.name = 'ROLE_HR';

-- Insert sample employees
INSERT INTO employees (employee_id, full_name, job_title, department, hire_date, employment_status, contact_information, address)
VALUES ('EMP001', 'John Doe', 'Software Engineer', 'IT', CURRENT_DATE, 'Active', 'john.doe@erms.com', '123 Main St');

INSERT INTO employees (employee_id, full_name, job_title, department, hire_date, employment_status, contact_information, address)
VALUES ('EMP002', 'Jane Smith', 'HR Manager', 'HR', CURRENT_DATE, 'Active', 'jane.smith@erms.com', '456 Oak Ave');