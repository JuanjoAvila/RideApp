package com.example.juanjo.rideapp.DTO;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class ParticipantesEventoDTO implements KvmSerializable {
    private Integer IdParticipante;
    private Integer Evento;
    private Integer IdUsuario;
    private String Usuario;
    private String Nombre;
    private String Apellidos;
    private String Avatar;
    private String Descripcion;
    private String Correo;

    public ParticipantesEventoDTO(Integer idParticipante, Integer Evento, Integer idUsuario, String Usuario, String Nombre,
                                  String Apellidos, String Avatar, String Descripcion, String Correo) {
        this.IdParticipante = idParticipante;
        this.Evento = Evento;
        this.IdUsuario = idUsuario;
        this.Usuario = Usuario;
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Avatar = Avatar;
        this.Descripcion = Descripcion;
        this.Correo = Correo;
    }

    public ParticipantesEventoDTO(Integer Evento, Integer idUsuario, String Usuario, String Nombre,
                                  String Apellidos, String Avatar, String Descripcion, String Correo) {
        this.IdParticipante = 0;
        this.Evento = Evento;
        this.IdUsuario = idUsuario;
        this.Usuario = Usuario;
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Avatar = Avatar;
        this.Descripcion = Descripcion;
        this.Correo = Correo;
    }

    public ParticipantesEventoDTO() {
        this.IdParticipante = 0;
    }

    public Integer getIdParticipante() {
        return IdParticipante;
    }

    public void setIdParticipante(Integer idParticipante) {
        IdParticipante = idParticipante;
    }

    public Integer getEvento() {
        return Evento;
    }

    public void setEvento(Integer evento) {
        Evento = evento;
    }

    public Integer getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    @Override
    public Object getProperty(int index) {

        switch(index){
            case 0:
                return IdParticipante;
            case 1:
                return Evento;
            case 2:
                return IdUsuario;
            case 3:
                return Usuario;
            case 4:
                return Nombre;
            case 5:
                return Apellidos;
            case 6:
                return Avatar;
            case 7:
                return Descripcion;
            case 8:
                return Correo;
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
                IdParticipante = Integer.valueOf((int)value);
                break;
            case 1:
                Evento = Integer.valueOf((int)value);
                break;
            case 2:
                IdUsuario = Integer.valueOf((int)value);
                break;
            case 3:
                Usuario = value.toString();
                break;
            case 4:
                Nombre = value.toString();
                break;
            case 5:
                Apellidos = value.toString();
                break;
            case 6:
                Avatar = value.toString();
                break;
            case 7:
                Descripcion = value.toString();
                break;
            case 8:
                Correo = value.toString();
                break;
            default: break;
        }
    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

        switch(index){
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdParticipante";
                break;
            case 1:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Evento";
                break;
            case 2:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdUsuario";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Usuario";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Nombre";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Apellidos";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Avatar";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Descripcion";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Correo";
                break;
            default: break;
        }
    }
}