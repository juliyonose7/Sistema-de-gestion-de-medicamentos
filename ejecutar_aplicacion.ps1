# Script PowerShell para ejecutar SistemaPedidosFarmacia con MySQL
Write-Host "========================================" -ForegroundColor Green
Write-Host "  Sistema de Pedidos de Farmacia" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

Write-Host "Compilando aplicacion..." -ForegroundColor Yellow
javac -cp "mysql-connector-java-8.0.33.jar" src\main\java\*.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error al compilar. Verifica que todos los archivos .java esten presentes." -ForegroundColor Red
    Read-Host "Presiona Enter para continuar"
    exit 1
}

Write-Host ""
Write-Host "Ejecutando aplicacion con MySQL..." -ForegroundColor Yellow
java -cp "src\main\java;mysql-connector-java-8.0.33.jar" SistemaPedidosFarmacia

Write-Host ""
Write-Host "Aplicacion cerrada." -ForegroundColor Green
Read-Host "Presiona Enter para continuar"
