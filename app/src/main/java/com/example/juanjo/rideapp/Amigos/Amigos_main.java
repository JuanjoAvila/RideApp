package com.example.juanjo.rideapp.Amigos;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.juanjo.rideapp.DTO.AmigoDTO;
import com.example.juanjo.rideapp.DTO.UsuarioDTO;
import com.example.juanjo.rideapp.DTO.Usuario_adapter;
import com.example.juanjo.rideapp.Login;
import com.example.juanjo.rideapp.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Amigos_main extends AppCompatActivity {

    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "listaUsuarios_sinUsuarioActual";
    public static final String SOAP_ACTION = "http://tempuri.org/listaUsuarios_sinUsuarioActual";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String METHOD_NAME2 = "Select_seguidos";
    public static final String SOAP_ACTION2 = "http://tempuri.org/Select_seguidos";

    private ListView usuarios;
    private AmigosAdapter adapter;
    private ArrayList<Usuario_adapter> listaUsuarios = new ArrayList<Usuario_adapter>();
    private ArrayList<Integer> idAmigos = new ArrayList<Integer>();
    private Activity activity;
    private long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amigos_main);

        getSupportActionBar().hide();

        activity = this;

        usuarios = findViewById(R.id.amigos_lv);

        try {
            new amigosSeguidos(getApplicationContext()).execute(Login.getUsuari().getIdUsuario()).get();
            new listaUsuarios_sinActual(getApplicationContext()).execute(Login.getUsuari().getIdUsuario()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class listaUsuarios_sinActual extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public listaUsuarios_sinActual(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            request.addProperty("idUsuario", (int)params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                for (int i = 0; i < resSoap.getPropertyCount(); i++){
                    SoapObject iu = (SoapObject)resSoap.getProperty(i);

                    Usuario_adapter usuario_adapter = new Usuario_adapter(id, Integer.valueOf(iu.getPropertyAsString(0)), iu.getPropertyAsString(1), iu.getPropertyAsString(2), iu.getPropertyAsString(3),
                            iu.getPropertyAsString(4), iu.getPropertyAsString(5), iu.getPropertyAsString(6), iu.getPropertyAsString(7));
                    listaUsuarios.add(usuario_adapter);
                    id++;
                }

            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                adapter = new AmigosAdapter(activity, listaUsuarios, idAmigos, context);
                usuarios.setAdapter(adapter);
            }else{
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class amigosSeguidos extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public amigosSeguidos(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME2);
            request.addProperty("idUsuario", (int)params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION2,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                for (int i = 0; i < resSoap.getPropertyCount(); i++){
                    SoapPrimitive iu = (SoapPrimitive)resSoap.getProperty(i);
                    Integer amigo = Integer.valueOf((String)iu.getValue());
                    idAmigos.add(amigo);
                }

            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(Boolean result) {

        }
    }

}
