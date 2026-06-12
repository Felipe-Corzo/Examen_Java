package com.tecnostore.view;

import com.tecnostore.config.ScannerSingleton;
import com.tecnostore.controller.ReporteController;
import com.tecnostore.service.ReporteService;

public class MenuReportes {

    private final ReporteController reporteController;
    private final ScannerSingleton teclado;

    public MenuReportes(ReporteController reporteController) {
        this.reporteController = reporteController;
        this.teclado = ScannerSingleton.getInstancia();
    }

    public void iniciarMenu() {
        int opcion;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         MÓDULO DE REPORTES           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Stock bajo (< 5 unidades)        ║");
            System.out.println("║  2. Top 3 más vendidos               ║");
            System.out.println("║  3. Ventas totales por mes           ║");
            System.out.println("║  4. Descargar Reporte Ventas (.txt)  ║"); 
            System.out.println("║  5. Reporte GENERAL                  ║"); // ← Nueva Opción
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  6. Volver al Menú Principal         ║"); // ← Cambio a opción 5
            System.out.println("╚══════════════════════════════════════╝");

            opcion = teclado.leerEntero("Seleccione Reporte: ");
            procesarOpcion(opcion);
        } while (opcion != 5); // ← Cambio a condición 5
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> mostrarStockBajo();
            case 2 -> mostrarTop3();
            case 3 -> mostrarVentasPorMes();
            case 4 -> descargarReporteVentasTXT(); // ← Enlace a la nueva función
            case 5 -> ReporteService.getInstancia().generarReporteGlobal();
            case 6 -> System.out.println("Regresando...");
            default -> System.out.println("❌ Opción inválida.");
        }
    }

    private void mostrarStockBajo() {
        System.out.println("\n⚠️ --- CELULARES CON STOCK CRÍTICO ---");
        reporteController.listarStockBajo().forEach(c ->
                System.out.printf("- %s %s | Unidades restantes: %d%n", c.getMarca(), c.getModelo(), c.getStock())
        );
    }

    private void mostrarTop3() {
        System.out.println("\n🏆 --- TOP 3 MÁS VENDIDOS ---");
        reporteController.listarTop3MasVendidos().forEach(entry ->
                System.out.printf("⭐ %s %s -> Total vendido: %d unidades%n",
                        entry.getKey().getMarca(), entry.getKey().getModelo(), entry.getValue())
        );
    }

    private void mostrarVentasPorMes() {
        System.out.println("\n📅 --- RECAUDO DE VENTAS POR MES ---");
        reporteController.listarVentasPorMes().forEach((mes, total) ->
                System.out.printf("📅 %-12s: $%,.2f%n", mes.toString(), total)
        );
    }


    private void descargarReporteVentasTXT() {
        System.out.println("\n💾 --- DESCARGAR REPORTE DE VENTAS (TXT) ---");
        String nombreArchivo = teclado.leerTexto("Ingrese el nombre para el archivo (ej: reporte_ventas): ");

        String resultado = reporteController.descargarReporteVentas(nombreArchivo);
        System.out.println(resultado);
    }
}