package com.tecnostore.model;

public class ItemVenta {

    private int     id;
    private Celular celular;
    private int     cantidad;
    private double  subtotal;


    public ItemVenta(Celular celular, int cantidad, double subtotal) {
        this.celular = celular;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public ItemVenta(int id, Celular celular, int cantidad, double subtotal) {
        this.id = id;
        this.celular = celular;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Celular getCelular() {
        return celular;
    }

    public void setCelular(Celular celular) {
        this.celular = celular;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "ItemVenta{" +
                "id=" + id +
                ", celular=" + celular +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                '}';
    }
}
