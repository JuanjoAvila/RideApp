package com.example.juanjo.rideapp.Amigos;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.juanjo.rideapp.DTO.Usuario_adapter;
import com.example.juanjo.rideapp.Login.Login;
import com.example.juanjo.rideapp.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * @author RideApp
 * @version Final
 * Actividad principal de Amigos, aquí se agregan y eliminan relaciones de amistad
 */
public class Amigos_main extends AppCompatActivity implements SearchView.OnQueryTextListener {

    /*
    Variables utilizadas para las consultas al WebService
     */
    public static final String WS_URL = "http://rideapp2.somee.com/WebService.asmx";
    public static final String LISTA_USUARIOS_SIN_ACTUAL_METHOD = "listaUsuarios_sinUsuarioActual";
    public static final String LISTA_USUARIOS_SIN_ACTUAL_ACTION = "http://tempuri.org/listaUsuarios_sinUsuarioActual";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String LISTA_SEGUIDOS_METHOD = "Select_seguidos";
    public static final String LISTA_SEGUIDOS_ACTION = "http://tempuri.org/Select_seguidos";
    public static final String BORRAR_AMIGO_IDUSUARIO_AMIGO_METHOD = "borrarAmigo_by_idUsuario_amigo";
    public static final String BORRAR_AMIGO_IDUSUARIO_AMIGO_ACTION = "http://tempuri.org/borrarAmigo_by_idUsuario_amigo";

    private ListView usuarios;
    private AmigosAdapter adapter;
    private ArrayList<Usuario_adapter> listaUsuarios = new ArrayList<Usuario_adapter>();
    private ArrayList<Usuario_adapter> listaUsuarios_for_filter = new ArrayList<Usuario_adapter>();
    private ArrayList<Integer> idAmigos = new ArrayList<Integer>();
    private Activity activity;
    private long id = 0;
    private SearchView editsearch;
    private Amigos_main main = this;
    Amigos_seguir_dialog seguir_dialog = null;
    Amigos_no_seguir_dialog no_seguir_dialog = null;
    public Integer usuario_actual;
    public Integer usuario_a_borrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amigos_main);

        getSupportActionBar().setTitle("Buscar amigos");

        activity = this;

        usuarios = findViewById(R.id.amigos_lv);
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

        try {
            /*
            Se obtienen las relaciones de amistad actuales para el usuario actual
             */
            new amigosSeguidos(getApplicationContext()).execute(Login.getUsuari().getIdUsuario()).get();
            /*
            Se obtiene todos los usuarios de la aplicación menos el actual
             */
            new listaUsuarios_sinActual(getApplicationContext()).execute(Login.getUsuari().getIdUsuario()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Utilizado para llamar al método filter del adaptador, el cual filtra los resultados del ListView
     * @param texto Texto introducido en la barra de búsqueda
     * @return
     */
    @Override
    public boolean onQueryTextChange(String texto) {
        String text = texto;
        adapter.filter(text);
        return false;
    }

    /**
     * Utilizado para mostrar el dialogo de amigo añadido
     */
    public void mostrar_seguir_dialog() {
        FragmentManager fm = getSupportFragmentManager();
        seguir_dialog = Amigos_seguir_dialog.newInstance("Some Title");
        seguir_dialog.show(fm, "fragment_edit_name");
    }

    /**
     * Utilizado en el botón del dialogo de seguir, para borrar el dialogo
     * @param view
     */
    public void seguir(View view){
        if(seguir_dialog != null){
            seguir_dialog.dismiss();
        }
    }

    /**
     * Utilizado para mostrar el dialogo de pregunta al borrar una relación de amistad
     */
    public void mostrar_no_seguir_dialog() {
        FragmentManager fm = getSupportFragmentManager();
        no_seguir_dialog = Amigos_no_seguir_dialog.newInstance("Some Title");
        no_seguir_dialog.show(fm, "fragment_edit_name");
    }

    /**
     * Utilizado en el botón del dialogo de no_seguir, si el botón es pulsado, se ejecuta
     * la eliminación de la relación de amistad
     * @param view
     */
    public void no_seguir(View view){
        new borrarAmigo(this).execute(usuario_actual, usuario_a_borrar);
        if(no_seguir_dialog != null){
            no_seguir_dialog.dismiss();
        }
    }

    /**
     * Tarea asincrona utilizada para la obtención de todos los usuarios de la aplicación,
     * menos el actual
     */
    private class listaUsuarios_sinActual extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public listaUsuarios_sinActual(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE, LISTA_USUARIOS_SIN_ACTUAL_METHOD);
            request.addProperty("idUsuario", (int)params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(WS_URL);

            try {
                transporte.call(LISTA_USUARIOS_SIN_ACTUAL_ACTION,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                for (int i = 0; i < resSoap.getPropertyCount(); i++){
                    SoapObject iu = (SoapObject)resSoap.getProperty(i);

                    Usuario_adapter usuario_adapter = new Usuario_adapter(id, Integer.valueOf(iu.getPropertyAsString(0)), iu.getPropertyAsString(1), iu.getPropertyAsString(2), iu.getPropertyAsString(3),
                            iu.getPropertyAsString(4), iu.getPropertyAsString(5), iu.getPropertyAsString(6), iu.getPropertyAsString(7));
                    listaUsuarios.add(usuario_adapter);
                    listaUsuarios_for_filter.add(usuario_adapter);
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
                /*
                Una vez se obtienen los resultados, se crea el adaptador con los datos necesarios
                 */
                adapter = new AmigosAdapter(main, activity, listaUsuarios_for_filter, listaUsuarios, idAmigos, context);
                usuarios.setAdapter(adapter);
            }
        }
    }

    /**
     * Tarea asincrona utilizada para la obtención de las relaciones de amistad existentes para el
     * usuario actual
     */
    private class amigosSeguidos extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public amigosSeguidos(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE, LISTA_SEGUIDOS_METHOD);
            request.addProperty("idUsuario", (int)params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(WS_URL);

            try {
                transporte.call(LISTA_SEGUIDOS_ACTION,envelope);
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

    /**
     * Tarea asincrona utilizada para borrar la relación de amistad perteneciente al usuario actual
     */
    private class borrarAmigo extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public borrarAmigo(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE, BORRAR_AMIGO_IDUSUARIO_AMIGO_METHOD);
            request.addProperty("idUsuario", params[0]);
            request.addProperty("amigo", params[1]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(WS_URL);

            try {
                transporte.call(BORRAR_AMIGO_IDUSUARIO_AMIGO_ACTION,envelope);
                SoapPrimitive resSoap = (SoapPrimitive) envelope.getResponse();
                idAmigos.remove(params[1]);
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
