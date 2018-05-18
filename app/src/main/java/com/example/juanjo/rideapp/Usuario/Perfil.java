package com.example.juanjo.rideapp.Usuario;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanjo.rideapp.DTO.AmigoDTO;
import com.example.juanjo.rideapp.Login;
import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.DTO.UsuarioDTO;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

public class Perfil extends AppCompatActivity {
    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME_AMIGOS = "listaAmigos";
    public static final String SOAP_ACTION_AMIGOS = "http://tempuri.org/listaAmigos";
    public static final String METHOD_NAME_USUARIO = "obtenerUsuario_byIdUsuario";
    public static final String SOAP_ACTION_USUARIO = "http://tempuri.org/obtenerUsuario_byIdUsuario";
    public static final String NAMESPACE = "http://tempuri.org/";
    private UsuarioDTO usuarioActivo;
    private Integer usuarioActivoID;
    private UsuarioDTO usuario;
    private ImageView avatar;
    private TextView usuarioNick;
    private RecyclerView seguidoresRecycler;
    private RecyclerView seguidosRecycler;
    private TextView descripcion;
    private TextView rutasCont;
    private LinkedList<AmigoDTO> amigosDTO;
    private LinkedList<Integer> seguidoresID;
    private LinkedList<Integer> seguidosID;
    private LinkedList<String> seguidoresNombres;
    private LinkedList<String> seguidoresAvatares;
    private LinkedList<String> seguidosNombres;
    private LinkedList<String> seguidosAvatares;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        avatar = findViewById(R.id.Perfil_usuarioAvatar);
        usuarioNick = findViewById(R.id.Perfil_usuarioID);
        seguidosRecycler = findViewById(R.id.Perfil_recyclerViewSeguidos);
        seguidoresRecycler = findViewById(R.id.Perfil_recyclerViewSeguidores);
        descripcion = findViewById(R.id.Perfil_descripcionUsuario);
        rutasCont = findViewById(R.id.Perfil_rutasCont);
        amigosDTO = new LinkedList<AmigoDTO>();
        seguidoresID = new LinkedList<Integer>();
        seguidosID = new LinkedList<Integer>();
        seguidoresNombres = new LinkedList<String>();
        seguidoresAvatares = new LinkedList<String>();
        seguidosNombres = new LinkedList<String>();
        seguidosAvatares = new LinkedList<String>();
        usuarioActivo = Login.getUsuari();
        try {
            iniciacionDatos();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void iniciacionDatos() throws ExecutionException, InterruptedException {
        new consultaAmigos(this).execute();
        cargarRecyclerLists();
    }
    private void cargarRecyclerLists() throws ExecutionException, InterruptedException {

        for(Integer id: seguidoresID){
            UsuarioDTO seguidor = new consultaUsuario(this).execute(id).get();
            if(seguidor!=null){
                seguidoresAvatares.add(seguidor.getAvatar());
                seguidoresNombres.add(seguidor.getUsuario());
            }
        }
        for(Integer id: seguidosID){
            UsuarioDTO seguido = new consultaUsuario(this).execute(id).get();
            if(seguido!=null) {
                seguidosAvatares.add(seguido.getAvatar());
                seguidosNombres.add(seguido.getUsuario());
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class consultaAmigos extends AsyncTask<String,Void,Boolean> {

        private Context context;

        public consultaAmigos(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(String... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_AMIGOS);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_AMIGOS,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                for (int i = 0; i < resSoap.getPropertyCount(); i++){
                    SoapObject iu = (SoapObject)resSoap.getProperty(i);
                    iu.getPropertyAsString(0);
                    AmigoDTO amigo = new AmigoDTO(Integer.valueOf(iu.getPropertyAsString(0)), Integer.valueOf(iu.getPropertyAsString(1)),Integer.valueOf(iu.getPropertyAsString(2)));

                    amigosDTO.add(amigo);
                }
            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                for(AmigoDTO amigo: amigosDTO){
                    if(amigo.getidUsuario()==usuarioActivo.getIdUsuario()){
                        seguidosID.add(amigo.getamigo());
                    }
                    else if(amigo.getamigo()==usuarioActivo.getIdUsuario()){
                        seguidoresID.add(amigo.getidUsuario());
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(), "Error al cargar seguidoresID/seguidosID", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class consultaUsuario extends AsyncTask<Integer,Void,UsuarioDTO> {

        private Context context;

        public consultaUsuario(Context context) {
            this.context = context;
        }

        protected UsuarioDTO doInBackground(Integer... params) {

            UsuarioDTO result = null;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_USUARIO);
            request.addProperty("idUsuario", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_USUARIO,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                // Se corrige el password obtenido, eliminando caracteres de control '%00'. Acuerdate que tambien elimina espacios, VALIDAR PASS AL CREAR USUARIO
                String pass = resSoap.getPropertyAsString(2).replaceAll("\\W", "");
                result = new UsuarioDTO(Integer.valueOf(resSoap.getPropertyAsString(0)), resSoap.getPropertyAsString(1), pass, resSoap.getPropertyAsString(3),
                        resSoap.getPropertyAsString(4), resSoap.getPropertyAsString(5), resSoap.getPropertyAsString(6), resSoap.getPropertyAsString(7));

            } catch (Exception e) {
                return result;
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){

            }else{
                Toast.makeText(getApplicationContext(), "Usuario incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
