package com.example.juanjo.rideapp.Rutas;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.juanjo.rideapp.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Rutas_mostrar_rutas extends AppCompatActivity {

    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "listaUsuarios";
    public static final String SOAP_ACTION = "http://tempuri.org/listaUsuarios";
    public static final String NAMESPACE = "http://tempuri.org/";

    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas_mostrar_rutas);

        idUsuario = getIntent().getExtras().getInt("idUsuario");



    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class TareaWSConsulta extends AsyncTask<String,Void,Boolean> {

        private Context context;

        public TareaWSConsulta(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(String... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                for (int i = 0; i < resSoap.getPropertyCount(); i++){
                    SoapObject iu = (SoapObject)resSoap.getProperty(i);

                    /*UsuarioDTO usuario = new UsuarioDTO(Integer.valueOf(iu.getPropertyAsString(0)), iu.getPropertyAsString(1), iu.getPropertyAsString(2), iu.getPropertyAsString(3),
                            iu.getPropertyAsString(4), iu.getPropertyAsString(5), iu.getPropertyAsString(6), iu.getPropertyAsString(7));

                    usuariosDTO.add(usuario);*/
                }

            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                /*for (UsuarioDTO user: usuariosDTO) {
                    datos.add(user.getUsuario());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, datos);

                usuarios.setAdapter(adapter);*/
            }else{
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
