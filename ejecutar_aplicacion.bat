@echo off
echo ========================================
echo   Sistema de Pedidos de Farmacia
echo ========================================
echo.
echo Compilando aplicacion...
javac -cp "mysql-connector-java-8.0.33.jar" src\main\java\*.java
if %errorlevel% neq 0 (
    echo Error al compilar. Verifica que todos los archivos .java esten presentes.
    pause
    exit /b 1
)

echo.
echo Ejecutando aplicacion con MySQL...
java -cp "src\main\java;mysql-connector-java-8.0.33.jar" SistemaPedidosFarmacia

echo.
echo Aplicacion cerrada.
pause
