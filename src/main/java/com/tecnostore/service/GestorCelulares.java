package com.tecnostore.service;

import com.tecnostore.model.Celular;
import com.tecnostore.model.emuns.Gama;
import com.tecnostore.model.emuns.SistemaOperativo;
import com.tecnostore.persistencia.ICelularDAO;
import com.tecnostore.factory.CelularFactory;
import com.tecnostore.utils.ValidadorCelular;

import java.sql.SQLException;
import java.util.List;

public class GestorCelulares {

    private final ICelularDAO celularDAO;

    public GestorCelulares(ICelularDAO celularDAO) {
        this.celularDAO = celularDAO;
    }

    public Celular registrar(String marca, String modelo, double precio, int stock, SistemaOperativo sistemaOperativo, Gama gama) throws SQLException {
        if (!ValidadorCelular.esValido(precio, stock)) {
            return null;
        }
        Celular nuevo = CelularFactory.crearCelular(0, marca, modelo, precio, stock, sistemaOperativo, gama);

        return celularDAO.registrar(nuevo);
    }

    public List<Celular> listar() throws SQLException {
        return celularDAO.listar();
    }

    public Celular buscarPorId(int id) throws SQLException {
        return celularDAO.buscarPorId(id);
    }

    public boolean actualizar(int id, String marca, String modelo, double precio, int stock, SistemaOperativo so, Gama gama) throws SQLException {
        // 1. Validamos que los nuevos datos lógicos de negocio sean correctos (< 0)
        if (!ValidadorCelular.esValido(precio, stock)) {
            return false; // Retorna false y frena la actualización si hay datos negativos
        }

        // 2. Buscamos el registro real en la BD para asegurarnos de que existe
        Celular existente = celularDAO.buscarPorId(id);
        if (existente == null) {
            return false;
        }

        // 3. Modificamos los valores de forma segura en la entidad si pasó la validación
        existente.setMarca(marca);
        existente.setModelo(modelo);
        existente.setPrecio(precio);
        existente.setStock(stock);
        existente.setSistema_operativo(so);
        existente.setGama(gama);

        // 4. Enviamos a actualizar en la persistencia (DAO)
        celularDAO.actualizar(existente);
        return true;
    }

    public void eliminar(int id) throws SQLException {
        celularDAO.eliminar(id);
    }
}