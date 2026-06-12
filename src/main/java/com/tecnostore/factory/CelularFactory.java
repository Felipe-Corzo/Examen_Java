package com.tecnostore.factory;

import com.tecnostore.model.Celular;
import com.tecnostore.model.emuns.Gama;
import com.tecnostore.model.emuns.SistemaOperativo;

public class CelularFactory {


    public static Celular crearCelular(int id, String marca, String modelo, double precio,
                                       int stock, SistemaOperativo so, Gama gama) {

        return new Celular(id, marca, modelo, precio, stock, so, gama);
    }
}
