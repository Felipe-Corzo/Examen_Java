package com.tecnostore.config;

import java.util.Scanner;

public class ScannerSingleton {


    private static ScannerSingleton instancia;


    private final Scanner scanner;


    private ScannerSingleton() {
        this.scanner = new Scanner(System.in);
    }


    public static ScannerSingleton getInstancia() {
        if (instancia == null) {
            instancia = new ScannerSingleton();
        }
        return instancia;
    }


    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Ingrese un número válido.");
            }
        }
    }

    public void cerrar() {
        if (scanner != null) {
            scanner.close();
        }
    }
}