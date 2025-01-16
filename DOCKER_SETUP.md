# Docker Setup Instructions

## Prerequisites
- Docker installed
- Docker Compose installed
- X11 server (for Linux GUI support)

## Setup Steps

1. Enable X11 forwarding for Docker:
```bash
# Linux
xhost +local:docker

# Windows with VcXsrv
# Start VcXsrv and set DISPLAY environment variable
set DISPLAY=host.docker.internal:0.0
```

2. Build and start the containers:
```bash
docker-compose up --build
```

3. Wait for the services to start:
- Database initialization (~30 seconds)
- Application startup (~15 seconds)

4. Access the application:
- GUI will appear automatically
- API available at http://localhost:8080
- Swagger UI at http://localhost:8080/swagger-ui.html

## Default Users
- Admin:
    - Username: admin
    - Password: admin123
- HR:
    - Username: hr
    - Password: hr123

## Useful Docker Commands

### View logs
```bash
# View all logs
docker-compose logs

# View app logs
docker-compose logs app

# View database logs
docker-compose logs db
```

### Restart services
```bash
# Restart all services
docker-compose restart

# Restart specific service
docker-compose restart app
```

### Stop and cleanup
```bash
# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Database management
```bash
# Connect to database
docker exec -it erms-db sqlplus sys/p@ss123//localhost:1521/XEPDB1 as sysdba

# Import data
docker exec -i erms-db sqlplus SYSTEM/p@ss123//localhost:1521/XEPDB1 < your_data.sql
```

## Troubleshooting

### GUI Issues
1. Check X11 forwarding:
```bash
echo $DISPLAY
xhost list
```

2. Verify X11 socket mount:
```bash
docker-compose exec app ls -l /tmp/.X11-unix
```

### Database Issues
1. Check database status:
```bash
docker-compose ps db
```

2. View database logs:
```bash
docker-compose logs db
```

### Application Issues
1. Check application logs:
```bash
docker-compose logs app
```

2. Verify environment variables:
```bash
docker-compose exec app env
```

## Development Workflow

1. Make changes to the code

2. Rebuild and restart:
```bash
docker-compose up --build -d
```

3. View logs for errors:
```bash
docker-compose logs -f app
```