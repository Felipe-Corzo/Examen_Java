package com.tecnostore.persistencia;

import com.tecnostore.config.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CreditoDAOImpl implements ICreditoDAO {

    @Override
    public Map<String, Double> listarSaldosPendientesPorCliente() throws SQLException {

        String sql = "SELECT cl.nombre, SUM(cr.saldoPendiente) AS saldo " +
                      "FROM creditos cr " +
                      "JOIN clientes cl ON cr.idCliente = cl.id_cliente " +
                      "WHERE cr.saldoPendiente > 0 " +
                      "GROUP BY cl.nombre";

        Map<String, Double> resultado = new LinkedHashMap<>();

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.put(rs.getString("nombre"), rs.getDouble("saldo"));
            }
        }

        return resultado;
    }
}