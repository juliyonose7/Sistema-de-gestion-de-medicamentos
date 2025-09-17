@echo off
echo Ejecutando Sistema de Drogueria con MySQL...
echo.

cd /d "%~dp0"

echo Verificando archivos necesarios...
if not exist "mysql-connector-j-8.0.33.jar" (
    echo ERROR: No se encontro mysql-connector-j-8.0.33.jar
    pause
    exit /b 1
)

if not exist "mysql.properties" (
    echo ERROR: No se encontro mysql.properties
    pause
    exit /b 1
)

echo Compilando archivos Java...
cd src\main\java
javac -cp "..\..\..\mysql-connector-j-8.0.33.jar" *.java
if %errorlevel% neq 0 (
    echo ERROR: Fallo la compilacion
    cd ..\..\..
    pause
    exit /b 1
)

cd ..\..\..

echo Ejecutando aplicacion...
java -cp "mysql-connector-j-8.0.33.jar;src\main\java" SistemaPedidosFarmacia

echo.
echo Aplicacion cerrada.
pause