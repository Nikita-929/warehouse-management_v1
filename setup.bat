@echo off
echo 🚀 Setting up Warehouse Management System v2.1
echo ==============================================

echo 📋 Checking requirements...

REM Check Java
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Java is not installed. Please install Java 17 or higher.
    pause
    exit /b 1
)

REM Check Maven
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Maven is not installed. Please install Maven 3.6 or higher.
    pause
    exit /b 1
)

REM Check Node.js
node -v >nul 2>&1
if errorlevel 1 (
    echo ❌ Node.js is not installed. Please install Node.js 16 or higher.
    pause
    exit /b 1
)

REM Check npm
npm -v >nul 2>&1
if errorlevel 1 (
    echo ❌ npm is not installed. Please install npm.
    pause
    exit /b 1
)

echo ✅ All requirements satisfied

echo 🗄️ Setting up database...
echo ⚠️  Please ensure MySQL is running and create the database manually:
echo    mysql -u root -p
echo    CREATE DATABASE warehouse_db;
echo    USE warehouse_db;
echo    source backend/src/main/resources/schema.sql;
echo    source backend/src/main/resources/data.sql;
echo.
pause

echo 🔧 Setting up backend...
cd backend

REM Create .env file if it doesn't exist
if not exist .env (
    echo 📝 Creating .env file...
    echo DB_HOST=localhost > .env
    echo DB_PORT=3306 >> .env
    echo DB_NAME=warehouse_db >> .env
    echo DB_USER=root >> .env
    echo DB_PASSWORD=password >> .env
    echo SESSION_SECRET=supersecretkey123 >> .env
    echo ✅ Created .env file. Please update with your database credentials.
)

REM Install dependencies
echo 📦 Installing Maven dependencies...
mvn clean install -DskipTests

if errorlevel 1 (
    echo ❌ Backend setup failed
    pause
    exit /b 1
)

echo ✅ Backend setup complete
cd ..

echo 🎨 Setting up frontend...
cd frontend

REM Install dependencies
echo 📦 Installing npm dependencies...
npm install

if errorlevel 1 (
    echo ❌ Frontend setup failed
    pause
    exit /b 1
)

echo ✅ Frontend setup complete
cd ..

echo 📜 Creating startup scripts...

REM Backend startup script
echo @echo off > start-backend.bat
echo echo 🚀 Starting Warehouse Management System Backend... >> start-backend.bat
echo cd backend >> start-backend.bat
echo mvn spring-boot:run >> start-backend.bat

REM Frontend startup script
echo @echo off > start-frontend.bat
echo echo 🎨 Starting Warehouse Management System Frontend... >> start-frontend.bat
echo cd frontend >> start-frontend.bat
echo npm start >> start-frontend.bat

echo ✅ Startup scripts created

echo.
echo 🎉 Setup Complete!
echo ==================
echo.
echo 📋 Next Steps:
echo 1. Update database credentials in backend\.env
echo 2. Create the database and run the SQL scripts
echo 3. Start the system:
echo    - Backend only: start-backend.bat
echo    - Frontend only: start-frontend.bat
echo.
echo 🌐 URLs:
echo    - Backend API: http://localhost:8080/api
echo    - Frontend: http://localhost:3000
echo.
echo 📚 Documentation: README.md
echo.
echo Happy coding! 🚀
pause
