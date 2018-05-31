package com.example.juanjo.rideapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

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

public class Registro extends AppCompatActivity {
    public static final String URL = "http://rideapp2.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "nuevoUsuario";
    public static final String SOAP_ACTION = "http://tempuri.org/nuevoUsuario";
    public static final String NAMESPACE = "http://tempuri.org/";
    public AutoCompleteTextView loginusuario, logincontrasena, loginrepetircontrasena,loginnombre,loginapellidos,logincorreo;
    private UsuarioDTO user = null;
    String idUsuario2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //Al apretar para logear con el usuario o contraseña que no se mueva el fondo
        getWindow().setBackgroundDrawableResource(R.drawable.fondo);
        //Sirve para que no salga al inciar el login , el pop up para escribir .
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loginusuario = findViewById(R.id.loginusuario);
        logincontrasena = findViewById(R.id.logincontraseña);
        loginrepetircontrasena = findViewById(R.id.loginrepetircontraseña);
        loginnombre = findViewById(R.id.loginnombre);
        loginapellidos = findViewById(R.id.loginapellidos);
        logincorreo = findViewById(R.id.logincorreo);
    }
    public void anadir(View view) {
        idUsuario2 = loginusuario.getText().toString();
        String password2 = logincontrasena.getText().toString();
        String nombre2 = loginnombre.getText().toString();
        String apellidos2 = loginapellidos.getText().toString();
        String correo2 = logincorreo.getText().toString();
        TareaWSConsultaUsuario twsc = new TareaWSConsultaUsuario(this);
        twsc.execute(String.valueOf(loginusuario.getText()));
        if(!idUsuario2.equals("") || !password2.equals("") || !nombre2.equals("") || !apellidos2.equals("")) {
                if (!loginusuario.getText().toString().isEmpty()) {
                    if (!logincontrasena.getText().toString().isEmpty()) {
                        if (!loginnombre.getText().toString().isEmpty()) {
                            if (!loginapellidos.getText().toString().isEmpty()) {
                                if (loginusuario.length() >= 5 && loginusuario.length() <= 15) {
                                    if (logincontrasena.getText().toString().equals(loginrepetircontrasena.getText().toString())) {
                                        if (!validarEmail(correo2)) {
                                            logincorreo.setError("Email no válido");
                                        } else {
                                            new TareaWSConsulta(getApplicationContext()).execute(idUsuario2, password2, nombre2, apellidos2, "", "", correo2);
                                            startActivity(new Intent(getBaseContext(), Login.class)
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                                            finish();
                                        }
                                    } else {
                                        loginrepetircontrasena.setError("Contraseña incorrecta");
                                        Toast.makeText(getApplicationContext(), "Introduzca la misma contraseña para confirmar", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    loginusuario.setError("Usuario incorrecto");
                                    Toast.makeText(getApplicationContext(), "Porfavor introduzca un usuario de entre 5 y 15 caracteres", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                loginapellidos.setError("Campo Obligatorio");
                                Toast.makeText(getApplicationContext(), "Porfavor introduzca sus apellidos", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            loginnombre.setError("Campo Obligatorio");
                            Toast.makeText(getApplicationContext(), "Porfavor introduzca su nombre", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        logincontrasena.setError("Campo Obligatorio");
                        Toast.makeText(getApplicationContext(), "Porfavor introduza una contraseña", Toast.LENGTH_LONG).show();
                    }
                } else {
                    loginusuario.setError("Campo Obligatorio");
                    Toast.makeText(getApplicationContext(), "Porfavor introduzca un usuario", Toast.LENGTH_LONG).show();
                }
        }else{
            loginusuario.setError("Campo Obligatorio");
            logincontrasena.setError("Campo Obligatorio");
            loginrepetircontrasena.setError("Campo Obligatorio");
            loginnombre.setError("Campo Obligatorio");
            loginapellidos.setError("Campo Obligatorio");
            Toast.makeText(getApplicationContext(),"Porfavor introduzca los datos correspondientes",Toast.LENGTH_LONG).show();
        }

    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "UsuarioDTO", usuario.getClass());

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION,envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                result = Integer.parseInt(response.toString());
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Integer result) {

            if(result.equals(0)){
                Toast.makeText(this.context, "Este usuario ya existe", Toast.LENGTH_LONG).show();
                loginusuario.setError("");
            }else{


            }
        }
    }
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    @SuppressLint("StaticFieldLeak")
    private class TareaWSConsultaUsuario extends AsyncTask<String,Void,Boolean> {

        private Context context;

        private TareaWSConsultaUsuario(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(String... params) {

            return existeUsuario(params[0]);
        }

        protected void onPostExecute(Boolean result) {

        }
    }

    @NonNull
    private Boolean existeUsuario(String param) {
        Boolean result = true;

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("idUsuario", param);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION,envelope);
            SoapObject resSoap = (SoapObject)envelope.getResponse();

            // Se corrige el password obtenido, eliminando caracteres de control '%00'. Acuerdate que tambien elimina espacios, VALIDAR PASS AL CREAR USUARIO
            String pass = resSoap.getPropertyAsString(2).replaceAll("\\W", "");
            user = new UsuarioDTO(Integer.valueOf(resSoap.getPropertyAsString(0)), resSoap.getPropertyAsString(1), pass, resSoap.getPropertyAsString(3),
                    resSoap.getPropertyAsString(4), resSoap.getPropertyAsString(5), resSoap.getPropertyAsString(6), resSoap.getPropertyAsString(7));

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }


        return result;
    }
}
