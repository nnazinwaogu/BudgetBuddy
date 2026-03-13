@echo off
chcp 65001 >nul
setlocal

echo ==============================
echo BudgetBuddy Launcher
echo ==============================
echo.

:: Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH.
    echo.
    echo Please install Java 21 or later from:
    echo   https://adoptium.net/
    echo.
    pause
    exit /b 1
)

:: Locate the JAR file
set "JAR_NAME=budget-buddy.jar"
set "JAR_PATH="

:: Check current directory
if exist "%JAR_NAME%" set "JAR_PATH=%JAR_NAME%"

:: Check target directory (if JAR not found yet)
if "%JAR_PATH%"=="" if exist "target\%JAR_NAME%" set "JAR_PATH=target\%JAR_NAME%"

:: Check parent directory (if JAR still not found)
if "%JAR_PATH%"=="" if exist "..\%JAR_NAME%" set "JAR_PATH=..\%JAR_NAME%"

if "%JAR_PATH%"=="" (
    echo ERROR: %JAR_NAME% not found.
    echo.
    echo Please ensure you are in the BudgetBuddy project directory
    echo or that the JAR file exists in the current folder or target/ subfolder.
    echo.
    echo You can build the project with:
    echo   mvnw package -DskipTests
    echo.
    pause
    exit /b 1
)

echo Starting BudgetBuddy...
echo.
java -jar "%JAR_PATH%"

if %errorlevel% neq 0 (
    echo.
    echo Application exited with error code %errorlevel%.
    echo.
    echo Note: BudgetBuddy requires Java 21 or later.
    echo If you see a JNI error, please update your Java installation.
    echo Download from: https://adoptium.net/
    echo.
    pause
    exit /b %errorlevel%
)
