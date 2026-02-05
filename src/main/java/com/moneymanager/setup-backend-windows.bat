@echo off
echo ================================
echo Money Manager Backend Setup
echo ================================
echo.

echo [1/3] Building backend...
echo This may take 2-3 minutes...
echo.
call mvnw.cmd clean install
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Build failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo ================================
echo Build Successful!
echo ================================
echo.

echo [2/3] Starting backend server...
echo.
echo The server will start on: http://localhost:8080
echo.
echo IMPORTANT: Keep this window open while using the application!
echo To stop the server, press Ctrl+C
echo.

pause

echo [3/3] Starting server...
call mvnw.cmd spring-boot:run
