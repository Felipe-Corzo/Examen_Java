package com.tecnostore.controller;

import com.tecnostore.model.Celular;
import com.tecnostore.model.emuns.Gama;
import com.tecnostore.model.emuns.SistemaOperativo;
import com.tecnostore.service.GestorCelulares;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CelularController {

    private final GestorCelulares gestorCelulares;

    public CelularController(GestorCelulares gestorCelulares) {
        this.gestorCelulares = gestorCelulares;
    }

    public String registrarCelular(String marca, String modelo, double precio, int stock, SistemaOperativo sistema_operativo, Gama gama) {
        try {
            Celular registrado = gestorCelulares.registrar(marca, modelo, precio, stock, sistema_operativo, gama);
            if (registrado == null) {
                return "⚠️ El celular no pudo ser registrado debido a datos inválidos.";

            }
            return "✅ Celular registrado con éxito. ID asignado: " + registrado.getId_celular();
        } catch (SQLException e) {
            return "❌ Error en el servicio al registrar: " + e.getMessage();
        }
    }


    public List<Celular> listarCelulares() {
        try {
            return gestorCelulares.listar();
        } catch (SQLException e) {
            System.out.println("❌ Error al listar celulares: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public Celular buscarCelular(int id) {
        try {
            return gestorCelulares.buscarPorId(id);
        } catch (SQLException e) {
            System.out.println("❌ Error al buscar el celular con ID " + id + ": " + e.getMessage());
            return null;
        }
    }

    public String actualizarCelular(int id, String marca, String modelo, double precio, int stock, SistemaOperativo sistema_operativo, Gama gama) {
        try {
            // Pasamos los datos primitivos directamente al gestor
            // Él se encargará de validar antes de tocar la base de datos
            boolean actualizado = gestorCelulares.actualizar(id, marca, modelo, precio, stock, sistema_operativo, gama);

            if (!actualizado) {
                return "⚠️ No se pudo actualizar el celular. Verifique los datos ingresados o el ID.";
            }

            return "🔄 Celular con ID " + id + " actualizado correctamente.";
        } catch (SQLException e) {
            return "❌ Error en el servicio al actualizar: " + e.getMessage();
        }
    }


    public String eliminarCelular(int id) {
        try {
            Celular existente = gestorCelulares.buscarPorId(id);
            if (existente == null) {
                return "❌ Error: No se encontró ningún celular con el ID: " + id;
            }

            gestorCelulares.eliminar(id);
            return "🗑️ Celular con ID " + id + " eliminado permanentemente.";
        } catch (SQLException e) {
            return "❌ Error en el servicio al eliminar: " + e.getMessage();
        }
    }
}