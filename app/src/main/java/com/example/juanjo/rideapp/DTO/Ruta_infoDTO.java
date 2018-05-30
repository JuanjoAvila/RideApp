package com.example.juanjo.rideapp.DTO;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by jesus on 24/05/18.
 */

public class Ruta_infoDTO implements KvmSerializable{
    private int idRuta;
    private String titulo;
    private String foto_ruta;
    private String fecha_ruta;
    private int dificultad;
    private int likes;
    private int dislikes;
    private String foto_usuario;

    public Ruta_infoDTO() {
    }

    public Ruta_infoDTO(int idRuta, String titulo, String foto_ruta, String fecha_ruta, int dificultad, int likes, int dislikes, String foto_usuario) {
        this.idRuta = idRuta;
        this.titulo = titulo;
        this.foto_ruta = foto_ruta;
        this.fecha_ruta = fecha_ruta;
        this.dificultad = dificultad;
        this.likes = likes;
        this.dislikes = dislikes;
        this.foto_usuario = foto_usuario;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFoto_ruta() {
        return foto_ruta;
    }

    public void setFoto_ruta(String foto_ruta) {
        this.foto_ruta = foto_ruta;
    }

    public String getFecha_ruta() {
        return fecha_ruta;
    }

    public void setFecha_ruta(String fecha_ruta) {
        this.fecha_ruta = fecha_ruta;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getFoto_usuario() {
        return foto_usuario;
    }

    public void setFoto_usuario(String foto_usuario) {
        this.foto_usuario = foto_usuario;
    }

    @Override
    public Object getProperty(int index) {

        switch(index){
            case 0:
                return idRuta;
            case 1:
                return titulo;
            case 2:
                return foto_ruta;
            case 3:
                return fecha_ruta;
            case 4:
                return dificultad;
            case 5:
                return likes;
            case 6:
                return dislikes;
            case 7:
                return foto_usuario;

            default: break;
        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 8;
    }

    @Override
    public void setProperty(int index, Object value) {

        switch(index){
            case 0:
                idRuta = Integer.valueOf((int)value);
                break;
            case 1:
                titulo = value.toString();
                break;
            case 2:
                foto_ruta = value.toString();
                break;
            case 3:
                fecha_ruta = value.toString();
                break;
            case 4:
                dificultad = Integer.valueOf((int)value);
                break;
            case 5:
                likes = Integer.valueOf((int)value);
            case 6:
                dislikes = Integer.valueOf((int)value);
                break;
            case 7:
                foto_usuario = value.toString();
                break;

            default: break;
        }
    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

        switch(index){
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "idRuta";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "titulo";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "foto_ruta";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "fecha_ruta";
                break;
            case 4:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "dificultad";
                break;
            case 5:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "likes";
                break;
            case 6:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "dislikes";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "foto_usuario";
                break;

            default: break;
        }
    }
}
