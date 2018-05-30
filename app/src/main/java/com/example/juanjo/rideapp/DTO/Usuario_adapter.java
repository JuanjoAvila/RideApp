package com.example.juanjo.rideapp.DTO;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * @author RideApp
 * @version Final
 * Utilizado para implementar un ListView en Amigos
 */

public class Usuario_adapter {
    private long id;
    private Integer idUsuario;
    private String usuario;
    private String password;
    private String nombre;
    private String apellidos;
    private String avatar;
    private String descripcion;
    private String correo;

    public Usuario_adapter(long id, Integer idUsuario, String usuario, String password, String nombre, String apellidos, String avatar, String descripcion, String correo) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.avatar = avatar;
        this.descripcion = descripcion;
        this.correo = correo;
    }

    public Usuario_adapter(String usuario, String password, String nombre, String apellidos, String avatar, String descripcion, String correo) {
        this.idUsuario = 0;
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.avatar = avatar;
        this.descripcion = descripcion;
        this.correo = correo;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario_adapter() {
        this.idUsuario = 0;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
