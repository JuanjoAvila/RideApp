package com.example.juanjo.rideapp.DTO;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * @author RideApp
 * @version Final
 * Data transfer object
 */

public class UsuarioDTO implements KvmSerializable{
    private Integer idUsuario;
    private String usuario;
    private String password;
    private String nombre;
    private String apellidos;
    private String avatar;
    private String descripcion;
    private String correo;

    public UsuarioDTO(Integer idUsuario, String usuario, String password, String nombre, String apellidos, String avatar, String descripcion, String correo) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.avatar = avatar;
        this.descripcion = descripcion;
        this.correo = correo;
    }

    public UsuarioDTO(String usuario, String password, String nombre, String apellidos, String avatar, String descripcion, String correo) {
        this.idUsuario = 0;
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.avatar = avatar;
        this.descripcion = descripcion;
        this.correo = correo;
    }

    public UsuarioDTO() {
        this.idUsuario = 0;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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

    @Override
    public Object getProperty(int index) {

        switch(index){
            case 0:
                return idUsuario;
            case 1:
                return usuario;
            case 2:
                return password;
            case 3:
                return nombre;
            case 4:
                return apellidos;
            case 5:
                return avatar;
            case 6:
                return descripcion;
            case 7:
                return correo;
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
                idUsuario = Integer.valueOf((int)value);
                break;
            case 1:
                usuario = value.toString();
                break;
            case 2:
                password = value.toString();
                break;
            case 3:
                nombre = value.toString();
                break;
            case 4:
                apellidos = value.toString();
                break;
            case 5:
                avatar = value.toString();
                break;
            case 6:
                descripcion = value.toString();
                break;
            case 7:
                correo = value.toString();
                break;
            default: break;
        }
    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

        switch(index){
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdUsuario";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Usuario";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Password";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Nombre";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Apellidos";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Avatar";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Descripcion";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Correo";
                break;
            default: break;
        }
    }
}
