package com.example.juanjo.rideapp.DTO;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * @author RideApp
 * @version Final
 * Data transfer object
 */
public class EventoDTO implements KvmSerializable {
    private int IdEvento;
    private int Ruta;
    private int Admin;
    private String Fecha;
    private String Comentario;

    public EventoDTO(int idEvento, int ruta, int admin, String fecha, String comentario) {
        this.IdEvento = idEvento;
        this.Ruta = ruta;
        this.Admin = admin;
        this.Fecha = fecha;
        this.Comentario = comentario;
    }

    public EventoDTO(int ruta, int admin, String fecha, String comentario) {
        this.IdEvento = 0;
        this.Ruta = ruta;
        this.Admin = admin;
        this.Fecha = fecha;
        this.Comentario = comentario;
    }

    public EventoDTO() {
        this.IdEvento = 0;
    }

    public int getIdEvento() {
        return IdEvento;
    }

    public void setIdEvento(int idEvento) {
        IdEvento = idEvento;
    }

    public int getRuta() {
        return Ruta;
    }

    public void setRuta(int ruta) {
        Ruta = ruta;
    }

    public int getAdmin() {
        return Admin;
    }

    public void setAdmin(int admin) {
        Admin = admin;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }

    @Override
    public Object getProperty(int index) {
        switch(index){
            case 0:
                return IdEvento;
            case 1:
                return Ruta;
            case 2:
                return Admin;
            case 3:
                return Fecha;
            case 4:
                return Comentario;
            default: break;
        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 5;
    }

    @Override
    public void setProperty(int index, Object value) {

        switch(index){
            case 0:
                IdEvento = Integer.valueOf((int)value);
                break;
            case 1:
                Ruta = Integer.valueOf((int)value);
                break;
            case 2:
                Admin = Integer.valueOf((int)value);
                break;
            case 3:
                Fecha = value.toString();
                break;
            case 4:
                Comentario = value.toString();
                break;
            default: break;
        }
    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

        switch(index){
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
                info.name = "Fecha";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Comentario";
                break;
            default: break;
        }
    }
}
