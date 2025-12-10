#!/bin/bash

# Warehouse Management System v2.1 Setup Script
# This script sets up the complete development environment

echo "🚀 Setting up Warehouse Management System v2.1"
echo "=============================================="

# Check if required tools are installed
check_requirements() {
    echo "📋 Checking requirements..."
    
    # Check Java
    if ! command -v java &> /dev/null; then
        echo "❌ Java is not installed. Please install Java 17 or higher."
        exit 1
    fi
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
        exit 1
    fi
    
    # Check Node.js
    if ! command -v node &> /dev/null; then
        echo "❌ Node.js is not installed. Please install Node.js 16 or higher."
        exit 1
    fi
    
    # Check npm
    if ! command -v npm &> /dev/null; then
        echo "❌ npm is not installed. Please install npm."
        exit 1
    fi
    
    echo "✅ All requirements satisfied"
}

# Setup database
setup_database() {
    echo "🗄️ Setting up database..."
    
    # Check if MySQL is running
    if ! pgrep mysql &> /dev/null; then
        echo "⚠️  MySQL doesn't appear to be running. Please start MySQL service."
        echo "   On Ubuntu/Debian: sudo systemctl start mysql"
        echo "   On macOS: brew services start mysql"
        echo "   On Windows: Start MySQL service from Services"
    fi
    
    echo "📝 Please create the database manually:"
    echo "   mysql -u root -p"
    echo "   CREATE DATABASE warehouse_db;"
    echo "   USE warehouse_db;"
    echo "   source backend/src/main/resources/schema.sql;"
    echo "   source backend/src/main/resources/data.sql;"
    echo ""
    read -p "Press Enter when database is ready..."
}

# Setup backend
setup_backend() {
    echo "🔧 Setting up backend..."
    cd backend
    
    # Create .env file if it doesn't exist
    if [ ! -f .env ]; then
        echo "📝 Creating .env file..."
        cat > .env << EOF
DB_HOST=localhost
DB_PORT=3306
DB_NAME=warehouse_db
DB_USER=root
DB_PASSWORD=password
SESSION_SECRET=supersecretkey123
EOF
        echo "✅ Created .env file. Please update with your database credentials."
    fi
    
    # Install dependencies
    echo "📦 Installing Maven dependencies..."
    mvn clean install -DskipTests
    
    if [ $? -eq 0 ]; then
        echo "✅ Backend setup complete"
    else
        echo "❌ Backend setup failed"
        exit 1
    fi
    
    cd ..
}

# Setup frontend
setup_frontend() {
    echo "🎨 Setting up frontend..."
    cd frontend
    
    # Install dependencies
    echo "📦 Installing npm dependencies..."
    npm install
    
    if [ $? -eq 0 ]; then
        echo "✅ Frontend setup complete"
    else
        echo "❌ Frontend setup failed"
        exit 1
    fi
    
    cd ..
}

# Create startup scripts
create_scripts() {
    echo "📜 Creating startup scripts..."
    
    # Backend startup script
    cat > start-backend.sh << 'EOF'
#!/bin/bash
echo "🚀 Starting Warehouse Management System Backend..."
cd backend
mvn spring-boot:run
EOF
    
    # Frontend startup script
    cat > start-frontend.sh << 'EOF'
#!/bin/bash
echo "🎨 Starting Warehouse Management System Frontend..."
cd frontend
npm start
EOF
    
    # Full startup script
    cat > start-all.sh << 'EOF'
#!/bin/bash
echo "🚀 Starting Warehouse Management System..."

# Start backend in background
echo "Starting backend..."
cd backend
mvn spring-boot:run &
BACKEND_PID=$!

# Wait for backend to start
echo "Waiting for backend to start..."
sleep 30

# Start frontend
echo "Starting frontend..."
cd ../frontend
npm start &
FRONTEND_PID=$!

echo "✅ System started!"
echo "Backend PID: $BACKEND_PID"
echo "Frontend PID: $FRONTEND_PID"
echo ""
echo "Backend: http://localhost:8080/api"
echo "Frontend: http://localhost:3000"
echo ""
echo "Press Ctrl+C to stop all services"

# Wait for user to stop
wait $BACKEND_PID $FRONTEND_PID
EOF
    
    # Make scripts executable
    chmod +x start-backend.sh start-frontend.sh start-all.sh
    
    echo "✅ Startup scripts created"
}

# Main setup function
main() {
    check_requirements
    setup_database
    setup_backend
    setup_frontend
    create_scripts
    
    echo ""
    echo "🎉 Setup Complete!"
    echo "=================="
    echo ""
    echo "📋 Next Steps:"
    echo "1. Update database credentials in backend/.env"
    echo "2. Create the database and run the SQL scripts"
    echo "3. Start the system:"
    echo "   - Backend only: ./start-backend.sh"
    echo "   - Frontend only: ./start-frontend.sh"
    echo "   - Both: ./start-all.sh"
    echo ""
    echo "🌐 URLs:"
    echo "   - Backend API: http://localhost:8080/api"
    echo "   - Frontend: http://localhost:3000"
    echo ""
    echo "📚 Documentation: README.md"
    echo ""
    echo "Happy coding! 🚀"
}

# Run main function
main
