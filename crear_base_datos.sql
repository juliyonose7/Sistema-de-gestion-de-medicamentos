-- Script para crear la base de datos de droguería
-- Ejecutar este script en MySQL antes de usar la aplicación

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS drogueria_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE drogueria_db;

-- Crear la tabla de medicamentos
CREATE TABLE IF NOT EXISTS medicamentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    cantidad INT NOT NULL,
    distribuidor VARCHAR(100) NOT NULL,
    sucursales TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_nombre (nombre),
    INDEX idx_tipo (tipo),
    INDEX idx_distribuidor (distribuidor),
    INDEX idx_fecha (fecha)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar algunos datos de ejemplo
INSERT INTO medicamentos (nombre, tipo, cantidad, distribuidor, sucursales) VALUES
('Paracetamol', 'Analgésico', 100, 'Cofarma', 'Principal, Secundaria'),
('Ibuprofeno', 'Analgésico', 75, 'Empsephar', 'Principal'),
('Omeprazol', 'Antiácido', 50, 'Cemefar', 'Secundaria'),
('Amoxicilina', 'Antibiótico', 30, 'Cofarma', 'Principal, Secundaria'),
('Loratadina', 'Antialérgico', 60, 'Empsephar', 'Principal');

-- Mostrar los datos insertados
SELECT * FROM medicamentos;

-- Mostrar información de la tabla
DESCRIBE medicamentos;
