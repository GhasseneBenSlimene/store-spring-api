#!/bin/bash

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}======================================${NC}"
echo -e "${YELLOW}Store API - Docker Startup${NC}"
echo -e "${YELLOW}======================================${NC}"
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

# Check if docker-compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}❌ docker-compose is not installed. Please install docker-compose first.${NC}"
    exit 1
fi

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    echo -e "${YELLOW}📄 Creating .env file from .env.example...${NC}"
    cp .env.example .env
    echo -e "${GREEN}✅ .env file created. Please update it with your credentials.${NC}"
fi

# Build and start
echo -e "${YELLOW}🔨 Building Docker image...${NC}"
docker-compose build

echo ""
echo -e "${YELLOW}🚀 Starting services...${NC}"
docker-compose up -d

# Wait for database
echo -e "${YELLOW}⏳ Waiting for database to be ready...${NC}"
sleep 10

# Check if API is running
echo -e "${YELLOW}🔍 Checking API health...${NC}"
for i in {1..30}; do
    if curl -s http://localhost:8080/actuator/health > /dev/null; then
        echo -e "${GREEN}✅ API is running!${NC}"
        break
    fi
    echo "   Attempt $i/30..."
    sleep 2
done

echo ""
echo -e "${GREEN}======================================${NC}"
echo -e "${GREEN}🎉 Application is ready!${NC}"
echo -e "${GREEN}======================================${NC}"
echo ""
echo -e "API URL:    ${YELLOW}http://localhost:8080${NC}"
echo -e "Database:   ${YELLOW}localhost:3306${NC}"
echo -e "Logs:       ${YELLOW}docker-compose logs -f api${NC}"
echo ""
echo -e "${YELLOW}Quick commands:${NC}"
echo "  View logs:      docker-compose logs -f"
echo "  Stop:           docker-compose down"
echo "  Pull latest DB: docker-compose pull database"
echo ""
