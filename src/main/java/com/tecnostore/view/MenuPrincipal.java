package com.tecnostore.view;

import com.tecnostore.config.ScannerSingleton;
import com.tecnostore.controller.CelularController;
import com.tecnostore.controller.ClienteController;
import com.tecnostore.controller.ReporteController;
import com.tecnostore.controller.VentaController;

public class MenuPrincipal {

    private final CelularController celularController;
    private final ClienteController clienteController;
    private final VentaController   ventaController;
    private final ReporteController reporteController;
    private final ScannerSingleton  teclado;


    public MenuPrincipal(CelularController celularController,
                         ClienteController clienteController,
                         VentaController   ventaController,
                         ReporteController reporteController) {
        this.celularController = celularController;
        this.clienteController = clienteController;
        this.ventaController   = ventaController;
        this.reporteController = reporteController;
        this.teclado           = ScannerSingleton.getInstancia();
    }

    public void iniciarMenu() {
        int opcion;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║       Bienvenido a TecnoStore        ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Gestión de Clientes              ║");
            System.out.println("║  2. Gestión de Celulares             ║");
            System.out.println("║  3. Gestión de Ventas                ║");
            System.out.println("║  4. Gestión de Reportes              ║");
            System.out.println("║  0. Salir                            ║");
            System.out.println("╚══════════════════════════════════════╝");

            opcion = teclado.leerEntero("Seleccione un módulo: ");

            switch (opcion) {
                case 1 -> {
                    MenuCliente menuCliente = new MenuCliente(clienteController);
                    menuCliente.mostrar();
                }
                case 2 -> {
                    MenuCelular menuCelular = new MenuCelular(celularController);
                    menuCelular.iniciarMenu();
                }
                case 3 -> {
                    MenuVenta menuVenta = new MenuVenta(ventaController);
                    menuVenta.mostrar();
                }
                case 4 ->  {
                    MenuReportes menuReporte = new MenuReportes(reporteController);
                    menuReporte.iniciarMenu();
                }
                case 0 -> {
                    System.out.println("👋 ¡Gracias por usar TecnoStore!");
                    teclado.cerrar();
                }
                default -> System.out.println("❌ Opción no válida.");
            }
        } while (opcion != 0);
    }
}