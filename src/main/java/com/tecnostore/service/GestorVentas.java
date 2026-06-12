package com.tecnostore.service;

import com.tecnostore.model.*;
import com.tecnostore.persistencia.ICelularDAO;
import com.tecnostore.persistencia.IClienteDAO;
import com.tecnostore.persistencia.IVentaDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class GestorVentas {

    private static final double IVA = 0.19;

    private final IVentaDAO   ventaDAO;
    private final ICelularDAO celularDAO;
    private final IClienteDAO clienteDAO;

    public GestorVentas(IVentaDAO ventaDAO, ICelularDAO celularDAO,
                        IClienteDAO clienteDAO) {
        this.ventaDAO   = ventaDAO;
        this.celularDAO = celularDAO;
        this.clienteDAO = clienteDAO;
    }

    public Cliente buscarCliente(String identificacion) {
        try {
            return clienteDAO.buscarPorIdentificacion(identificacion);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage());
        }
    }

    public Celular buscarCelular(int id) {
        try {
            return celularDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar celular: " + e.getMessage());
        }
    }


    public Venta registrarVenta(Cliente cliente, List<ItemVenta> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("❌ La lista de ítems no puede estar vacía.");
        }

        for (ItemVenta item : items) {
            if (item.getCantidad() <= 0)
                throw new IllegalArgumentException("❌ La cantidad debe ser mayor a 0.");

            if (item.getCelular().getStock() < item.getCantidad())
                throw new IllegalArgumentException("❌ Stock insuficiente para: "
                        + item.getCelular().getMarca() + " " + item.getCelular().getModelo());
        }

        double subtotalSum = items.stream().mapToDouble(ItemVenta::getSubtotal).sum();
        double total = Math.round(subtotalSum * (1 + IVA) * 100.0) / 100.0;

        try {
            Venta venta = new Venta(cliente, items, LocalDate.now(), total);
            venta.setItems(items);
            ventaDAO.registrar(venta);
            return venta;
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar la venta: " + e.getMessage());
        }
    }


    public List<Venta> listarVentas() {
        try {
            return ventaDAO.listarTodas();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar ventas: " + e.getMessage());
        }
    }
}