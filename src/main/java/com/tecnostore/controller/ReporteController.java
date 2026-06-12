package com.tecnostore.controller;

import com.tecnostore.model.Celular;
import com.tecnostore.service.GestorReporte;
import com.tecnostore.service.ReporteService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Month;
import java.util.List;
import java.util.Map;

public class ReporteController {

    private final GestorReporte reporteService;

    public ReporteController(GestorReporte reporteService) {
        this.reporteService = reporteService;
    }

    public List<Celular> listarStockBajo() {
        try {
            return reporteService.obtenerCelularesStockBajo();
        } catch (SQLException e) {
            System.err.println("❌ Error al procesar stock bajo: " + e.getMessage());
            return List.of();
        }
    }

    public List<Map.Entry<Celular, Integer>> listarTop3MasVendidos() {
        try {
            return reporteService.obtenerTop3CelularesMasVendidos();
        } catch (SQLException e) {
            System.err.println("❌ Error al procesar Top 3: " + e.getMessage());
            return List.of();
        }
    }

    public Map<Month, Double> listarVentasPorMes() {
        try {
            return reporteService.obtenerVentasTotalesPorMes();
        } catch (SQLException e) {
            System.err.println("❌ Error al procesar ventas mensuales: " + e.getMessage());
            return Map.of();
        }
    }


    public String descargarReporteVentas(String nombreArchivo) {
        try {
            // Asegurar la extensión correcta del archivo
            if (!nombreArchivo.toLowerCase().endsWith(".txt")) {
                nombreArchivo += ".txt";
            }

            // Corregido: Usamos 'reporteService' que es tu variable real de clase
            reporteService.generarReporteTXT(nombreArchivo);

            return "✅ Archivo generado exitosamente como: " + nombreArchivo;

        } catch (SQLException e) {
            return "❌ Error en la base de datos al recopilar las ventas: " + e.getMessage();
        } catch (IOException e) {
            return "❌ Error al crear o escribir el archivo físico: " + e.getMessage();
        }
    }
    
    
    public void generarReporteGlobal() {
    ReporteService.getInstancia().generarReporteGlobal();
}
}