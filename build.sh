#!/bin/bash

# Build Script for Workflow Builder Microservices

set -e

echo "=========================================="
echo "Building Workflow Builder Microservices"
echo "=========================================="

# Build parent project
echo "Building parent project..."
mvn clean install -DskipTests

# Build and package all services
echo ""
echo "Building Common Library..."
cd services/common
mvn clean package -DskipTests
cd ../..

echo ""
echo "Building Workflow Service..."
cd services/workflow-service
mvn clean package -DskipTests
cd ../..

echo ""
echo "Building Approval Service..."
cd services/approval-service
mvn clean package -DskipTests
cd ../..

echo ""
echo "Building Task Service..."
cd services/task-service
mvn clean package -DskipTests
cd ../..

echo ""
echo "Building Notification Service..."
cd services/notification-service
mvn clean package -DskipTests
cd ../..

echo ""
echo "Building API Gateway..."
cd services/api-gateway
mvn clean package -DskipTests
cd ../..

echo ""
echo "=========================================="
echo "Build completed successfully!"
echo "=========================================="
