# Script para crear la base de datos drogueria_db
Write-Host "=== Configuración de Base de Datos MySQL ===" -ForegroundColor Green
Write-Host ""

# Solicitar contraseña de MySQL
$password = Read-Host "Ingresa la contraseña de MySQL para el usuario 'root'" -AsSecureString

Write-Host ""
Write-Host "Creando base de datos 'drogueria_db'..." -ForegroundColor Yellow

try {
    # Convertir la contraseña segura a texto plano para uso en comandos
    $plainPassword = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($password))
    
    # Crear la base de datos
    $createDbCmd = "CREATE DATABASE IF NOT EXISTS drogueria_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    $mysqlArgs = @("-u", "root", "-p$plainPassword", "-e", $createDbCmd)
    & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" $mysqlArgs
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Base de datos 'drogueria_db' creada exitosamente" -ForegroundColor Green
        
        # Ejecutar el script completo
        Write-Host "Configurando tabla y datos de ejemplo..." -ForegroundColor Yellow
        $mysqlArgs = @("-u", "root", "-p$plainPassword")
        Get-Content "setup_database.sql" | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" $mysqlArgs
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✓ Tabla 'medicamentos' creada y datos insertados" -ForegroundColor Green
            Write-Host ""
            Write-Host "¡Configuración completada exitosamente!" -ForegroundColor Green
            Write-Host "Ahora puedes ejecutar tu aplicación Java." -ForegroundColor Cyan
        } else {
            Write-Host "✗ Error al crear la tabla" -ForegroundColor Red
        }
    } else {
        Write-Host "✗ Error al crear la base de datos" -ForegroundColor Red
        Write-Host "Verifica que la contraseña sea correcta y que MySQL esté ejecutándose" -ForegroundColor Yellow
    }
} catch {
    Write-Host "✗ Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "Presiona cualquier tecla para continuar..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
