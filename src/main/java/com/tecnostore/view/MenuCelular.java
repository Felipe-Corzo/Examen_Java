package com.tecnostore.view;

import com.tecnostore.config.ScannerSingleton;
import com.tecnostore.controller.CelularController;
import com.tecnostore.model.Celular;
import com.tecnostore.model.emuns.Gama;
import com.tecnostore.model.emuns.SistemaOperativo;

import java.util.List;

public class MenuCelular {

    private final CelularController controller;
    private final ScannerSingleton teclado;

    public MenuCelular(CelularController controller) {
        this.controller = controller;
        this.teclado = ScannerSingleton.getInstancia();
    }

    public void iniciarMenu() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = teclado.leerEntero("Seleccione una opción: ");
            procesarOpcion(opcion);
        } while (opcion != 0);
    }

    private void mostrarOpciones() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║      GESTIÓN DE CELULARES            ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Registrar Celular                ║");
        System.out.println("║  2. Listar Todos los Celulares       ║");
        System.out.println("║  3. Buscar Celular por ID            ║");
        System.out.println("║  4. Actualizar Celular               ║");
        System.out.println("║  5. Eliminar Celular                 ║");
        System.out.println("║  0. Volver al Menú Principal         ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> registrar();
            case 2 -> listar();
            case 3 -> buscar();
            case 4 -> actualizar();
            case 5 -> eliminar();
            case 0 -> System.out.println("Regresando al menú anterior...");
            default -> System.out.println("❌ Opción no válida. Intente de nuevo.");
        }
    }

    private void registrar() {
        System.out.println("\n--- REGISTRAR NUEVO CELULAR ---");
        String marca = teclado.leerTexto("Marca: ");
        String modelo = teclado.leerTexto("Modelo: ");


        double precio = pedirDecimalSeguro("Precio: ");

        int stock = teclado.leerEntero("Stock inicial: ");
        SistemaOperativo sistema_operativo= pedirSODinamico();
        Gama gama = pedirGamaDinamica();

        String respuesta = controller.registrarCelular(marca, modelo, precio, stock, sistema_operativo, gama);
        System.out.println(respuesta);
    }

    private void listar() {
        System.out.println("\n--- LISTA DE CELULARES REGISTRADOS ---");
        List<Celular> celulares = controller.listarCelulares();

        if (celulares == null || celulares.isEmpty()) {
            System.out.println("⚠ No hay celulares registrados.");
            return;
        }

        System.out.printf("%-5s | %-12s | %-15s | %-12s | %-8s | %-12s | %-10s\n",
                "ID", "MARCA", "MODELO", "PRECIO", "STOCK", "S.O.", "GAMA");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Celular c : celulares) {
            System.out.printf("%-5d | %-12s | %-15s | $%-11.2f | %-8d | %-12s | %-10s\n",
                    c.getId_celular(), c.getMarca(), c.getModelo(), c.getPrecio(), c.getStock(), c.getSistema_operativo(), c.getGama());
        }
    }

    private void buscar() {
        System.out.println("\n--- BUSCAR CELULAR ---");
        int id = teclado.leerEntero("Ingrese el ID del celular a buscar: ");

        Celular celular = controller.buscarCelular(id);
        if (celular != null) {
            System.out.println("\n✅ Celular Encontrado:\n" + celular);
        } else {
            System.out.println("❌ No se encontró ningún celular con el ID " + id);
        }
    }

    private void actualizar() {
        System.out.println("\n--- ACTUALIZAR CELULAR ---");
        int id = teclado.leerEntero("Ingrese el ID del celular que desea modificar: ");

        Celular existente = controller.buscarCelular(id);
        if (existente == null) {
            System.out.println("❌ Error: El celular con ID " + id + " no existe.");
            return;
        }

        System.out.println("\n--- Ingrese los nuevos datos ---");
        String marca = teclado.leerTexto("Nueva Marca (" + existente.getMarca() + "): ");
        String modelo = teclado.leerTexto("Nuevo Modelo (" + existente.getModelo() + "): ");

        double precio = pedirDecimalSeguro("Nuevo Precio ($" + existente.getPrecio() + "): ");

        int stock = teclado.leerEntero("Nuevo Stock (" + existente.getStock() + "): ");

        SistemaOperativo sistema_operativo = pedirSODinamico();
        Gama gama = pedirGamaDinamica();

        String respuesta = controller.actualizarCelular(id, marca, modelo, precio, stock, sistema_operativo, gama);
        System.out.println(respuesta);
    }

    private void eliminar() {
        System.out.println("\n--- ELIMINAR CELULAR ---");
        int id = teclado.leerEntero("⚠️ Ingrese el ID del celular a eliminar: ");
        String confirmacion = teclado.leerTexto("¿Está seguro? (S/N): ");

        if (confirmacion.equalsIgnoreCase("S")) {
            System.out.println(controller.eliminarCelular(id));
        } else {
            System.out.println("❌ Operación cancelada.");
        }
    }

    private SistemaOperativo pedirSODinamico() {
        SistemaOperativo[] opciones = SistemaOperativo.values();
        int seleccion;

        do {
            System.out.println("Seleccione el SistemaOperativo:");
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }
            seleccion = teclado.leerEntero("Opción: ");
        } while (seleccion < 1 || seleccion > opciones.length);

        return opciones[seleccion - 1];
    }


    private Gama pedirGamaDinamica() {
        Gama[] opciones = Gama.values();
        int seleccion;

        do {
            System.out.println("Seleccione la Gama:");
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }
            seleccion = teclado.leerEntero("Opción: ");
        } while (seleccion < 1 || seleccion > opciones.length);

        return opciones[seleccion - 1];
    }

    private double pedirDecimalSeguro(String mensaje) {
        try {
            return Double.parseDouble(teclado.leerTexto(mensaje));
        } catch (NumberFormatException e) {
            System.out.println("❌ Formato numérico inválido.");
            return -1.0;
        }
    }
}