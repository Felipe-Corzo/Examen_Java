package com.tecnostore.service;

import com.tecnostore.model.Cliente;
import com.tecnostore.persistencia.IClienteDAO;
import com.tecnostore.utils.ValidadorCliente;
import java.sql.SQLException;

public class GestorClientes {

    private final IClienteDAO clienteDAO;

    public GestorClientes(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public Cliente registrarCliente(String nombre, String identificacion,
                                    String correo, String telefono) {
        ValidadorCliente.validarCampos(nombre, identificacion, correo, telefono);

        try {
            if (clienteDAO.existeIdentificacion(identificacion)) {
                throw new IllegalArgumentException("❌ Ya existe un cliente con la identificación: " + identificacion);
            }

            Cliente nuevo = new Cliente(nombre.trim(), identificacion.trim(),
                    correo.trim(),  telefono.trim());
            return clienteDAO.registrar(nuevo);

        } catch (SQLException e) {
            throw new RuntimeException("❌ Error al registrar cliente: " + e.getMessage());
        }
    }
}
