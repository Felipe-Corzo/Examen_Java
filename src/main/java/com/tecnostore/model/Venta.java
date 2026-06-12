package com.tecnostore.model;

import java.time.LocalDate;
import java.util.List;

public class Venta {

    private int           id;
    private Cliente       cliente;
    private List<ItemVenta> items;
    private LocalDate fecha;
    private double        total;


    public Venta(Cliente cliente, List<ItemVenta> items, LocalDate fecha, double total) {
        this.cliente = cliente;
        this.items   = items;
        this.fecha   = fecha;
        this.total   = total;
    }

    public Venta(int id, Cliente cliente, List<ItemVenta> items,
                 LocalDate fecha, double total) {
        this.id      = id;
        this.cliente = cliente;
        this.items   = items;
        this.fecha   = fecha;
        this.total   = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemVenta> getItems() {
        return items;
    }

    public void setItems(List<ItemVenta> items) {
        this.items = items;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", items=" + items +
                ", fecha=" + fecha +
                ", total=" + total +
                '}';
    }
}