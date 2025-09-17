# Script PowerShell para crear la base de datos MySQL
# Configuración de droguería

$mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
$password = "INSERTE_SU_CONTRASENA"

Write-Host "Intentando conectar a MySQL..."
Write-Host "Usuario: INSERTE_SU_USUARIO"
Write-Host "Password: $password"

# Intentar conectar con diferentes rutas de MySQL
$possiblePaths = @(
    "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe",
    "C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\mysql.exe",
    "mysql"
)

$mysqlFound = $false
foreach ($path in $possiblePaths) {
    if (Test-Path $path -ErrorAction SilentlyContinue) {
        $mysqlPath = $path
        $mysqlFound = $true
        Write-Host "MySQL encontrado en: $path"
        break
    }
}

if (-not $mysqlFound -and (Get-Command mysql -ErrorAction SilentlyContinue)) {
    $mysqlPath = "mysql"
    $mysqlFound = $true
    Write-Host "MySQL encontrado en PATH del sistema"
}

if (-not $mysqlFound) {
    Write-Host "ERROR: No se pudo encontrar MySQL. Asegúrate de que esté instalado."
    exit 1
}

# Crear la base de datos
$sql = @"
CREATE DATABASE IF NOT EXISTS drogueria_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE drogueria_db;
CREATE TABLE IF NOT EXISTS medicamentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    cantidad INT NOT NULL,
    distribuidor VARCHAR(100) NOT NULL,
    sucursales TEXT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
SHOW TABLES;
"@

# Ejecutar el comando
try {
    $sql | & $mysqlPath -u root --password=$password
    Write-Host "¡Base de datos creada exitosamente!"
} catch {
    Write-Host "Error al ejecutar MySQL: $_"
    Write-Host "Intentando con conexión interactiva..."
    
    # Intentar sin password automática
    Write-Host "Ejecutando MySQL interactivamente..."
    & $mysqlPath -u root -p
}