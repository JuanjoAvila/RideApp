package com.example.juanjo.rideapp.DTO;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * @author RideApp
 * @version Final
 * Data transfer object
 */
public class AmigoDTO implements KvmSerializable {
    private Integer idAmigo;
    private Integer idUsuario;
    private Integer amigo;

    public AmigoDTO(Integer idAmigo, Integer idUsuario, Integer amigo) {
        this.idAmigo = idAmigo;
        this.idAmigo = idUsuario;
        this.idAmigo = amigo;
    }

    public AmigoDTO(Integer idUsuario, Integer amigo) {
        this.idAmigo = 0;
        this.idAmigo = idUsuario;
        this.idAmigo = amigo;
    }

    public AmigoDTO() {
        this.idAmigo = 0;
    }

    public Integer getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(Integer idAmigo) {
        this.idAmigo = idAmigo;
    }

    public Integer getidUsuario() {
        return idUsuario;
    }

    public void setidUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getamigo() {
        return amigo;
    }

    public void setamigo(Integer amigo) {
        this.amigo = amigo;
    }

    @Override
    public Object getProperty(int index) {

        switch(index){
            case 0:
                return idAmigo;
            case 1:
                return idUsuario;
            case 2:
                return amigo;
            default: break;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 3;
    }

    @Override
    public void setProperty(int index, Object value) {

        switch(index){
            case 0:
                idAmigo = Integer.valueOf((int)value);
                break;
            case 1:
                idUsuario = Integer.valueOf((int)value);
                break;
            case 2:
                amigo = Integer.valueOf((int)value);
                break;
            default: break;
        }
    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

        switch(index){
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdAmigo";
                break;
            case 1:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdUsuario";
                break;
            case 2:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Amigo";
                break;
            default: break;
        }
    }
}