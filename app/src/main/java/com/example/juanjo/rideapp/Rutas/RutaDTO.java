package com.example.juanjo.rideapp.Rutas;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by jesus on 22/05/18.
 */

public class RutaDTO implements KvmSerializable{
    private int idRuta;
    private int idUsuario;
    private String titulo;
    private String descripcion;
    private String mapa;
    private int dificultad;
    private int likes;
    private int dislikes;
    private String foto;

    public RutaDTO() {
    }

    public RutaDTO(int idRuta, int idUsuario, String titulo, String descripcion, String mapa, int dificultad, int likes, int dislikes, String foto) {
        this.idRuta = idRuta;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.mapa = mapa;
        this.dificultad = dificultad;
        this.likes = likes;
        this.dislikes = dislikes;
        this.foto = foto;
    }

    public RutaDTO(int idUsuario, String titulo, String descripcion, String mapa, int dificultad, int likes, int dislikes, String foto) {
        this.idRuta = 0;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.mapa = mapa;
        this.dificultad = dificultad;
        this.likes = likes;
        this.dislikes = dislikes;
        this.foto = foto;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getMapa() {
        return mapa;
    }

    public void setMapa(String mapa) {
        this.mapa = mapa;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public Object getProperty(int index) {

        switch(index){
            case 0:
                return idRuta;
            case 1:
                return idUsuario;
            case 2:
                return titulo;
            case 3:
                return descripcion;
            case 4:
                return mapa;
            case 5:
                return dificultad;
            case 6:
                return likes;
            case 7:
                return dislikes;
            case 8:
                return foto;
            default: break;
        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 9;
    }

    @Override
    public void setProperty(int index, Object value) {

        switch(index){
            case 0:
                idRuta = Integer.valueOf((int)value);
                break;
            case 1:
                idUsuario = Integer.valueOf((int)value);
                break;
            case 2:
                titulo = value.toString();
                break;
            case 3:
                descripcion = value.toString();
                break;
            case 4:
                mapa = value.toString();
                break;
            case 5:
                dificultad = Integer.valueOf((int)value);
                break;
            case 6:
                likes = Integer.valueOf((int)value);
                break;
            case 7:
                dislikes = Integer.valueOf((int)value);
                break;
            case 8:
                foto = value.toString();
            default: break;
        }
    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

        switch(index){
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdRuta";
                break;
            case 1:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdUsuario";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Titulo";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Descripcion";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Mapa";
                break;
            case 5:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Dificultad";
                break;
            case 6:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Likes";
                break;
            case 7:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Dislikes";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Foto";
                break;
            default: break;
        }
    }
}
