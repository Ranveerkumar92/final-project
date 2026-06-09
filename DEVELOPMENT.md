# Workflow Builder - Development Guide

## Architecture Overview

### Microservices Pattern
Each service is independently deployable with its own database (Database per Service Pattern).

### Event-Driven Communication
- **Kafka Topics**: 
  - `approval-events` - Approval decisions
  - `task-events` - Task state changes
  - `workflow-events` - Workflow events

### API Gateway Pattern
- Single entry point at `http://localhost:8000`
- Handles authentication, routing, and rate limiting
- Services communicate via REST APIs and Kafka events

## Service Details

### Workflow Service
**Responsibility**: Define and manage workflows
- Create workflow templates
- Publish workflows
- Track workflow instances
- Database: `workflow_db`

### Approval Service
**Responsibility**: Handle approval workflows
- Submit approval decisions
- Track approval history
- Publish approval events to Kafka
- Database: `approval_db`

### Task Service
**Responsibility**: Task management and orchestration
- Create and assign tasks
- Update task status
- Support scheduled task execution
- Database: `task_db`

### Notification Service
**Responsibility**: Event-driven notifications
- Listen to workflow events
- Send email notifications
- Track notification delivery status
- Database: `notification_db`

### API Gateway
**Responsibility**: Request routing and authentication
- JWT token validation
- Route requests to appropriate services
- Handle cross-cutting concerns
- No persistent storage

## Security Architecture

### Authentication Flow
1. Client sends credentials
2. API Gateway validates and generates JWT token
3. Client includes JWT in Authorization header
4. Token is validated at each service

### Token Structure
```
Header: {
  "alg": "HS512",
  "typ": "JWT"
}
Payload: {
  "sub": "userId",
  "username": "username",
  "roles": ["ROLE_USER"],
  "iat": 1234567890,
  "exp": 1234654290
}
```

## Data Flow Examples

### Create and Execute Workflow
```
1. POST /workflows (Workflow Service)
2. Service creates workflow in workflow_db
3. Returns workflow ID
4. Client publishes workflow (POST /workflows/{id}/publish)
5. Workflow status changes to PUBLISHED
```

### Approval Process
```
1. Workflow instance created
2. Approval request triggered
3. Notification Service listens to approval-events topic
4. Email sent to approver
5. Approver submits decision via POST /approvals/submit
6. Approval event published to Kafka
7. Workflow continues based on decision
```

### Task Execution
```
1. Workflow creates task via POST /tasks
2. Task assigned to user
3. Task Service publishes task-events
4. Notification Service sends notification
5. User updates task status via PATCH /tasks/{id}/status
6. Workflow continues upon task completion
```

## Configuration Management

### Environment Variables (from .env)
- Database credentials
- Kafka brokers
- Mail server details
- OAuth2 settings
- JWT secret and expiration

### Application Properties
- Located in `src/main/resources/application.yml`
- Service-specific configurations
- Logging levels
- Actuator endpoints

## Testing

### Unit Tests
```bash
cd services/workflow-service
mvn test
```

### Integration Tests
- Test service to database connectivity
- Test Kafka producer/consumer
- Test REST endpoints

### End-to-End Tests
- Test complete workflow from creation to completion
- Test API Gateway routing
- Test authentication and authorization

## Deployment Strategies

### Development (Docker Compose)
```bash
docker-compose up -d
```
- All services run in containers
- Shared network bridge
- Database and Kafka included
- Suitable for local development

### Production (Kubernetes)
```bash
./deploy-k8s.sh
```
- High availability with replicas
- Auto-scaling capabilities
- Self-healing pods
- Rolling updates support
- Persistent volumes for data

## Monitoring and Observability

### Prometheus Metrics
- HTTP request metrics (count, duration, errors)
- JVM metrics (memory, GC, threads)
- Kafka consumer lag
- Database connection pool status

### Grafana Dashboards
- Service health overview
- Request rate and latency
- Error rate trends
- Resource utilization

### Centralized Logging
- View logs via: `docker-compose logs -f <service>`
- Or: `kubectl logs -f <pod> -n workflow`

## Performance Considerations

### Database
- Use connection pooling (HikariCP)
- Implement indexes on foreign keys
- Pagination for large queries

### Kafka
- Batch size tuning for producer
- Consumer group scaling
- Partition count for parallelism

### API Gateway
- Enable response caching
- Implement circuit breakers
- Rate limiting per client

## Troubleshooting Guide

### Service won't start
1. Check database connectivity
2. Verify port availability
3. Review application logs
4. Check environment variables

### Kafka connection issues
1. Verify Kafka broker is running
2. Check bootstrap servers config
3. Test connectivity: `nc -zv kafka 9092`

### Database errors
1. Verify database is running
2. Check credentials in .env
3. Verify schema migrations
4. Check disk space

## Code Standards

### Naming Conventions
- Classes: PascalCase (e.g., WorkflowService)
- Methods: camelCase (e.g., createWorkflow)
- Constants: UPPER_SNAKE_CASE
- Packages: lowercase with dots

### Code Organization
- Entity classes in `entity/` package
- DTOs in `dto/` package
- Services in `service/` package
- Controllers in `controller/` package
- Repositories in `repository/` package

### Documentation
- Use JavaDoc for public methods
- Add comments for complex logic
- Update README for configuration changes

## Future Enhancements

- API rate limiting and throttling
- Request tracing with Spring Cloud Sleuth
- Distributed tracing with Jaeger
- CQRS pattern implementation
- Saga pattern for distributed transactions
- Event sourcing
- GraphQL API layer
- Multi-tenant support

---

**Last Updated**: June 2024
