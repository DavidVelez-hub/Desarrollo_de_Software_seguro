CREATE DATABASE IF NOT EXISTS gestion_funcionarios;
USE gestion_funcionarios;

-- 1. Tablas auxiliares (usando el nombre como PK para que sea intuitivo)
CREATE TABLE IF NOT EXISTS tipo_documento (
    nombre VARCHAR(50) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS estado_civil (
    nombre VARCHAR(50) PRIMARY KEY
);

-- 2. Tabla Principal
CREATE TABLE IF NOT EXISTS funcionarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_documento VARCHAR(20) UNIQUE NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    tipo_documento_id VARCHAR(50), -- Ahora guarda el nombre directamente o como FK
    estado_civil_id VARCHAR(50),    -- Ahora guarda el nombre directamente o como FK
    fecha_nacimiento DATE,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200),
    fecha_ingreso DATE,
    cargo VARCHAR(100),
    FOREIGN KEY (tipo_documento_id) REFERENCES tipo_documento(nombre),
    FOREIGN KEY (estado_civil_id) REFERENCES estado_civil(nombre)
);

-- 3. Datos de prueba (Poblado)
INSERT IGNORE INTO tipo_documento (nombre) VALUES ('Cédula'), ('Pasaporte'), ('NIT');
INSERT IGNORE INTO estado_civil (nombre) VALUES ('Soltero'), ('Casado'), ('Divorciado'), ('Unión Libre');

INSERT INTO funcionarios (numero_documento, nombre_completo, tipo_documento_id, estado_civil_id, fecha_nacimiento, telefono, email, direccion, fecha_ingreso, cargo)
VALUES 
('123456789', 'Juan Pérez', 'Cédula', 'Casado', '1990-01-01', '3001234567', 'juan@test.com', 'Calle 123', '2023-01-15', 'Analista'),
('987654321', 'Maria Gomez', 'Cédula', 'Soltero', '1995-05-20', '3109876543', 'maria@test.com', 'Carrera 45', '2024-03-10', 'Diseñadora');
