# Workflow Builder - Copilot Instructions

This workspace contains a complete Spring Boot microservices project for workflow automation.

## Project Structure

- **services/** - Individual microservices
  - common/ - Shared libraries and utilities
  - workflow-service/ - Workflow management
  - approval-service/ - Approval workflows
  - task-service/ - Task orchestration
  - notification-service/ - Event-driven notifications
  - api-gateway/ - API routing and security

- **kubernetes/** - Kubernetes manifests for production deployment
- **docker/** - Dockerfiles for containerization
- **config/** - Configuration files (Prometheus, etc.)

## Key Technologies

- Java 17, Spring Boot 3.2, Spring Cloud Gateway
- PostgreSQL, Apache Kafka
- Docker, Kubernetes
- Prometheus, Grafana for monitoring

## Getting Started

1. **Build**: `mvn clean install` or use `build.sh`
2. **Run**: `docker-compose up -d` or use `deploy.sh`
3. **Access**: 
   - API Gateway: http://localhost:8000
   - Swagger UI: http://localhost:8001/swagger-ui.html
   - Grafana: http://localhost:3000

## Common Commands

- Build all services: `mvn clean install -DskipTests`
- Start Docker services: `docker-compose up -d`
- View logs: `docker-compose logs -f <service>`
- Stop services: `docker-compose down`
- Deploy to Kubernetes: `./deploy-k8s.sh`

## API Endpoints

- **Workflow**: POST /api/v1/workflows, GET /api/v1/workflows/{id}
- **Approval**: POST /api/v1/approvals/submit, GET /api/v1/approvals/pending
- **Tasks**: POST /api/v1/tasks, GET /api/v1/tasks/assigned/{userId}
- **Notifications**: POST /api/v1/notifications/send, GET /api/v1/notifications/user/{userId}

All endpoints require JWT authentication via the API Gateway.

## Database

- Default user: workflow_user
- Default password: workflow_password
- Services auto-create schemas via Hibernate

## Next Steps

1. Configure `.env` file with your email credentials
2. Build services with `mvn clean install`
3. Start with `docker-compose up -d`
4. Test endpoints via Swagger UI or curl
