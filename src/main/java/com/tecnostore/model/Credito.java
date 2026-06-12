package com.tecnostore.model;

import java.time.LocalDate;

public class Credito {
    private int id_credito;
    private int id_venta;          // Vinculado a la venta realizada
    private int id_cliente;        // Quién debe el dinero
    private double montoTotal;     // El total financiado
    private double saldoPendiente; // Lo que falta por pagar
    private int totalCuotas;      // Número de cuotas pactadas
    private int cuotasPagadas;    // Cuántas cuotas van pagadas
    private LocalDate fechaInicio; 
    private LocalDate fechaLimite; // Próximo vencimiento
    private String estado;         // "AL DIA", "EN MORA", "PAGADO"

    // Constructor vacío
    public Credito() {
    }

    // Constructor completo
    public Credito(int id_credito, int id_venta, int id_cliente, double montoTotal, double saldoPendiente, 
                   int totalCuotas, int cuotasPagadas, LocalDate fechaInicio, LocalDate fechaLimite, String estado) {
        this.id_credito = id_credito;
        this.id_venta = id_venta;
        this.id_cliente = id_cliente;
        this.montoTotal = montoTotal;
        this.saldoPendiente = saldoPendiente;
        this.totalCuotas = totalCuotas;
        this.cuotasPagadas = cuotasPagadas;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdCredito() {
        return id_credito;
    }

    public void setIdCredito(int idCredito) {
        this.id_credito = idCredito;
    }

    public int getIdVenta() {
        return id_venta;
    }

    public void setIdVenta(int idVenta) {
        this.id_venta = idVenta;
    }

    public int getIdCliente() {
        return id_cliente;
    }

    public void setIdCliente(int idCliente) {
        this.id_cliente = idCliente;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public int getTotalCuotas() {
        return totalCuotas;
    }

    public void setTotalCuotas(int totalCuotas) {
        this.totalCuotas = totalCuotas;
    }

    public int getCuotasPagadas() {
        return cuotasPagadas;
    }

    public void setCuotasPagadas(int cuotasPagadas) {
        this.cuotasPagadas = cuotasPagadas;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método de utilidad para el Reporte de Gestión
    @Override
    public String toString() {
        return "Credito ID: " + id_credito + 
               " | Cliente ID: " + id_cliente + 
               " | Total: $" + montoTotal + 
               " | Pendiente: $" + saldoPendiente + 
               " | Cuotas: " + cuotasPagadas + "/" + totalCuotas + 
               " | Estado: " + estado;
    }

    public int getSaldo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getCliente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}