package com.example.juanjo.rideapp.Rutas;

/**
 * Created by jesus on 17/05/18.
 */

public class Rutas_opciones {

    private long id;
    private String titulo;
    private String descripcion;

    public Rutas_opciones(long id, String titulo, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
