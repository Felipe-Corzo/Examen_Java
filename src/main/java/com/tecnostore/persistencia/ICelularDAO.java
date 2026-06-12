package com.tecnostore.persistencia;

import com.tecnostore.model.Celular;
import java.sql.SQLException;
import java.util.List;

public interface ICelularDAO {

    Celular registrar(Celular celular) throws SQLException;

    void actualizar(Celular celular) throws SQLException;

    void eliminar(int id) throws SQLException;

    List<Celular> listar() throws SQLException;

    Celular buscarPorId(int id) throws SQLException;
}