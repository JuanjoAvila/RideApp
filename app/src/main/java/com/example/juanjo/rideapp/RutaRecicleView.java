package com.example.juanjo.rideapp;

import android.widget.RatingBar;

import java.time.LocalDate;
import java.util.Date;

public class RutaRecicleView {
    private int fotoUsuario;
    private String nombreRuta;
    private int rutaMapa;
    private String fechaPublicacion;
    private int puntuacionEstrellas;
    private int likesCarita;
    private int likes;
    private int dislikesCarita;
    private int dislikes;

    RutaRecicleView(int fotoUsuario, String nombreRuta, int rutaMapa, String fechaPublicacion, int puntuacionEstrellas, int likesCarita, int likes, int dislikesCarita, int dislikes) {
        this.fotoUsuario = fotoUsuario;
        this.nombreRuta = nombreRuta;
        this.rutaMapa = rutaMapa;
        this.fechaPublicacion = fechaPublicacion;
        this.puntuacionEstrellas = puntuacionEstrellas;
        this.likesCarita = likesCarita;
        this.likes = likes;
        this.dislikesCarita = dislikesCarita;
        this.dislikes = dislikes;
    }

    public int getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(int fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public int getRutaMapa() {
        return rutaMapa;
    }

    public void setRutaMapa(int rutaMapa) {
        this.rutaMapa = rutaMapa;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getPuntuacionEstrellas() {
        return puntuacionEstrellas;
    }

    public void setPuntuacionEstrellas(int puntuacionEstrellas) {
        this.puntuacionEstrellas = puntuacionEstrellas;
    }

    public int getLikesCarita() {
        return likesCarita;
    }

    public void setLikesCarita(int likesCarita) {
        this.likesCarita = likesCarita;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikesCarita() {
        return dislikesCarita;
    }

    public void setDislikesCarita(int dislikesCarita) {
        this.dislikesCarita = dislikesCarita;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}