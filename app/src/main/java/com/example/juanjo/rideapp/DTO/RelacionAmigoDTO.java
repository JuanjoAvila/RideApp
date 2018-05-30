package com.example.juanjo.rideapp.DTO;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class RelacionAmigoDTO implements KvmSerializable {
    private Integer IdUsuario;
    private String Usuario;
    private String NombreUsuario;
    private String Avatar;
    private Integer IdAmigo;
    private String UsuarioAmigo;
    private String NombreAmigo;
    private String AvatarAmigo;

    public RelacionAmigoDTO(Integer IdUsuario, String Usuario, String NombreUsuario, String Avatar, Integer IdAmigo,
                    String UsuarioAmigo, String NombreAmigo, String AvatarAmigo) {
        this.IdUsuario = IdUsuario;
        this.Usuario = Usuario;
        this.NombreUsuario = NombreUsuario;
        this.Avatar = Avatar;
        this.IdAmigo = IdAmigo;
        this.UsuarioAmigo = UsuarioAmigo;
        this.NombreAmigo = NombreAmigo;
        this.AvatarAmigo = AvatarAmigo;
    }

    public RelacionAmigoDTO() {}

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

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public Integer getIdAmigo() {
        return IdAmigo;
    }

    public void setIdAmigo(Integer idAmigo) {
        IdAmigo = idAmigo;
    }

    public String getUsuarioAmigo() {
        return UsuarioAmigo;
    }

    public void setUsuarioAmigo(String usuarioAmigo) {
        UsuarioAmigo = usuarioAmigo;
    }

    public String getNombreAmigo() {
        return NombreAmigo;
    }

    public void setNombreAmigo(String nombreAmigo) {
        NombreAmigo = nombreAmigo;
    }

    public String getAvatarAmigo() {
        return AvatarAmigo;
    }

    public void setAvatarAmigo(String avatarAmigo) {
        AvatarAmigo = avatarAmigo;
    }

    @Override
    public Object getProperty(int index) {
        switch(index){
            case 0:
                return IdUsuario;
            case 1:
                return Usuario;
            case 2:
                return NombreUsuario;
            case 3:
                return Avatar;
            case 4:
                return IdAmigo;
            case 5:
                return UsuarioAmigo;
            case 6:
                return NombreAmigo;
            case 7:
                return AvatarAmigo;
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
                IdUsuario = Integer.valueOf((int)value);
                break;
            case 1:
                Usuario = value.toString();
                break;
            case 2:
                NombreUsuario = value.toString();
                break;
            case 3:
                Avatar = value.toString();
                break;
            case 4:
                IdAmigo = Integer.valueOf((int)value);
                break;
            case 5:
                UsuarioAmigo = value.toString();
                break;
            case 6:
                NombreAmigo = value.toString();
                break;
            case 7:
                AvatarAmigo = value.toString();
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
                info.name = "NombreUsuario";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Avatar";
                break;
            case 4:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdAmigo";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "UsuarioAmigo";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "NombreAmigo";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "AvatarAmigo";
                break;
            default: break;
        }
    }
}
