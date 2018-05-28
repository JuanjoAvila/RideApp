package com.example.juanjo.rideapp.Usuario;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanjo.rideapp.Login;
import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.DTO.UsuarioDTO;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.regex.Pattern;

public class EdicionPerfil extends AppCompatActivity {
    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "modificarUsuario";
    public static final String SOAP_ACTION = "http://tempuri.org/modificarUsuario";
    public static final String NAMESPACE = "http://tempuri.org/";
    private AutoCompleteTextView password;
    private AutoCompleteTextView contraseña2;
    private AutoCompleteTextView nombre;
    private AutoCompleteTextView apellidos;
    private AutoCompleteTextView avatar;
    private AutoCompleteTextView descripcion;
    private AutoCompleteTextView correo;
    private TextView cambiar_contrasena, cambiar_nombre,cambiar_apellidos,cambiar_correo,cambiar_descripcion;
    private UsuarioDTO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_perfil);
        contraseña2 =findViewById(R.id.contrasena_repetida_perfil);
        password = findViewById(R.id.contrasena_perfil);
        nombre = findViewById(R.id.nombre_perfil);
        apellidos = findViewById(R.id.apellidos_perfil);
        correo = findViewById(R.id.correo_perfil);
        descripcion = findViewById(R.id.descripcion_perfil);
        cambiar_contrasena = findViewById(R.id.cambiar_contrasena);
        cambiar_nombre = findViewById(R.id.cambiar_nombre);
        cambiar_apellidos = findViewById(R.id.cambiar_apellidos);
        cambiar_correo = findViewById(R.id.cambiar_correo);
        cambiar_descripcion = findViewById(R.id.cambiar_descripcion);
    }

    public void update(View view) {
        new TareaWSConsulta2(getApplicationContext()).execute(Login.user.getIdUsuario().toString());
    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    @SuppressLint("StaticFieldLeak")
    private class TareaWSConsulta extends AsyncTask<UsuarioDTO, Void, Integer> {

        private Context context;

        TareaWSConsulta(Context context) {
            this.context = context;
        }

        protected Integer doInBackground(UsuarioDTO... params) {

            Integer result = 0;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("usuario");
            pi.setValue(params[0]);
            pi.setType(params[0].getClass());

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "UsuarioDTO", params[0].getClass());

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
            if (result.equals(0)) {
                Toast.makeText(this.context, "No se ha podido modificar el usuario", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.context, "Se ha modificado correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    @SuppressLint("StaticFieldLeak")
    private class TareaWSConsulta2 extends AsyncTask<String, Void, Boolean> {

        private Context context;

        TareaWSConsulta2(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(String... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE, EdicionPerfil.METHOD_NAME);
            request.addProperty("idUsuario", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(EdicionPerfil.SOAP_ACTION, envelope);
                SoapObject resSoap = (SoapObject) envelope.getResponse();
                user = new UsuarioDTO(Integer.valueOf(resSoap.getPropertyAsString(0)), resSoap.getPropertyAsString(1), resSoap.getPropertyAsString(2), resSoap.getPropertyAsString(3),
                        resSoap.getPropertyAsString(4), resSoap.getPropertyAsString(5), resSoap.getPropertyAsString(6), resSoap.getPropertyAsString(7));

            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }


            return result;
        }
        private boolean validarEmail(String email) {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            return pattern.matcher(email).matches();
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                if(cambiar_contrasena.isPressed()){
                    if(password.getText().toString().equals("")){
                        password.setError("Introduce la contraseña");
                    }else{
                        if(password.getText().toString().equals(contraseña2.getText().toString())) {
                            user.setPassword(password.getText().toString());
                        }else {
                            contraseña2.setError("Introduce la misma contraseña");
                        }
                    }
                }
                if(cambiar_nombre.isPressed()) {
                    if(nombre.getText().toString().equals("")){
                        nombre.setError("Introduce el nombre");
                    }else{
                        user.setNombre(nombre.getText().toString());
                    }
                }
                if(cambiar_apellidos.isPressed()){
                    if(apellidos.getText().toString().equals("")){
                        apellidos.setError("Introduce los apellidos");
                    }else {
                        user.setApellidos(apellidos.getText().toString());
                    }
                }
                if(cambiar_correo.isPressed()){
                    if(correo.getText().toString().equals("")) {
                        correo.setError("Introduce el correo");
                    }else{
                        if(validarEmail(correo.getText().toString())){
                            user.setCorreo(correo.getText().toString());
                        }else{
                            correo.setError("Introduce un correo valido");
                        }
                    }
                }
                if(cambiar_descripcion.isPressed()){
                    if(descripcion.getText().toString().equals("")){
                        descripcion.setError("Introduzca una descripcion");
                    }else{
                        user.setDescripcion(descripcion.getText().toString());
                    }
                }
                new TareaWSConsulta(getApplicationContext()).execute(user);
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
