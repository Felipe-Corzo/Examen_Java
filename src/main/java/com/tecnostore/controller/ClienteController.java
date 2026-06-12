package com.tecnostore.controller;

import com.tecnostore.model.Cliente;
import com.tecnostore.service.GestorClientes;
import com.tecnostore.config.ScannerSingleton;

public class ClienteController {

    private final GestorClientes gestorClientes;
    private final ScannerSingleton entrada;

    public ClienteController(GestorClientes gestorClientes) {
        this.gestorClientes = gestorClientes;
        this.entrada = ScannerSingleton.getInstancia();
    }

    public void registrar() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       Registrar Nuevo Cliente        ║");
        System.out.println("╚══════════════════════════════════════╝");

        String nombre         = entrada.leerTexto("--> Nombre:         ");
        String identificacion = entrada.leerTexto("--> Identificación: ");
        String correo         = entrada.leerTexto("--> Correo:         ");
        String telefono       = entrada.leerTexto("--> Teléfono:       ");

        try {
            Cliente c = gestorClientes.registrarCliente(nombre, identificacion,
                    correo, telefono);
            System.out.println("\n✅ Cliente registrado con éxito (ID: " + c.getId() + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Validación: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("\n❌ Error: " + e.getMessage());
        }
    }
}
