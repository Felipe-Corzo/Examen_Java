package com.tecnostore.persistencia;
import com.tecnostore.model.Cliente;

import java.sql.SQLException;

public interface IClienteDAO {
    Cliente registrar(Cliente cliente) throws SQLException;
    boolean existeIdentificacion(String identificacion) throws SQLException;
    Cliente buscarPorIdentificacion(String identificacion) throws SQLException;
}
