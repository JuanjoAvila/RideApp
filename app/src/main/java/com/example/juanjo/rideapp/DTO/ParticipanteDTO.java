package com.example.juanjo.rideapp.DTO;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * @author RideApp
 * @version Final
 * Data transfer object
 */
public class ParticipanteDTO implements KvmSerializable{
    private Integer IdParticipante;
    private Integer Evento;
    private Integer IdUsuario;

    public ParticipanteDTO(Integer idParticipante, Integer Evento, Integer idUsuario) {
        this.IdParticipante = idParticipante;
        this.Evento = Evento;
        this.IdUsuario = idUsuario;
    }

    public ParticipanteDTO(Integer Evento, Integer amigo) {
        this.IdParticipante = 0;
        this.Evento = Evento;
        this.IdUsuario = amigo;
    }

    public ParticipanteDTO() {
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

    @Override
    public Object getProperty(int index) {

        switch(index){
            case 0:
                return IdParticipante;
            case 1:
                return Evento;
            case 2:
                return IdUsuario;
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
                IdParticipante = Integer.valueOf((int)value);
                break;
            case 1:
                Evento = Integer.valueOf((int)value);
                break;
            case 2:
                IdUsuario = Integer.valueOf((int)value);
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
            default: break;
        }
    }
}
