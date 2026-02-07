@echo off
setlocal enabledelayedexpansion

REM Colors using ANSI codes
set "YELLOW=[1;33m"
set "GREEN=[0;32m"
set "RED=[0;31m"
set "NC=[0m"

echo.
echo %YELLOW%======================================%NC%
echo %YELLOW%Store API - Docker Startup (Windows)%NC%
echo %YELLOW%======================================%NC%
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo %RED%[X] Docker is not installed. Please install Docker Desktop.%NC%
    exit /b 1
)

REM Check if .env exists
if not exist .env (
    echo %YELLOW%[*] Creating .env file from .env.example...%NC%
    copy .env.example .env
    echo %GREEN%[+] .env file created. Please update it with your credentials.%NC%
)

REM Build
echo.
echo %YELLOW%[*] Building Docker image...%NC%
docker-compose build

REM Start
echo.
echo %YELLOW%[*] Starting services...%NC%
docker-compose up -d

REM Wait for database
echo %YELLOW%[*] Waiting for database...%NC%
timeout /t 10 /nobreak

REM Check health
echo %YELLOW%[*] Checking API health...%NC%
setlocal enabledelayedexpansion
for /L %%i in (1,1,30) do (
    for /f %%A in ('powershell -NoProfile -Command "try { $r = Invoke-WebRequest http://localhost:8080/actuator/health -ErrorAction SilentlyContinue; if ($r.StatusCode -eq 200) { Write-Host 'ok' } } catch { }"') do (
        if "%%A"=="ok" (
            echo %GREEN%[+] API is running!%NC%
            goto :success
        )
    )
    echo    Attempt %%i/30...
    timeout /t 2 /nobreak >nul
)

:success
echo.
echo %GREEN%======================================%NC%
echo %GREEN%[+] Application is ready!%NC%
echo %GREEN%======================================%NC%
echo.
echo API URL:     http://localhost:8080
echo Database:    localhost:3306
echo View logs:   docker-compose logs -f api
echo.
echo Quick commands:
echo   Stop:      docker-compose down
echo   Logs:      docker-compose logs -f
echo.
pause
