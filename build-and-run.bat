@echo off
set IMAGE_NAME=learnsphere-backend
set DOCKER_USERNAME=pravinsonwane

echo 🔄 Cleaning and packaging the Spring Boot project...
mvnw clean package

IF %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven build failed. Exiting...
    exit /b %ERRORLEVEL%
)

echo ✅ Maven build successful.

echo 🐳 Building Docker image...
docker build -t %DOCKER_USERNAME%/%IMAGE_NAME% .

IF %ERRORLEVEL% NEQ 0 (
    echo ❌ Docker build failed. Exiting...
    exit /b %ERRORLEVEL%
)

echo 🔐 Logging into Docker Hub...
docker login

echo 🚀 Pushing image to Docker Hub...
docker push %DOCKER_USERNAME%/%IMAGE_NAME%
