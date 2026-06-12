package com.tecnostore.persistencia;

import com.tecnostore.model.Venta;
import java.sql.SQLException;
import java.util.List;

public interface IVentaDAO {
    Venta registrar(Venta venta) throws SQLException;
    List<Venta> listarTodas() throws SQLException;

    List<Venta> listar() throws SQLException;

    List<Venta> listarConDetalles() throws SQLException;

}
