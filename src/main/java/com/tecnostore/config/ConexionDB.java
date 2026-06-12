package com.tecnostore.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://mysql-172478d7-aprendiendomysqlcorzo.b.aivencloud.com:13968/tecnostore_db?ssl-mode=REQUIRED";
    private static final String USUARIO = "avnadmin";
    private static final String PASSWORD = "";
// borraR CLAVE
    private static ConexionDB instancia;
    private Connection conexion;

    private ConexionDB() {
        conectar();
    }

    public static ConexionDB getInstancia() {

        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }


    private void conectar() {
        try {
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("[DB] Conexión establecida con tecnostore_db.");
        } catch (SQLException e) {
            throw new RuntimeException("[DB] No se pudo conectar: " + e.getMessage());
        }
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            conectar();
        }
        return conexion;
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[DB] Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error al cerrar: " + e.getMessage());
        }
    }


}
