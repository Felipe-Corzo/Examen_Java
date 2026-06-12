package com.tecnostore.persistencia;

import java.sql.SQLException;
import java.util.Map;

public interface ICreditoDAO {

    /**
     * Devuelve un mapa con el nombre del cliente y la suma de sus
     * saldos pendientes (solo clientes con saldoPendiente > 0).
     */
    Map<String, Double> listarSaldosPendientesPorCliente() throws SQLException;
}