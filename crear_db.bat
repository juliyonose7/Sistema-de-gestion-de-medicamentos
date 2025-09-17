@echo off
echo Creando base de datos drogueria_db...
echo.
echo Por favor, ingresa la contraseña de MySQL cuando se solicite:
echo.

"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p < crear_base_datos.sql

if %errorlevel% equ 0 (
    echo.
    echo ¡Base de datos creada exitosamente!
    echo.
) else (
    echo.
    echo Error al crear la base de datos. Verifica que MySQL esté instalado correctamente.
    echo.
)

pause
