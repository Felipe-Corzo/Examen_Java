package com.tecnostore.utils;

public class ValidadorCliente {

    private static final String REGEX_CORREO = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
    private static final String REGEX_IDENTIFICACION = "^[0-9]{6,15}$";
    private static final String REGEX_TELEFONO       = "^[0-9]{7,15}$";

    public static boolean esCorreoValido(String correo) {
        if (correo == null || correo.isBlank()) return false;
        return correo.trim().matches(REGEX_CORREO);          // ← sin Pattern
    }

    public static boolean esIdentificacionValida(String identificacion) {
        if (identificacion == null || identificacion.isBlank()) return false;
        return identificacion.trim().matches(REGEX_IDENTIFICACION);
    }

    public static boolean esTelefonoValido(String telefono) {
        if (telefono == null || telefono.isBlank()) return false;
        return telefono.trim().matches(REGEX_TELEFONO);
    }

    public static boolean esNombreValido(String nombre) {
        return nombre != null && nombre.trim().length() >= 2;
    }

    public static void validarCampos(String nombre, String identificacion, String correo, String telefono) {
        if (!esNombreValido(nombre)) {
            throw new IllegalArgumentException("❌ El nombre debe tener al menos 2 caracteres.");
        }
        if (!esIdentificacionValida(identificacion)) {
            throw new IllegalArgumentException("❌ Identificación inválida (debe tener entre 6 y 15 dígitos).");
        }
        if (!esCorreoValido(correo)) {
            throw new IllegalArgumentException("❌ El formato del correo electrónico no es válido.");
        }
        if (!esTelefonoValido(telefono)) {
            throw new IllegalArgumentException("❌ Teléfono inválido (debe tener entre 7 y 15 dígitos).");
        }
    }
}
