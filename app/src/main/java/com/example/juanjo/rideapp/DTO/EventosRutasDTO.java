package com.example.juanjo.rideapp.DTO;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * @author RideApp
 * @version Final
 * Data transfer object
 */
public class EventosRutasDTO implements KvmSerializable {
    private Integer IdEvento;
    private Integer Ruta;
    private Integer Admin;
    private String Fecha_evento;
    private String Comentario_Evento;
    private Integer IdRuta;
    private Integer IdUsuario;
    private String Titulo;
    private String Descripcion;
    private String Mapa;
    private Integer Dificultad;
    private Integer Likes;
    private Integer Dislikes;
    private String Fecha_ruta;
    private String Foto_ruta;

    public EventosRutasDTO(Integer idEvento, Integer ruta, Integer admin, String fecha_evento, String Comentario_Evento, Integer idRuta, Integer idUsuario, String titulo, String descripcion, String mapa, Integer dificultad, Integer likes, Integer dislikes, String fecha_ruta, String foto_ruta) {
        this.IdEvento = idEvento;
        this.Ruta = ruta;
        this.Admin = admin;
        this.Fecha_evento = fecha_evento;
        this.Comentario_Evento = Comentario_Evento;
        this.IdRuta = idRuta;
        this.IdUsuario = idUsuario;
        this.Titulo = titulo;
        this.Descripcion = descripcion;
        this.Mapa = mapa;
        this.Dificultad = dificultad;
        this.Likes = likes;
        this.Dislikes = dislikes;
        this.Fecha_ruta = fecha_ruta;
        this.Foto_ruta = foto_ruta;
    }

    public EventosRutasDTO(Integer ruta, Integer admin, String fecha_evento, String Comentario_Evento, Integer idRuta, Integer idUsuario, String titulo, String descripcion,
                           String mapa, Integer dificultad, Integer likes, Integer dislikes, String fecha_ruta, String foto_ruta) {
        this.IdEvento = 0;
        this.Ruta = ruta;
        this.Admin = admin;
        this.Fecha_evento = fecha_evento;
        this.Comentario_Evento = Comentario_Evento;
        this.IdRuta = idRuta;
        this.IdUsuario = idUsuario;
        this.Titulo = titulo;
        this.Descripcion = descripcion;
        this.Mapa = mapa;
        this.Dificultad = dificultad;
        this.Likes = likes;
        this.Dislikes = dislikes;
        this.Fecha_ruta = fecha_ruta;
        this.Foto_ruta = foto_ruta;
    }

    public EventosRutasDTO() {
    }

    public Integer getIdEvento() {
        return IdEvento;
    }

    public void setIdEvento(Integer idEvento) {
        IdEvento = idEvento;
    }

    public Integer getRuta() {
        return Ruta;
    }

    public void setRuta(Integer ruta) {
        Ruta = ruta;
    }

    public Integer getAdmin() {
        return Admin;
    }

    public void setAdmin(Integer admin) {
        Admin = admin;
    }

    public String getFecha_evento() {
        return Fecha_evento;
    }

    public void setFecha_evento(String fecha_evento) {
        Fecha_evento = fecha_evento;
    }

    public String getComentario_Evento() {
        return Comentario_Evento;
    }

    public void setComentario_Evento(String comentario_Evento) {
        Comentario_Evento = comentario_Evento;
    }

    public Integer getIdRuta() {
        return IdRuta;
    }

    public void setIdRuta(Integer idRuta) {
        IdRuta = idRuta;
    }

    public Integer getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getMapa() {
        return Mapa;
    }

    public void setMapa(String mapa) {
        Mapa = mapa;
    }

    public Integer getDificultad() {
        return Dificultad;
    }

    public void setDificultad(Integer dificultad) {
        Dificultad = dificultad;
    }

    public Integer getLikes() {
        return Likes;
    }

    public void setLikes(Integer likes) {
        Likes = likes;
    }

    public Integer getDislikes() {
        return Dislikes;
    }

    public void setDislikes(Integer dislikes) {
        Dislikes = dislikes;
    }

    public String getFecha_ruta() {
        return Fecha_ruta;
    }

    public void setFecha_ruta(String fecha_ruta) {
        Fecha_ruta = fecha_ruta;
    }

    public String getFoto_ruta() {
        return Foto_ruta;
    }

    public void setFoto_ruta(String foto_ruta) {
        Foto_ruta = foto_ruta;
    }

    @Override
    public Object getProperty(int index) {

        switch (index) {
            case 0:
                return IdEvento;
            case 1:
                return Ruta;
            case 2:
                return Admin;
            case 3:
                return Fecha_evento;
            case 4:
                return Comentario_Evento;
            case 5:
                return IdRuta;
            case 6:
                return IdUsuario;
            case 7:
                return Titulo;
            case 8:
                return Descripcion;
            case 9:
                return Mapa;
            case 10:
                return Dificultad;
            case 11:
                return Likes;
            case 12:
                return Dislikes;
            case 13:
                return Fecha_ruta;
            case 14:
                return Foto_ruta;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 15;
    }

    @Override
    public void setProperty(int index, Object value) {

        switch (index) {
            case 0:
                IdEvento = Integer.valueOf((int) value);
                break;
            case 1:
                Ruta = Integer.valueOf((int) value);
                break;
            case 2:
                Admin = Integer.valueOf((int) value);
                break;
            case 3:
                Fecha_evento = value.toString();
                break;
            case 4:
                Comentario_Evento = value.toString();
                break;
            case 5:
                IdRuta = Integer.valueOf((int) value);
                break;
            case 6:
                IdUsuario = Integer.valueOf((int) value);
                break;
            case 7:
                Titulo = value.toString();
                break;
            case 8:
                Descripcion = value.toString();
                break;
            case 9:
                Mapa = value.toString();
                break;
            case 10:
                Dificultad = Integer.valueOf((int) value);
                break;
            case 11:
                Likes = Integer.valueOf((int) value);
                break;
            case 12:
                Dislikes = Integer.valueOf((int) value);
                break;
            case 13:
                Fecha_ruta = value.toString();
                break;
            case 14:
                Foto_ruta = value.toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

        switch (index) {
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdEvento";
                break;
            case 1:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Ruta";
                break;
            case 2:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Admin";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Fecha_evento";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Comentario_Evento";
                break;
            case 5:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdRuta";
                break;
            case 6:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdUsuario";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Titulo";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Descripcion";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Mapa";
                break;
            case 10:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Dificultad";
                break;
            case 11:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Likes";
                break;
            case 12:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Dislikes";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Fecha_ruta";
                break;
            case 14:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Foto_ruta";
                break;
            default:
                break;
        }
    }
}
