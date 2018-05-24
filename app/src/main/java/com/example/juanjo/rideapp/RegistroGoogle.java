package com.example.juanjo.rideapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.juanjo.rideapp.DTO.UsuarioDTO;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Objects;

public class RegistroGoogle {
    private static final String URL = "http://rideapp.somee.com/WebService.asmx";
    private static final String METHOD_NAME = "nuevoUsuario";
    private static final String SOAP_ACTION = "http://tempuri.org/nuevoUsuario";
    private static final String NAMESPACE = "http://tempuri.org/";
    private String idUsuario2;
    private String password1;
    private String password2;
    private String nombre2;
    private String imagen;
    private String apellidos2;
    private String correo2;
    private Context mContext;

    RegistroGoogle(GoogleSignInResult result, Context context) {
        idUsuario2 = Objects.requireNonNull(result.getSignInAccount()).getId();
        password1 = result.getSignInAccount().getId();
        password2 = result.getSignInAccount().getId();
        nombre2 = result.getSignInAccount().getGivenName();
        imagen = String.valueOf(result.getSignInAccount().getPhotoUrl());
        apellidos2 = result.getSignInAccount().getFamilyName();
        correo2 = result.getSignInAccount().getEmail();
        mContext = context;
    }



    public void anadir(){

        if(password2.equals(password1)) {
            new RegistroGoogle.TareaWSConsulta(mContext).execute(idUsuario2+"google", password2, nombre2, apellidos2, imagen, "", correo2);
        }else{
            Toast.makeText(mContext(),"No son la misma contraseña",Toast.LENGTH_SHORT).show();
        }

    }

    private Context mContext() {
        return mContext;
    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    @SuppressLint("StaticFieldLeak")
    private class TareaWSConsulta extends AsyncTask<String,Void,Integer> {

        private Context context;

        private TareaWSConsulta(Context context) {
            this.context = context;
        }

        protected Integer doInBackground(String... params) {

            Integer result = 0;

            UsuarioDTO usuario = new UsuarioDTO(params[0], params[1], params[2], params[3], params[4], params[5], params[6]);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("usuario");
            pi.setValue(usuario);
            pi.setType(usuario.getClass());

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "UsuarioDTO", usuario.getClass());

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                result = Integer.parseInt(response.toString());
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Integer result) {

        }
    }
}
