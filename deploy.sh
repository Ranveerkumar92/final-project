#!/bin/bash

# Deploy Script for Docker Compose

echo "=========================================="
echo "Starting Workflow Builder Services"
echo "=========================================="

# Load environment variables
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
else
    echo "Warning: .env file not found. Using default values."
fi

# Start services
echo "Starting Docker Compose services..."
docker-compose -f docker-compose.yml up -d

# Wait for services to be ready
echo ""
echo "Waiting for services to be ready..."
sleep 10

# Check service health
echo ""
echo "Checking service health..."
echo "Workflow Service: $(curl -s http://localhost:8001/actuator/health | jq '.status')"
echo "Approval Service: $(curl -s http://localhost:8002/actuator/health | jq '.status')"
echo "Task Service: $(curl -s http://localhost:8003/actuator/health | jq '.status')"
echo "Notification Service: $(curl -s http://localhost:8004/actuator/health | jq '.status')"
echo "API Gateway: $(curl -s http://localhost:8000/actuator/health | jq '.status')"

echo ""
echo "=========================================="
echo "Services Started Successfully!"
echo "=========================================="
echo ""
echo "Access Points:"
echo "- API Gateway: http://localhost:8000"
echo "- Workflow Service: http://localhost:8001"
echo "- Approval Service: http://localhost:8002"
echo "- Task Service: http://localhost:8003"
echo "- Notification Service: http://localhost:8004"
echo "- Grafana Dashboard: http://localhost:3000 (admin/admin)"
echo "- Prometheus: http://localhost:9090"
echo ""
echo "View logs: docker-compose logs -f"
echo "Stop services: docker-compose down"
