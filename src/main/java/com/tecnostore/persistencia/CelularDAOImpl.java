package com.tecnostore.persistencia;

import com.tecnostore.config.ConexionDB;
import com.tecnostore.model.Celular;
import com.tecnostore.model.emuns.Gama;
import com.tecnostore.model.emuns.SistemaOperativo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CelularDAOImpl implements ICelularDAO {

    @Override
    public Celular registrar(Celular celular) throws SQLException {
        String sql = "INSERT INTO celulares (marca, modelo, precio, stock, sistema_operativo, gama) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, celular.getMarca());
            ps.setString(2, celular.getModelo());
            ps.setDouble(3, celular.getPrecio());
            ps.setInt(4, celular.getStock());
            ps.setString(5, celular.getSistema_operativo().name());
            ps.setString(6, celular.getGama().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    celular.setId_celular(rs.getInt(1));
                }
            }
        }
        return celular;
    }

    @Override
    public void actualizar(Celular celular) throws SQLException {
        String sql = "UPDATE celulares SET marca = ?, modelo = ?, precio = ?, stock = ?, sistema_operativo = ?, gama = ? WHERE id_celular = ?";

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, celular.getMarca());
            ps.setString(2, celular.getModelo());
            ps.setDouble(3, celular.getPrecio());
            ps.setInt(4, celular.getStock());
            ps.setString(5, celular.getSistema_operativo().name());
            ps.setString(6, celular.getGama().name().toLowerCase());
            ps.setInt(7, celular.getId_celular());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {

        String sql = "UPDATE celulares SET activo = false WHERE id_celular = ?";

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Celular> listar() throws SQLException {

        String sql = "SELECT id_celular, marca, modelo, precio, stock, sistema_operativo, gama FROM celulares WHERE activo = true";
        List<Celular> listaCelulares = new ArrayList<>();

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Celular celular = construirCelularDesdeResultSet(rs);
                listaCelulares.add(celular);
            }
        }
        return listaCelulares;
    }

    @Override
    public Celular buscarPorId(int id) throws SQLException {

        String sql = "SELECT id_celular, marca, modelo, precio, stock, sistema_operativo, gama FROM celulares WHERE id_celular = ? AND activo = true";

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    return construirCelularDesdeResultSet(rs);
                }
            }
        }

        return null;
    }


    private Celular construirCelularDesdeResultSet(ResultSet rs) throws SQLException {
        Celular celular = new Celular();
        celular.setId_celular(rs.getInt("id_celular"));
        celular.setMarca(rs.getString("marca"));
        celular.setModelo(rs.getString("modelo"));
        celular.setPrecio(rs.getDouble("precio"));
        celular.setStock(rs.getInt("stock"));


        String soDB = rs.getString("sistema_operativo");
        celular.setSistema_operativo(buscarSistemaOperativo(soDB));


        String gamaDB = rs.getString("gama");
        celular.setGama(buscarGama(gamaDB));

        return celular;
    }


    private SistemaOperativo buscarSistemaOperativo(String valor) {
        for (SistemaOperativo so : SistemaOperativo.values()) {
            if (so.name().equalsIgnoreCase(valor)) {
                return so;
            }
        }
        return SistemaOperativo.Android;
    }

    private Gama buscarGama(String valor) {
        for (Gama g : Gama.values()) {
            if (g.name().equalsIgnoreCase(valor)) {
                return g;
            }
        }
        return Gama.MEDIA;
    }
}