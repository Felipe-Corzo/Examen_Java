package com.tecnostore.model;

import com.tecnostore.model.emuns.Gama;
import com.tecnostore.model.emuns.SistemaOperativo;

public class Celular {

    private int id_celular;
    private String marca;
    private String modelo;
    private double precio;
    private int stock;
    private SistemaOperativo sistema_operativo;
    private Gama gama;


    public Celular() {
    }


    public Celular(int id_celular, String marca, String modelo, double precio, int stock, SistemaOperativo sistema_operativo, Gama gama) {
        this.id_celular = id_celular;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
        this.sistema_operativo = sistema_operativo;
        this.gama = gama;
    }



    public int getId_celular() {
        return id_celular;
    }

    public void setId_celular(int id_celular) {
        this.id_celular = id_celular;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public SistemaOperativo getSistema_operativo() {
        return sistema_operativo;
    }

    public void setSistema_operativo(SistemaOperativo sistema_operativo) {
        this.sistema_operativo = sistema_operativo;
    }

    public Gama getGama() {
        return gama;
    }

    public void setGama(Gama gama) {
        this.gama = gama;
    }

    // Método toString opcional (Te servirá muchísimo en la consola/vista para listar los celulares)
    @Override
    public String toString() {
        return String.format("📱 CELULAR [ID: %d] | %s %s | Precio: $%.2f | Stock: %d | S.O: %s | Gama: %s",
                this.id_celular, this.marca, this.modelo, this.precio, this.stock, this.sistema_operativo, this.gama);
    }
}