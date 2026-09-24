@echo off
where gradle >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Gradle is not installed or not available in PATH.
    echo Install Gradle 8.10+ and retry.
    exit /b 1
)

gradle %*
