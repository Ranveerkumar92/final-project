# Quick Start Guide - Workflow Builder

## 5-Minute Setup

### Prerequisites
- Java 17+ installed
- Docker & Docker Compose installed
- Maven 3.9+ installed

### Step 1: Configure Environment
```bash
cd workflow-builder
cp .env.example .env
```

### Step 2: Build All Services
```bash
./build.sh
# OR manually:
mvn clean install -DskipTests
```

### Step 3: Start Services
```bash
./deploy.sh
# This starts:
# - PostgreSQL (port 5432)
# - Kafka (port 9092)
# - API Gateway (port 8000)
# - Workflow Service (port 8001)
# - Approval Service (port 8002)
# - Task Service (port 8003)
# - Notification Service (port 8004)
# - Prometheus (port 9090)
# - Grafana (port 3000)
```

### Step 4: Verify Services
```bash
# Check all services are running
docker-compose ps

# View logs
docker-compose logs -f

# Test API
curl http://localhost:8001/actuator/health
```

### Step 5: Access Applications

| Service | URL |
|---------|-----|
| **Workflow Service API** | http://localhost:8001/api/v1/workflows |
| **Approval Service API** | http://localhost:8002/api/v1/approvals |
| **Task Service API** | http://localhost:8003/api/v1/tasks |
| **Notification Service API** | http://localhost:8004/api/v1/notifications |
| **Swagger UI (Workflow)** | http://localhost:8001/swagger-ui.html |
| **Swagger UI (Approval)** | http://localhost:8002/swagger-ui.html |
| **Swagger UI (Task)** | http://localhost:8003/swagger-ui.html |
| **Grafana Dashboard** | http://localhost:3000 (admin/admin) |
| **Prometheus** | http://localhost:9090 |

## First API Call

### 1. Create a Workflow
```bash
curl -X POST http://localhost:8001/api/v1/workflows \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Purchase Approval",
    "description": "Approve purchase requests",
    "definition": "{\"steps\": []}"
  }'
```

### 2. Get All Workflows
```bash
curl http://localhost:8001/api/v1/workflows
```

### 3. Publish a Workflow
```bash
curl -X POST http://localhost:8001/api/v1/workflows/1/publish
```

## Common Commands

### Build
```bash
# Build all services
mvn clean install -DskipTests

# Build specific service
cd services/workflow-service && mvn clean package -DskipTests

# Build with tests
mvn clean verify
```

### Run
```bash
# Start all services (Docker)
docker-compose up -d

# Start with logs
docker-compose up

# Run single service locally
cd services/workflow-service
mvn spring-boot:run
```

### Stop
```bash
# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Logs
```bash
# View all logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f workflow-service

# Kubernetes logs
kubectl logs -f deployment/workflow-service -n workflow
```

## Troubleshooting

### Port already in use
```bash
# Find and kill process on port
lsof -i :8001
kill -9 <PID>
```

### Services won't start
```bash
# Check logs
docker-compose logs

# Rebuild images
docker-compose down -v
docker-compose build --no-cache
docker-compose up
```

### Database connection error
```bash
# Verify PostgreSQL is running
docker-compose ps postgres

# Check credentials in .env file
cat .env | grep DB_
```

### Kafka connection issues
```bash
# Test Kafka connectivity
nc -zv localhost 9092

# Check Kafka logs
docker-compose logs kafka
```

## Project Structure

```
workflow-builder/
├── services/               # Microservices
│   ├── common/            # Shared libraries
│   ├── workflow-service/  # Workflow management
│   ├── approval-service/  # Approval workflows
│   ├── task-service/      # Task orchestration
│   ├── notification-service/  # Notifications
│   └── api-gateway/       # API routing
├── kubernetes/            # K8s manifests
├── docker/                # Dockerfiles
├── config/                # Configuration
├── docker-compose.yml     # Docker Compose setup
├── pom.xml               # Maven parent POM
├── README.md             # Full documentation
├── DEVELOPMENT.md        # Development guide
└── QUICKSTART.md         # This file
```

## Architecture

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       ▼
┌──────────────────┐
│  API Gateway     │ (port 8000)
└──────┬───────────┘
       │
       ├─────────────────┬─────────────────┬──────────────────┐
       ▼                 ▼                 ▼                  ▼
┌─────────────┐  ┌──────────────┐  ┌────────────┐  ┌─────────────────┐
│ Workflow    │  │  Approval    │  │    Task    │  │  Notification   │
│ Service     │  │  Service     │  │  Service   │  │  Service        │
│ (8001)      │  │  (8002)      │  │  (8003)    │  │  (8004)         │
└──────┬──────┘  └──────┬───────┘  └────┬───────┘  └────┬────────────┘
       │                │               │              │
       └────────────────┼───────────────┼──────────────┘
                        ▼
                    ┌────────┐
                    │ Kafka  │ (Event Streaming)
                    └────┬───┘
                         │
        ┌────────────────┼────────────────┐
        ▼                ▼                 ▼
   ┌─────────┐      ┌──────────┐     ┌───────────┐
   │PostgreSQL│      │Prometheus│     │ Grafana  │
   │          │      │          │     │          │
   └──────────┘      └──────────┘     └──────────┘
```

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| DB_USER | workflow_user | Database username |
| DB_PASSWORD | workflow_password | Database password |
| DB_HOST | postgres | Database host |
| KAFKA_BOOTSTRAP_SERVERS | kafka:9092 | Kafka broker |
| MAIL_USERNAME | (none) | Email for notifications |
| MAIL_PASSWORD | (none) | Email app password |
| JWT_SECRET | (provided) | JWT signing key |
| JWT_EXPIRATION | 86400000 | Token expiration (ms) |

## Performance Tips

### Development
- Run services locally for faster debugging
- Use `mvn -o` for offline builds
- Cache Maven dependencies

### Production (Kubernetes)
- Enable horizontal pod autoscaling
- Use resource limits and requests
- Implement circuit breakers
- Monitor metrics in Grafana

## Next Steps

1. Review [README.md](README.md) for full documentation
2. Check [DEVELOPMENT.md](DEVELOPMENT.md) for architecture details
3. Read [CONTRIBUTING.md](CONTRIBUTING.md) if making changes
4. Explore API endpoints via Swagger UI
5. Set up monitoring via Grafana dashboards

## Support

- **Documentation**: See README.md and DEVELOPMENT.md
- **Issues**: GitHub Issues
- **Email**: ranveer.earth@gmail.com
- **Phone**: +91 9810754471

---

**Version**: 1.0.0
**Last Updated**: June 2024
