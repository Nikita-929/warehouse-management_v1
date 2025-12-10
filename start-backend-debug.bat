@echo off
echo Starting Warehouse Management System Backend with debug output...
cd backend
echo Building application...
mvn clean package -DskipTests
if errorlevel 1 (
    echo Build failed!
    pause
    exit /b 1
)
echo Starting application...
java -jar target/warehouse-management-1.0.0.jar
pause

