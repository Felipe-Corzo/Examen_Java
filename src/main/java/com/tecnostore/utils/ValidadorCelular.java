package com.tecnostore.utils;

public class ValidadorCelular {


    public static boolean esValido(double precio, int stock) {
        if (precio < 0) {
            System.out.println("❌ Error de Validación: El precio no puede ser un valor negativo.");
            return false;
        }

        if (stock < 0) {
            System.out.println("❌ Error de Validación: El stock disponible no puede ser un valor negativo.");
            return false;
        }

        return true;
    }
}