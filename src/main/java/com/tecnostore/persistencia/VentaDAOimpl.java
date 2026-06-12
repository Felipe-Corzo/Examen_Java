package com.tecnostore.persistencia;

import com.tecnostore.config.ConexionDB;

import com.tecnostore.model.Celular;
import com.tecnostore.model.Cliente;
import com.tecnostore.model.ItemVenta;
import com.tecnostore.model.Venta;
import com.tecnostore.model.emuns.Gama;
import com.tecnostore.model.emuns.SistemaOperativo;


import java.sql.*;
import java.time.LocalDate;



import java.util.ArrayList;
import java.util.List;

public class VentaDAOimpl implements IVentaDAO {

    private Connection getConexion() {
        return ConexionDB.getInstancia().getConexion();
    }

    @Override
    public Venta registrar(Venta venta) throws SQLException {
        Connection con = getConexion();
        boolean autoCommitOriginal = con.getAutoCommit();
        con.setAutoCommit(false); // ← inicio de transacción

        try {

            String sqlVenta = "INSERT INTO ventas (id_cliente, fecha, total) " +
                    "VALUES (?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(
                    sqlVenta, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, venta.getCliente().getId());
                ps.setDate(2, java.sql.Date.valueOf(venta.getFecha()));
                ps.setDouble(3, venta.getTotal());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) venta.setId(keys.getInt(1));
                }
            }


            String sqlDetalle = "INSERT INTO detalle_ventas " +
                    "(id_venta, id_celular, cantidad, subtotal) " +
                    "VALUES (?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                for (ItemVenta item : venta.getItems()) {
                    ps.setInt(1, venta.getId());
                    ps.setInt(2, item.getCelular().getId_celular());
                    ps.setInt(3, item.getCantidad());
                    ps.setDouble(4, item.getSubtotal());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            String sqlStock = "UPDATE celulares SET stock = stock - ? WHERE id_celular = ?";
            try (PreparedStatement psStock = con.prepareStatement(sqlStock)) {
                for (ItemVenta item : venta.getItems()) {
                    psStock.setInt(1, item.getCantidad());
                    // Asegúrate de que el getter coincida con el nombre que tienes en Celular.java
                    psStock.setInt(2, item.getCelular().getId_celular());
                    psStock.addBatch();
                }
                psStock.executeBatch();
            }

            con.commit();

        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(autoCommitOriginal);
        }

        return venta;
    }

    @Override
    public List<Venta> listarTodas() throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.id_venta, v.fecha, v.total, " +
                "c.id_cliente, c.nombre, c.identificacion, c.correo, c.telefono " +
                "FROM ventas v " +
                "INNER JOIN clientes c ON v.id_cliente = c.id_cliente " +
                "ORDER BY v.fecha DESC";

        try (PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("identificacion"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );

                Venta venta = new Venta(
                        rs.getInt("id_venta"),
                        cliente,
                        new ArrayList<>(), // <--- Aquí está el argumento que faltaba
                        rs.getDate("fecha").toLocalDate(),
                        rs.getDouble("total")
                );
                ventas.add(venta);
            }
        }
        return ventas;
    }


    //================================================================================================================================================


    @Override
    public List<Venta> listar() throws SQLException {
        List<Venta> ventas = new ArrayList<>();

        String sql = "SELECT v.id_venta, v.fecha, v.total, " +
                "       c.id_cliente, c.nombre, c.identificacion, c.correo, c.telefono " +
                "FROM ventas v " +
                "JOIN clientes c ON v.id_cliente = c.id_cliente";

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("identificacion"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );

                int idVenta = rs.getInt("id_venta");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                double total = rs.getDouble("total");

                Venta venta = new Venta(idVenta, cliente, new ArrayList<>(), fecha, total);
                ventas.add(venta);
            }
        }
        return ventas;
    }

    @Override
    public List<Venta> listarConDetalles() throws SQLException {
        List<Venta> ventas = new ArrayList<>();


        String sql = "SELECT v.id_venta, v.fecha, v.total, " +
                "       cl.id_cliente, cl.nombre, cl.identificacion, cl.correo, cl.telefono, " +
                "       dv.id_detalle_venta, dv.cantidad, dv.subtotal, " +
                "       ce.id_celular, ce.marca, ce.modelo, ce.sistema_operativo, ce.gama, ce.precio, ce.stock " +
                "FROM ventas v " +
                "JOIN clientes cl ON v.id_cliente = cl.id_cliente " +
                "JOIN detalle_ventas dv ON v.id_venta = dv.id_venta " +
                "JOIN celulares ce ON dv.id_celular = ce.id_celular " +
                "ORDER BY v.id_venta";

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Venta ventaActual = null;
            List<ItemVenta> itemsActuales = null;

            while (rs.next()) {
                int idVenta = rs.getInt("id_venta");

                if (ventaActual == null || ventaActual.getId() != idVenta) {


                    Cliente cliente = new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("identificacion"),
                            rs.getString("correo"),
                            rs.getString("telefono")
                    );

                    LocalDate fecha = rs.getDate("fecha").toLocalDate();
                    double total = rs.getDouble("total");


                    itemsActuales = new ArrayList<>();


                    ventaActual = new Venta(idVenta, cliente, itemsActuales, fecha, total);
                    ventas.add(ventaActual);
                }


                Gama gamaEnum = Gama.valueOf(rs.getString("gama").toUpperCase());
                String soBD = rs.getString("sistema_operativo").trim();
                SistemaOperativo soEnum = SistemaOperativo.valueOf(soBD);

                Celular celular = new Celular(
                        rs.getInt("id_celular"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        soEnum,
                        gamaEnum
                );

                ItemVenta item = new ItemVenta(
                        rs.getInt("id_detalle_venta"),
                        celular,
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal")
                );


                itemsActuales.add(item);
            }
        }
        return ventas;
    }


}
