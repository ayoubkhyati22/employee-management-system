# Employee Records Management System

## Overview
An internal Employee Records Management System to centralize the management of employee data across departments.

## Tech Stack
- Backend: Java 17, Spring Boot
- Database: Oracle SQL
- UI: Swing-based desktop application
- Security: JWT Authentication
- Testing: JUnit, Mockito

## Features
- User Role-Based Access Control (Admin, HR, Manager)
- Employee CRUD Operations
- Search and Filtering
- Audit Trail
- Secure Authentication

## Project Structure
```
employee-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/erms/employee_management_system/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       ├── service/
│   │   │       └── ui/
│   │   └── resources/
│   └── test/
├── docs/
└── README.md
```

## Prerequisites
- JDK 17
- Maven 3.8+
- Oracle Database

## Setup and Installation
1. Clone the repository
   ```bash
   git clone https://github.com/yourusername/employee-management-system.git
   ```

2. Configure database in `application.properties`
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
   spring.datasource.username=SYSTEM
   spring.datasource.password=p@ss123
   ```

3. Build the project
   ```bash
   mvn clean install
   ```

4. Run the application
   ```bash
   mvn spring-boot:run
   ```

## Default Users
- Admin:
    - Username: admin
    - Password: admin123
- HR:
    - Username: hr
    - Password: hr123

## API Documentation
The REST API documentation is available at `/swagger-ui.html` when running the application.

## Testing
Run tests using:
```bash
mvn test              # Unit tests
mvn verify            # Integration tests
mvn test jacoco:report # Coverage report
```

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details