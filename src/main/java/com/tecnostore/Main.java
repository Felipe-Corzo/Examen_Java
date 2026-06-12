package com.tecnostore;

import com.tecnostore.config.ConexionDB;
import com.tecnostore.controller.CelularController;
import com.tecnostore.controller.ClienteController;
import com.tecnostore.controller.ReporteController;
import com.tecnostore.controller.VentaController;
import com.tecnostore.persistencia.*;
import com.tecnostore.service.GestorCelulares;
import com.tecnostore.service.GestorClientes;
import com.tecnostore.service.GestorReporte;
import com.tecnostore.service.GestorVentas;
import com.tecnostore.view.MenuPrincipal;

public class Main {

    public static void main(String[] args) {

        // ── DAOs ──────────────────────────────────────────────────────────
        IClienteDAO clienteDAO = new ClienteDAOimpl();
        ICelularDAO celularDAO = new CelularDAOImpl();
        IVentaDAO   ventaDAO   = new VentaDAOimpl();

        // ── Servicios ─────────────────────────────────────────────────────
        GestorClientes  gestorClientes  = new GestorClientes(clienteDAO);
        GestorCelulares gestorCelulares = new GestorCelulares(celularDAO);
        GestorVentas    gestorVentas    = new GestorVentas(ventaDAO, celularDAO, clienteDAO);
        GestorReporte   gestorReporte   = new GestorReporte(celularDAO, ventaDAO);

        // ── Controladores ─────────────────────────────────────────────────
        ClienteController clienteController = new ClienteController(gestorClientes);
        CelularController celularController = new CelularController(gestorCelulares);
        VentaController   ventaController   = new VentaController(gestorVentas);
        ReporteController reporteController = new ReporteController(gestorReporte);

        // ── Arrancar ──────────────────────────────────────────────────────
        MenuPrincipal menu = new MenuPrincipal(
                celularController,
                clienteController,
                ventaController,
                reporteController
        );
        menu.iniciarMenu();

        ConexionDB.getInstancia().cerrar();
    }
}