#!/bin/bash

# Kubernetes Deployment Script

echo "=========================================="
echo "Deploying to Kubernetes"
echo "=========================================="

NAMESPACE="workflow"

# Create namespace
echo "Creating namespace..."
kubectl apply -f kubernetes/namespace-secrets.yaml

# Deploy PostgreSQL
echo "Deploying PostgreSQL..."
kubectl apply -f kubernetes/postgres.yaml

# Wait for PostgreSQL to be ready
echo "Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=ready pod -l app=postgres -n $NAMESPACE --timeout=300s

# Deploy services
echo ""
echo "Deploying Workflow Service..."
kubectl apply -f kubernetes/workflow-service.yaml

echo ""
echo "Deploying Approval Service..."
kubectl apply -f kubernetes/approval-service.yaml

echo ""
echo "Deploying Task Service..."
kubectl apply -f kubernetes/task-service.yaml

echo ""
echo "Deploying Notification Service..."
kubectl apply -f kubernetes/notification-service.yaml

echo ""
echo "Deploying API Gateway..."
kubectl apply -f kubernetes/api-gateway.yaml

echo ""
echo "=========================================="
echo "Deployment in progress!"
echo "=========================================="
echo ""
echo "Check pod status:"
echo "kubectl get pods -n $NAMESPACE"
echo ""
echo "View pod logs:"
echo "kubectl logs -f <pod-name> -n $NAMESPACE"
echo ""
echo "Access API Gateway:"
echo "kubectl port-forward svc/api-gateway -n $NAMESPACE 8000:8000"
