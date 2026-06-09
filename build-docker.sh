#!/bin/bash

# Build Docker Images Script

echo "=========================================="
echo "Building Docker Images"
echo "=========================================="

# Build images for each service
echo "Building Workflow Service image..."
docker build -f docker/Dockerfile.workflow -t workflow-builder/workflow-service:latest services/workflow-service

echo ""
echo "Building Approval Service image..."
docker build -f docker/Dockerfile.approval -t workflow-builder/approval-service:latest services/approval-service

echo ""
echo "Building Task Service image..."
docker build -f docker/Dockerfile.task -t workflow-builder/task-service:latest services/task-service

echo ""
echo "Building Notification Service image..."
docker build -f docker/Dockerfile.notification -t workflow-builder/notification-service:latest services/notification-service

echo ""
echo "Building API Gateway image..."
docker build -f docker/Dockerfile.gateway -t workflow-builder/api-gateway:latest services/api-gateway

echo ""
echo "=========================================="
echo "Docker images built successfully!"
echo "=========================================="
echo ""
echo "Built images:"
docker images | grep workflow-builder
