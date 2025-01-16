# API Documentation

## Authentication Endpoints

### Login
```http
POST /api/auth/login
```

#### Request Body
```json
{
    "username": "string",
    "password": "string"
}
```

#### Response
```json
{
    "token": "string",
    "username": "string",
    "roles": ["string"]
}
```

## Employee Endpoints

### Get All Employees
```http
GET /api/employees
```
Authorization: Bearer Token required
Roles: ADMIN, HR, MANAGER

### Get Employee by ID
```http
GET /api/employees/{id}
```
Authorization: Bearer Token required
Roles: ADMIN, HR, MANAGER

### Create Employee
```http
POST /api/employees
```
Authorization: Bearer Token required
Roles: ADMIN, HR

#### Request Body
```json
{
    "employeeId": "string",
    "fullName": "string",
    "jobTitle": "string",
    "department": "string",
    "hireDate": "date",
    "employmentStatus": "string",
    "contactInformation": "string",
    "address": "string"
}
```

### Update Employee
```http
PUT /api/employees/{id}
```
Authorization: Bearer Token required
Roles: ADMIN, HR, MANAGER (same department only)

### Delete Employee
```http
DELETE /api/employees/{id}
```
Authorization: Bearer Token required
Roles: ADMIN, HR

### Search Employees
```http
GET /api/employees/search?query={query}
```
Authorization: Bearer Token required
Roles: ADMIN, HR, MANAGER

## Error Codes
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error

## Security
All endpoints except `/api/auth/login` require JWT authentication.
The JWT token should be included in the Authorization header as a Bearer token.