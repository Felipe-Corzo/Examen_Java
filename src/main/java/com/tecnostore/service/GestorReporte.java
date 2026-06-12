package com.tecnostore.service;

import com.tecnostore.model.Celular;
import com.tecnostore.model.Venta;
import com.tecnostore.persistencia.ICelularDAO;
import com.tecnostore.persistencia.IVentaDAO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestorReporte {

    private final ICelularDAO celularDAO;
    private final IVentaDAO ventaDAO;


    public GestorReporte(ICelularDAO celularDAO, IVentaDAO ventaDAO) {
        this.celularDAO = celularDAO;
        this.ventaDAO = ventaDAO;
    }


    public List<Celular> obtenerCelularesStockBajo() throws SQLException {
        List<Celular> todos = celularDAO.listar();

        return todos.stream()
                .filter(c -> c.getStock() < 5)
                .collect(Collectors.toList());
    }

    public List<Map.Entry<Celular, Integer>> obtenerTop3CelularesMasVendidos() throws SQLException {
        List<Venta> todasLasVentas = ventaDAO.listarConDetalles();

        return todasLasVentas.stream()
                .flatMap(v -> v.getItems().stream())
                .collect(Collectors.groupingBy(
                        detalle -> detalle.getCelular(),
                        Collectors.summingInt(detalle -> detalle.getCantidad())
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Ordenamos de mayor a menor
                .limit(3) // Nos quedamos solo con los 3 primeros
                .collect(Collectors.toList());
    }

    public Map<Month, Double> obtenerVentasTotalesPorMes() throws SQLException {
        List<Venta> todasLasVentas = ventaDAO.listar();

        return todasLasVentas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getFecha().getMonth(),
                        Collectors.summingDouble(v -> v.getTotal())
                ));
    }


    public void generarReporteTXT(String rutaArchivo) throws SQLException, IOException {

        List<Venta> ventas = ventaDAO.listar();

        if (ventas == null || ventas.isEmpty()) {
            throw new IOException("No existen registros de ventas en el sistema para generar un reporte.");
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {

            writer.write("============================================================================\n");
            writer.write("                       TECNOSTORE - REPORTE GENERAL DE VENTAS               \n");
            writer.write("============================================================================\n");
            writer.write(String.format("%-10s | %-15s | %-25s | %-15s\n",
                    "ID VENTA", "FECHA", "CLIENTE (IDENTIFICACIÓN)", "TOTAL FACTURADO"));
            writer.write("----------------------------------------------------------------------------\n");

            double totalHistorico = 0;

            for (Venta v : ventas) {

                String infoCliente = v.getCliente().getNombre() + " (" + v.getCliente().getIdentificacion() + ")";

                writer.write(String.format("%-10d | %-15s | %-25s | $%,-14.2f\n",
                        v.getId(),
                        v.getFecha().toString(),
                        infoCliente,
                        v.getTotal()
                ));
                totalHistorico += v.getTotal();
            }

            writer.write("----------------------------------------------------------------------------\n");
            writer.write(String.format("RECAUDACIÓN TOTAL ACUMULADA: $%,.2f\n", totalHistorico));
            writer.write("============================================================================\n");
        }
    }
}