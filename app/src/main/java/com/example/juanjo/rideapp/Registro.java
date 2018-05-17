package com.example.juanjo.rideapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Registro extends AppCompatActivity {
    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "nuevoUsuario";
    public static final String SOAP_ACTION = "http://tempuri.org/nuevoUsuario";
    public static final String NAMESPACE = "http://tempuri.org/";
    public AutoCompleteTextView loginusuario,logincontraseña,loginrepetircontraseña,loginnombre,loginapellidos,logincorreo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //Al apretar para logear con el usuario o contraseña que no se mueva el fondo
        getWindow().setBackgroundDrawableResource(R.drawable.fondo);
        //Sirve para que no salga al inciar el login , el pop up para escribir .
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loginusuario = findViewById(R.id.loginusuario);
        logincontraseña = findViewById(R.id.logincontraseña);
        loginrepetircontraseña = findViewById(R.id.loginrepetircontraseña);
        loginnombre = findViewById(R.id.loginnombre);
        loginapellidos = findViewById(R.id.loginapellidos);
        logincorreo = findViewById(R.id.logincorreo);
    }
    public void añadir(View view){
        String idUsuario2 = loginusuario.getText().toString();
        String password2 = logincontraseña.getText().toString();
        String nombre2 = loginnombre.getText().toString();
        String apellidos2 = loginapellidos.getText().toString();
        String correo2 = logincorreo.getText().toString();
        if(logincontraseña.getText().toString().equals(loginrepetircontraseña.getText().toString())) {
            new TareaWSConsulta(getApplicationContext()).execute(idUsuario2, password2, nombre2, apellidos2, "", "", correo2);
            startActivity(new Intent(getBaseContext(), Login.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"No son la misma contraseña",Toast.LENGTH_SHORT).show();
        }

    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class TareaWSConsulta extends AsyncTask<String,Void,Integer> {

        private Context context;

        public TareaWSConsulta(Context context) {
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Integer result) {

            if(result.equals(new Integer(0))){
                Toast.makeText(this.context, "No se ha podido crear el usuario", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this.context, "Se ha creado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
