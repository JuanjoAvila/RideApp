package com.example.juanjo.rideapp.Evento;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.juanjo.rideapp.DTO.EventosRutasDTO;
import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.Rutas.Rutas_recyclerView_clickListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Clase encargada de gestionar la ventana de la lista de eventos.
 */
public class Eventos extends AppCompatActivity {
    public static final String URL = "http://rideapp2.somee.com/WebService.asmx";
    public static final String METHOD_NAME_EVENTOS = "Select_eventos_rutas";
    public static final String SOAP_ACTION_EVENTOS = "http://tempuri.org/Select_eventos_rutas";
    public static final String NAMESPACE = "http://tempuri.org/";

    private RecyclerView eventosRecyclerView;
    private ArrayList<EventosRutasDTO> listaEventos;
    private Context mContext;
    private RVEventos adapterEvento;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        mContext = this;
        eventosRecyclerView = findViewById(R.id.Eventos_ListaEventos);
        eventosRecyclerView.setHasFixedSize(true);
        listaEventos = new ArrayList<EventosRutasDTO>();
        cargarRecyclerView();
    }

    public void cargarRecyclerView(){
        Handler uiHandlerRecyclerViews = new Handler(this.getMainLooper());
        uiHandlerRecyclerViews.post(new Runnable() {
            @Override
            public void run() {
                ConsultaEventos selectEventos = new ConsultaEventos(mContext);
                try {
                    selectEventos.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                layoutManager = new LinearLayoutManager(mContext);
                eventosRecyclerView.setLayoutManager(layoutManager);
                adapterEvento = new RVEventos(listaEventos);
                eventosRecyclerView.setAdapter(adapterEvento);
            }
        });
        eventosRecyclerView.addOnItemTouchListener(
                new Rutas_recyclerView_clickListener(getApplicationContext(), eventosRecyclerView,new Rutas_recyclerView_clickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        EventosRutasDTO ruta = adapterEvento.getEvento(position);
                        Intent intent = new Intent(getApplicationContext(), Evento.class);
                        intent.putExtra("idEvento", ruta.getIdEvento());
                        startActivity(intent);
                    }
                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }
    public void crearEvento(View view){
        Intent i = new Intent(this, CrearEvento.class );
        startActivity(i);
    }

    private class ConsultaEventos extends AsyncTask<String,Void,Boolean> {

        private Context context;

        public ConsultaEventos(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(String... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_EVENTOS);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_EVENTOS,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                for (int i = 0; i < resSoap.getPropertyCount(); i++){
                    SoapObject iu = (SoapObject)resSoap.getProperty(i);
                    EventosRutasDTO eventos_info = new EventosRutasDTO(Integer.valueOf(iu.getPropertyAsString(0)),
                            Integer.valueOf(iu.getPropertyAsString(1)), Integer.valueOf(iu.getPropertyAsString(2)),
                            iu.getPropertyAsString(3),(iu.getPropertyAsString(4)),Integer.valueOf(iu.getPropertyAsString(5)),
                            Integer.valueOf(iu.getPropertyAsString(6)), iu.getPropertyAsString(7),
                            iu.getPropertyAsString(8), iu.getPropertyAsString(9), Integer.valueOf(iu.getPropertyAsString(10)),
                            Integer.valueOf(iu.getPropertyAsString(11)), Integer.valueOf(iu.getPropertyAsString(12)),
                            iu.getPropertyAsString(13),iu.getPropertyAsString(14));

                    listaEventos.add(eventos_info);
                }
            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
            }else {
                Toast.makeText(getApplicationContext(), "Error al cargar los eventos.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
