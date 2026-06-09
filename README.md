# Workflow Builder Microservices

Workflow Builder is an enterprise-grade, scalable workflow automation platform built with Spring Boot microservices. It enables automated business process management, approval workflows, task orchestration, and real-time notifications.

## Project Overview

### Key Features

- **Scalable Microservices Architecture**: Independently deployable services for workflow management, approvals, task orchestration, and notifications
- **Event-Driven Processing**: Kafka-based event streaming for real-time workflow updates
- **Secure APIs**: OAuth2 and JWT authentication with Spring Security
- **RESTful Endpoints**: OpenAPI/Swagger documentation for all services
- **Database Persistence**: PostgreSQL for data consistency and transactional integrity
- **Centralized Monitoring**: Prometheus metrics collection and Grafana visualization
- **Cloud-Ready**: Docker containerization and Kubernetes deployment ready
- **High Availability**: Load balancing with API Gateway and service replication

## Microservices

### 1. **API Gateway** (Port: 8000)
- Spring Cloud Gateway for request routing and authentication
- JWT token validation
- Request/response filtering
- Service discovery and load balancing

### 2. **Workflow Service** (Port: 8001)
- Create, update, and publish workflow definitions
- Workflow lifecycle management
- Workflow instance tracking
- REST endpoints: `/api/v1/workflows`

### 3. **Approval Service** (Port: 8002)
- Manage workflow approvals and decisions
- Multi-level approval chains
- Approval tracking and history
- Kafka event publishing
- REST endpoints: `/api/v1/approvals`

### 4. **Task Service** (Port: 8003)
- Task creation, assignment, and execution
- Task status management (PENDING, IN_PROGRESS, COMPLETED)
- Scheduled task processing
- Priority-based task scheduling
- REST endpoints: `/api/v1/tasks`

### 5. **Notification Service** (Port: 8004)
- Event-driven notifications via Kafka
- Email integration (SMTP)
- SMS support (extensible)
- In-app notifications
- REST endpoints: `/api/v1/notifications`

### 6. **Common Library**
- Shared DTOs and utilities
- JWT token provider
- Exception handling
- Security configurations
- API response wrappers

## Technology Stack

- **Runtime**: Java 17
- **Framework**: Spring Boot 3.2.0
- **API Gateway**: Spring Cloud Gateway 4.0.1
- **Message Broker**: Apache Kafka 3.6.1
- **Database**: PostgreSQL 15
- **Authentication**: JWT (JSON Web Tokens)
- **Monitoring**: Prometheus + Grafana
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **Documentation**: OpenAPI 3.0 / Swagger UI
- **Build Tool**: Maven 3.9+

## Project Structure

```
workflow-builder/
├── docker/
│   ├── Dockerfile.workflow
│   ├── Dockerfile.approval
│   ├── Dockerfile.task
│   ├── Dockerfile.notification
│   └── Dockerfile.gateway
├── kubernetes/
│   ├── namespace-secrets.yaml
│   ├── workflow-service.yaml
│   ├── approval-service.yaml
│   ├── task-service.yaml
│   ├── notification-service.yaml
│   ├── api-gateway.yaml
│   └── postgres.yaml
├── config/
│   └── prometheus.yml
├── services/
│   ├── common/
│   │   └── src/main/java/com/workflow/common/
│   │       ├── dto/
│   │       ├── exception/
│   │       └── security/
│   ├── workflow-service/
│   ├── approval-service/
│   ├── task-service/
│   ├── notification-service/
│   └── api-gateway/
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Prerequisites

- Java 17 or higher
- Apache Maven 3.9+
- Docker & Docker Compose 20.10+
- PostgreSQL 15 (or use Docker)
- Kafka 3.6+ (or use Docker)
- Kubernetes cluster (optional, for production deployment)

## Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/workflow-builder.git
cd workflow-builder
```

### 2. Configure Environment
```bash
cp .env.example .env
# Update .env with your configuration
```

### 3. Build the Project
```bash
# Build all services
mvn clean install -DskipTests

# Or build specific service
cd services/workflow-service
mvn clean package -DskipTests
```

### 4. Run with Docker Compose
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### 5. Access Services

| Service | URL | Swagger UI |
|---------|-----|-----------|
| API Gateway | http://localhost:8000 | - |
| Workflow Service | http://localhost:8001 | http://localhost:8001/swagger-ui.html |
| Approval Service | http://localhost:8002 | http://localhost:8002/swagger-ui.html |
| Task Service | http://localhost:8003 | http://localhost:8003/swagger-ui.html |
| Notification Service | http://localhost:8004 | http://localhost:8004/swagger-ui.html |
| Grafana Dashboard | http://localhost:3000 | (admin/admin) |
| Prometheus | http://localhost:9090 | - |

## API Examples

### Create a Workflow
```bash
curl -X POST http://localhost:8001/api/v1/workflows \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Purchase Approval Workflow",
    "description": "Multi-level approval for purchase requests",
    "definition": "{...workflow definition...}"
  }'
```

### Submit an Approval
```bash
curl -X POST http://localhost:8002/api/v1/approvals/submit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "workflowInstanceId": 1,
    "status": "APPROVED",
    "comments": "Approved by manager"
  }'
```

### Create a Task
```bash
curl -X POST http://localhost:8003/api/v1/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "workflowInstanceId": 1,
    "name": "Prepare Invoice",
    "description": "Generate and send invoice",
    "assignedTo": "user123",
    "priority": 1
  }'
```

## Database Setup

### Automatic (Docker)
PostgreSQL is automatically initialized with the provided docker-compose.yml

### Manual Setup
```sql
-- Create databases
CREATE DATABASE workflow_db;
CREATE DATABASE approval_db;
CREATE DATABASE task_db;
CREATE DATABASE notification_db;

-- Create user
CREATE USER workflow_user WITH PASSWORD 'workflow_password';
GRANT ALL PRIVILEGES ON DATABASE workflow_db, approval_db, task_db, notification_db TO workflow_user;
```

## Kubernetes Deployment

### Prerequisites
- Kubernetes cluster (1.20+)
- kubectl configured
- Docker registry access

### Deploy to Kubernetes
```bash
# Create namespace and secrets
kubectl apply -f kubernetes/namespace-secrets.yaml

# Deploy database
kubectl apply -f kubernetes/postgres.yaml

# Deploy services
kubectl apply -f kubernetes/workflow-service.yaml
kubectl apply -f kubernetes/approval-service.yaml
kubectl apply -f kubernetes/task-service.yaml
kubectl apply -f kubernetes/notification-service.yaml
kubectl apply -f kubernetes/api-gateway.yaml

# Check deployment status
kubectl get pods -n workflow
kubectl get services -n workflow

# Access API Gateway
kubectl port-forward svc/api-gateway -n workflow 8000:8000
```

## Monitoring & Logging

### Prometheus Metrics
- All services expose metrics at `/actuator/prometheus`
- Default scrape interval: 15 seconds
- Metrics include: HTTP requests, JVM metrics, Kafka consumer lag

### Grafana Dashboards
1. Access Grafana at http://localhost:3000
2. Login with admin/admin
3. Add Prometheus data source
4. Import pre-built dashboards or create custom ones

### Logs
- Docker: `docker-compose logs <service_name>`
- Kubernetes: `kubectl logs -f <pod_name> -n workflow`
- Log files: `logs/` directory in each service container

## Security

### Authentication
- JWT tokens for API authentication
- Token validation at API Gateway
- OAuth2 integration ready (configurable)

### Authorization
- Role-based access control (RBAC)
- User context propagation across services
- Request headers validation

### Secrets Management
- Database credentials stored in Kubernetes secrets or .env
- Mail credentials stored securely
- Never commit secrets to version control

## Development

### Building Individual Services
```bash
cd services/workflow-service
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Run tests
mvn test
```

### Running Services Locally
```bash
# Terminal 1: Start Kafka and PostgreSQL
docker-compose up kafka postgres zookeeper

# Terminal 2: Run Workflow Service
cd services/workflow-service
mvn spring-boot:run

# Terminal 3: Run other services similarly
```

### IDE Setup (IntelliJ IDEA)
1. Open project as Maven project
2. Right-click on pom.xml → Maven → Reload Projects
3. Configure Run Configurations for each service
4. Set VM options: `-Dspring.profiles.active=dev`

## Troubleshooting

### Port Already in Use
```bash
# Find process using port
lsof -i :8001

# Kill process
kill -9 <PID>
```

### Database Connection Error
- Verify PostgreSQL is running: `docker-compose ps`
- Check database credentials in application.yml
- Run migrations manually if needed

### Kafka Connection Issues
- Verify Kafka broker: `docker-compose logs kafka`
- Check bootstrap servers configuration
- Test connectivity: `nc -zv kafka 9092`

### JWT Token Validation Fails
- Verify token format (Bearer <token>)
- Check token expiration time
- Validate JWT secret matches

## Performance Tuning

### Database Optimization
- Enable connection pooling (HikariCP)
- Create indexes on frequently queried columns
- Implement query caching

### Kafka Optimization
- Adjust batch sizes and linger time
- Configure replication factors
- Monitor consumer lag

### Service Scaling
- Increase replicas in Kubernetes
- Implement horizontal pod autoscaling
- Use load balancing at API Gateway level

## Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Submit pull request

## Future Enhancements

- AI-based workflow recommendations
- Low-code workflow configuration UI
- Advanced analytics dashboards
- Multi-tenant support
- Workflow versioning and rollback
- Advanced scheduling and cron support
- WebSocket for real-time updates
- GraphQL API support

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Support

For issues and questions:
- GitHub Issues: https://github.com/yourusername/workflow-builder/issues
- Email: ranveer.earth@gmail.com
- Phone: +91 9810754471

## Contributors

- Ranveer Singh - Lead Developer

---

**Last Updated**: June 2024
**Version**: 1.0.0
