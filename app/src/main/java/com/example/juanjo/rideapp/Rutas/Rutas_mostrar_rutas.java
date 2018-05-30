package com.example.juanjo.rideapp.Rutas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.juanjo.rideapp.DTO.Ruta_infoDTO;
import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.RecicleViewAdapterRutas2;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

public class Rutas_mostrar_rutas extends AppCompatActivity {

    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "listaRuta_info_byIdUsuario";
    public static final String SOAP_ACTION = "http://tempuri.org/listaRuta_info_byIdUsuario";
    public static final String NAMESPACE = "http://tempuri.org/";

    private int idUsuario;

    private LinkedList<Ruta_infoDTO> info_rutas = new LinkedList<Ruta_infoDTO>();
    private RecyclerView rView_mostrar_rutas;
    private RecicleViewAdapterRutas2 adapter;
    private RecyclerView.LayoutManager lManager;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rutas_mostrar_rutas);

        getSupportActionBar().hide();

        activity = this;

        rView_mostrar_rutas = (RecyclerView)findViewById(R.id.rView_mostrar_rutas);

        idUsuario = getIntent().getExtras().getInt("idUsuario");

        Handler uiHandlerPerfil = new Handler(this.getMainLooper());
        uiHandlerPerfil.post(new Runnable() {
            @Override
            public void run() {

                try {
                    new obtenerInfoRutas(getApplicationContext()).execute(idUsuario).get();
                    adapter = new RecicleViewAdapterRutas2(info_rutas);
                    rView_mostrar_rutas.setAdapter(adapter);
                    rView_mostrar_rutas.setHasFixedSize(true);

                    lManager = new LinearLayoutManager(getApplicationContext());
                    rView_mostrar_rutas.setLayoutManager(lManager);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }


            }
        });

        rView_mostrar_rutas.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), rView_mostrar_rutas,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Ruta_infoDTO ruta = adapter.getItem(position);
                        Intent intent = new Intent(getApplicationContext(), Rutas_cargar_ruta.class);
                        intent.putExtra("idRuta", ruta.getIdRuta());
                        startActivity(intent);
                        activity.finish();
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, Rutas_main.class);
        startActivity(i);
        this.finish();
    }

    private class obtenerInfoRutas extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public obtenerInfoRutas(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            request.addProperty("idUsuario", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                for (int i = 0; i < resSoap.getPropertyCount(); i++){
                    SoapObject iu = (SoapObject)resSoap.getProperty(i);

                    Ruta_infoDTO ruta_info = new Ruta_infoDTO(Integer.valueOf(iu.getPropertyAsString(0)),
                            iu.getPropertyAsString(1), iu.getPropertyAsString(2), iu.getPropertyAsString(3),
                            Integer.valueOf(iu.getPropertyAsString(4)), Integer.valueOf(iu.getPropertyAsString(5)),
                            Integer.valueOf(iu.getPropertyAsString(6)), iu.getPropertyAsString(7));

                    info_rutas.add(ruta_info);
                }

            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){

            }else{

            }
        }
    }
}
