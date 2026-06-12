package com.tecnostore.controller;

import com.tecnostore.config.ScannerSingleton;
import com.tecnostore.model.*;
import com.tecnostore.persistencia.*;
import com.tecnostore.service.GestorVentas;

import java.util.ArrayList;
import java.util.List;

public class VentaController {

    private final GestorVentas    gestorVentas;
    private final ScannerSingleton entrada;

    public VentaController(GestorVentas gestorVentas) {
        this.gestorVentas = gestorVentas;
        this.entrada = ScannerSingleton.getInstancia();
    }

    public void registrarVenta() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║        Registrar Nueva Venta         ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Buscar cliente
        String identificacion = entrada.leerTexto("--> Identificación del cliente: ");
        Cliente cliente = gestorVentas.buscarCliente(identificacion);

        if (cliente == null) {
            System.out.println("❌ No se encontró cliente con esa identificación.");
            return;
        }
        System.out.println("✅ Cliente: " + cliente.getNombre());

        // Agregar ítems
        List<ItemVenta> items = new ArrayList<>();
        System.out.println("\n[Ingrese celulares — ID 0 para terminar]");

        while (true) {
            int idCelular = entrada.leerEntero("--> ID del celular: ");
            if (idCelular == 0) {
                if (items.isEmpty()) {
                    System.out.println("⚠️ Debe agregar al menos un celular.");
                    continue;
                }
                break;
            }

            Celular celular = gestorVentas.buscarCelular(idCelular);
            if (celular == null) {
                System.out.println("❌ Celular no encontrado.");
                continue;
            }

            int cantidad = entrada.leerEntero("--> Cantidad: ");
            double subtotal = celular.getPrecio() * cantidad;
            items.add(new ItemVenta(celular, cantidad, subtotal));
        }

        // Resumen de venta
        double subtotalTotal = items.stream().mapToDouble(ItemVenta::getSubtotal).sum();
        double iva   = subtotalTotal * 0.19;
        double total = subtotalTotal + iva;

        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("          RESUMEN DE VENTA");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        for (ItemVenta item : items) {
            System.out.printf("  %-28s x%d  $%.2f%n",
                    item.getCelular().getMarca() + " " + item.getCelular().getModelo(),
                    item.getCantidad(), item.getSubtotal());
        }
        System.out.println("──────────────────────────────────────");
        System.out.printf("  Subtotal:       $%.2f%n", subtotalTotal);
        System.out.printf("  IVA (19%%):      $%.2f%n", iva);
        System.out.printf("  TOTAL:          $%.2f%n", total);
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Confirmación
        String confirm = entrada.leerTexto("¿Confirmar venta? (s/n): ");
        if (!confirm.equalsIgnoreCase("s")) {
            System.out.println("⚠️ Venta cancelada.");
            return;
        }

        try {
            Venta venta = gestorVentas.registrarVenta(cliente, items);
            System.out.println("\n✅ Venta registrada con éxito (ID: " + venta.getId() + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Error en validación: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("\n❌ Error al guardar: " + e.getMessage());
        }
    }
}