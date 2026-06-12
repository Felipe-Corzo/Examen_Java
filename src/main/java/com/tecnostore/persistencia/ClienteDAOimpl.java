package com.tecnostore.persistencia;

import com.tecnostore.model.Cliente;
import com.tecnostore.config.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ClienteDAOimpl implements IClienteDAO {
    private Connection getConexion() {
        return ConexionDB.getInstancia().getConexion();
    }


    @Override
    public Cliente registrar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, identificacion, correo, telefono) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = getConexion()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getIdentificacion());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    cliente.setId(keys.getInt(1));
                }
            }
        }
        return cliente;
    }

    @Override
    public boolean existeIdentificacion(String identificacion) throws SQLException {
        String sql = "SELECT 1 FROM clientes WHERE identificacion = ?";

        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Cliente buscarPorIdentificacion(String identificacion) throws SQLException {
        String sql = "SELECT id_cliente, nombre, identificacion, correo, telefono " +
                "FROM clientes WHERE identificacion = ?";

        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("identificacion"),
                            rs.getString("correo"),
                            rs.getString("telefono")
                    );
                }
            }
        }
        return null;
    }

}
