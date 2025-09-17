@echo off
echo Buscando MySQL Server...
echo.

set MYSQL_PATH=""

if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" (
    set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    echo MySQL encontrado en: C:\Program Files\MySQL\MySQL Server 8.0\bin\
) else if exist "C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\mysql.exe" (
    set MYSQL_PATH="C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\mysql.exe"
    echo MySQL encontrado en: C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\
) else if exist "C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe" (
    set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe"
    echo MySQL encontrado en: C:\Program Files\MySQL\MySQL Server 8.1\bin\
) else (
    echo MySQL no encontrado en las ubicaciones comunes.
    echo Por favor, ejecuta MySQL Workbench y ejecuta manualmente el archivo crear_base_datos.sql
    pause
    exit /b 1
)

echo.
echo Creando base de datos drogueria_db...
echo Por favor, ingresa la contraseña de MySQL cuando se solicite:
echo.

%MYSQL_PATH% -u root -p < crear_base_datos.sql

if %errorlevel% equ 0 (
    echo.
    echo ¡Base de datos creada exitosamente!
    echo.
) else (
    echo.
    echo Error al crear la base de datos.
    echo Verifica que MySQL esté ejecutándose y que la contraseña sea correcta.
    echo.
)

pause
