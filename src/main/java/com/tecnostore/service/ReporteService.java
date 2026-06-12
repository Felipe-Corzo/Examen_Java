package com.tecnostore.service;

import com.tecnostore.model.Celular;
import com.tecnostore.model.ItemVenta;
import com.tecnostore.model.Venta;
import com.tecnostore.persistencia.CelularDAOImpl;
import com.tecnostore.persistencia.CreditoDAOImpl;
import com.tecnostore.persistencia.ICelularDAO;
import com.tecnostore.persistencia.ICreditoDAO;
import com.tecnostore.persistencia.IVentaDAO;
import com.tecnostore.persistencia.VentaDAOimpl;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio encargado de generar el Reporte Global de Gestión.
 * Implementado como Singleton: solo existe una instancia encargada
 * de generar reportes en todo el sistema (SRP: esta clase solo
 * orquesta y genera reportes, delegando el acceso a datos en los
 * DAOs correspondientes -> DIP).
 */
public class ReporteService {

    private static final int STOCK_MINIMO = 5;
    private static final String NOMBRE_ARCHIVO = "reporte_global.txt";

    private static ReporteService instancia;

    // Dependencias (DIP: depende de interfaces, no de implementaciones concretas)
    private final IVentaDAO ventaDAO;
    private final ICelularDAO celularDAO;
    private final ICreditoDAO creditoDAO;

    private ReporteService() {
        this.ventaDAO = new VentaDAOimpl();
        this.celularDAO = new CelularDAOImpl();
        this.creditoDAO = new CreditoDAOImpl();
    }

    public static synchronized ReporteService getInstancia() {
        if (instancia == null) {
            instancia = new ReporteService();
        }
        return instancia;
    }

    /**
     * Genera el reporte global completo: lo imprime en consola
     * y lo guarda en reporte_global.txt
     */
    public void generarReporteGlobal() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- REPORTE GLOBAL TECNOCELL ---\n");

        try {
            sb.append(seccionTotalVentas());
            sb.append("\n");
            sb.append(seccionVentasPorModelo());
            sb.append("\n");
            sb.append(seccionCreditosPendientes());
            sb.append("\n");
            sb.append(seccionStock());

        } catch (SQLException e) {
            String mensajeError = "[ERROR] No se pudo obtener información de la base de datos: "
                    + e.getMessage() + "\n";
            sb.append(mensajeError);
            System.err.println(mensajeError);
        }

        // Mostrar en consola
        System.out.println(sb);

        // Guardar en archivo
        guardarReporteEnArchivo(sb.toString());
    }

    // ----------------------------------------------------------
    // 1. Total de ventas
    // ----------------------------------------------------------
    private String seccionTotalVentas() throws SQLException {
        List<Venta> ventas = ventaDAO.listarConDetalles();

        if (ventas.isEmpty()) {
            return "Total de ventas: No hay ventas registradas en el sistema.\n";
        }

        double totalVentas = ventas.stream()
                .mapToDouble(Venta::getTotal)
                .sum();

        return String.format("Total de ventas: $%,.0f%n", totalVentas);
    }

    // ----------------------------------------------------------
    // 2. Celulares vendidos por modelo
    // ----------------------------------------------------------
    private String seccionVentasPorModelo() throws SQLException {
        List<Venta> ventas = ventaDAO.listarConDetalles();

        List<ItemVenta> todosLosItems = ventas.stream()
                .flatMap(v -> v.getItems().stream())
                .collect(Collectors.toList());

        if (todosLosItems.isEmpty()) {
            return "Celulares vendidos por modelo: No hay ítems de venta registrados.\n";
        }

        Map<String, Integer> porModelo = todosLosItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getCelular().getModelo(),
                        Collectors.summingInt(ItemVenta::getCantidad)
                ));

        StringBuilder sb = new StringBuilder("Celulares vendidos por modelo:\n");
        for (Map.Entry<String, Integer> entry : porModelo.entrySet()) {
            sb.append(String.format("- %s: %d unidades%n", entry.getKey(), entry.getValue()));
        }

        return sb.toString();
    }

    // ----------------------------------------------------------
    // 3. Clientes con créditos pendientes
    // ----------------------------------------------------------
    private String seccionCreditosPendientes() throws SQLException {
        Map<String, Double> saldos = creditoDAO.listarSaldosPendientesPorCliente();

        if (saldos.isEmpty()) {
            return "Clientes con créditos pendientes: No hay créditos pendientes.\n";
        }

        StringBuilder sb = new StringBuilder("Clientes con créditos pendientes:\n");
        for (Map.Entry<String, Double> entry : saldos.entrySet()) {
            sb.append(String.format("- %s -> $%,.0f%n", entry.getKey(), entry.getValue()));
        }

        return sb.toString();
    }

    // ----------------------------------------------------------
    // 4. Stock actual con alertas
    // ----------------------------------------------------------
    private String seccionStock() throws SQLException {
        List<Celular> celulares = celularDAO.listar();

        if (celulares.isEmpty()) {
            return "Stock actual: No hay productos registrados.\n";
        }

        StringBuilder sb = new StringBuilder("Stock actual:\n");

        for (Celular c : celulares) {
            String alerta = c.getStock() < STOCK_MINIMO ? " (ALERTA: bajo stock)" : "";
            sb.append(String.format("- %s: %d unidades%s%n", c.getModelo(), c.getStock(), alerta));
        }

        return sb.toString();
    }

    // ----------------------------------------------------------
    // 5. Guardar reporte en archivo de texto
    // ----------------------------------------------------------
    private void guardarReporteEnArchivo(String contenido) {
        try (FileWriter writer = new FileWriter(NOMBRE_ARCHIVO)) {
            writer.write(contenido);
            System.out.println("Reporte guardado en: " + NOMBRE_ARCHIVO);
        } catch (IOException e) {
            System.err.println("[ERROR] No se pudo escribir el archivo " + NOMBRE_ARCHIVO
                    + ": " + e.getMessage());
        }
    }
}