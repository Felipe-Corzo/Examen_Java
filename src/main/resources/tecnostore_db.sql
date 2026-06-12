CREATE DATABASE IF NOT EXISTS tecnostore_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE tecnostore_db;

CREATE TABLE clientes (
    id_cliente     INT UNSIGNED    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(255)    NOT NULL,
    identificacion VARCHAR(20)     NOT NULL UNIQUE,
    correo         VARCHAR(255)    NOT NULL,
    telefono       VARCHAR(50)     NOT NULL
);

CREATE TABLE celulares (
    id_celular         INT UNSIGNED    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    marca              VARCHAR(255)    NOT NULL,
    modelo             VARCHAR(255)    NOT NULL,
    sistema_operativo  ENUM( 'Android', 'iOS', 'Windows_Phone') NOT NULL,
    gama               ENUM('alta','media','baja') NOT NULL,
    precio             DECIMAL(8,2)    NOT NULL,
    stock              INT             NOT NULL,
    activo             BOOLEAN         DEFAULT TRUE
);

CREATE TABLE ventas (
    id_venta   INT UNSIGNED  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT UNSIGNED  NOT NULL,
    fecha      DATE          NOT NULL,
    total      DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

CREATE TABLE detalle_ventas (
    id_detalle_venta INT UNSIGNED  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_venta         INT UNSIGNED  NOT NULL,
    id_celular       INT UNSIGNED  NOT NULL,
    cantidad         INT           NOT NULL,
    subtotal         DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta)   REFERENCES ventas(id_venta),
    FOREIGN KEY (id_celular) REFERENCES celulares(id_celular)
);