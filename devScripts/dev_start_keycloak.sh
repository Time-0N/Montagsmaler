#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
CONTAINER_NAME="keycloak_dev"
KEYCLOAK_IMAGE="quay.io/keycloak/keycloak:24.0.2"
PORT="8080"
ADMIN_USER="admin"
ADMIN_PASSWORD="admin"

# Function to check if Docker is installed
check_docker() {
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}Docker is not installed. Please install Docker first.${NC}"
        exit 1
    fi
}

# Function to check if container exists
container_exists() {
    docker ps -a --filter "name=$CONTAINER_NAME" | grep -w "$CONTAINER_NAME" > /dev/null
}

# Function to check if container is running
container_running() {
    docker ps --filter "name=$CONTAINER_NAME" --filter "status=running" | grep -w "$CONTAINER_NAME" > /dev/null
}

# Function to start existing container
start_existing_container() {
    echo -e "${YELLOW}Starting existing Keycloak container...${NC}"
    docker start $CONTAINER_NAME
}

# Function to setup new container
setup_new_container() {
    echo -e "${YELLOW}Setting up new Keycloak container...${NC}"
    docker run -d \
        --name $CONTAINER_NAME \
        -p $PORT:8080 \
        -e KEYCLOAK_ADMIN=$ADMIN_USER \
        -e KEYCLOAK_ADMIN_PASSWORD=$ADMIN_PASSWORD \
        -v keycloak_dev_data:/opt/keycloak/data \
        $KEYCLOAK_IMAGE \
        start-dev
}

# Main execution
set -e

check_docker

if container_running; then
    echo -e "${GREEN}Keycloak container is already running.${NC}"
    exit 0
fi

if container_exists; then
    start_existing_container
else
    setup_new_container
fi

# Wait for container to start
sleep 5

if container_running; then
    echo -e "${GREEN}Keycloak setup complete!${NC}"
    echo -e "\nAccess Admin Console: http://localhost:$PORT/admin"
    echo -e "Username: ${ADMIN_USER}"
    echo -e "Password: ${ADMIN_PASSWORD}"
else
    echo -e "${RED}Failed to start Keycloak container. Check logs with: docker logs $CONTAINER_NAME${NC}"
    exit 1
fi