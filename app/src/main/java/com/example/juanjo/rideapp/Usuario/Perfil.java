package com.example.juanjo.rideapp.Usuario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanjo.rideapp.DTO.AmigoDTO;
import com.example.juanjo.rideapp.DTO.RelacionAmigoDTO;
import com.example.juanjo.rideapp.DTO.UsuarioDTO;
import com.example.juanjo.rideapp.FTP.FTPManager;
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

/**
 * Clase controladora del Perfil del usuario o de un seguidor/seguido.
 * En el perfil se muestran los datos del usuario, sus seguidores y sus seguidos además de un acceso a sus rutas
 */
public class Perfil extends AppCompatActivity {
    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME_AMIGOS = "Select_amigos_usuarios";
    public static final String SOAP_ACTION_AMIGOS = "http://tempuri.org/Select_amigos_usuarios";
    public static final String METHOD_NAME_USUARIO = "obtenerUsuario_byIdUsuario";
    public static final String SOAP_ACTION_USUARIO = "http://tempuri.org/obtenerUsuario_byIdUsuario";
    public static final String METHOD_NAME_FOLLOW = "nuevoAmigo";
    public static final String SOAP_ACTION_FOLLOW = "http://tempuri.org/nuevoAmigo";
    public static final String METHOD_NAME_UNFOLLOW = "borrarAmigo_by_idUsuario_amigo";
    public static final String SOAP_ACTION_UNFOLLOW = "http://tempuri.org/borrarAmigo_by_idUsuario_amigo";
    public static final String NAMESPACE = "http://tempuri.org/";
    private UsuarioDTO usuarioPerfil;
    private UsuarioDTO usuarioActivo;
    private Integer usuarioPerfilID;
    private UsuarioDTO usuario;
    private ImageView avatar;
    private ImageView seguirUsuario;
    private ImageView dejarseguirUsuario;
    private Button verRutasButton;
    private TextView usuarioNick;
    private TextView usuarioNombre;
    private RecyclerView seguidoresRecycler;
    private RecyclerView seguidosRecycler;
    private TextView descripcion;
    private ArrayList<RelacionAmigoDTO> amigosDTO;
    private ArrayList<Integer> seguidoresID;
    private ArrayList<Integer> seguidosID;
    private ArrayList<String> seguidoresUsuarios;
    private ArrayList<String> seguidoresNombres;
    private ArrayList<String> seguidoresAvatares;
    private ArrayList<String> seguidosUsuarios;
    private ArrayList<String> seguidosNombres;
    private ArrayList<String> seguidosAvatares;
    private FTPManager ftpManager;
    private Activity mContext;
    private ImageView Perfil_configUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_perfil);
        avatar = findViewById(R.id.Perfil_usuarioAvatar);
        usuarioNick = findViewById(R.id.Perfil_usuarioID);
        usuarioNombre = findViewById(R.id.Perfil_nombreCuenta);
        seguidosRecycler = findViewById(R.id.Perfil_recyclerViewSeguidos);
        seguidoresRecycler = findViewById(R.id.Perfil_recyclerViewSeguidores);
        descripcion = findViewById(R.id.Perfil_descripcionUsuario);
        verRutasButton = findViewById(R.id.Perfil_botonRutas);
        amigosDTO = new ArrayList<RelacionAmigoDTO>();
        seguidoresID = new ArrayList<Integer>();
        seguidosID = new ArrayList<Integer>();
        seguidoresUsuarios = new ArrayList<String>();
        seguidoresNombres = new ArrayList<String>();
        seguidoresAvatares = new ArrayList<String>();
        seguidosUsuarios = new ArrayList<String>();
        seguidosNombres = new ArrayList<String>();
        seguidosAvatares = new ArrayList<String>();
        usuarioActivo = Login.getUsuari();
        seguirUsuario = findViewById(R.id.Perfil_seguirUsuario);
        dejarseguirUsuario = findViewById(R.id.Perfil_dejarseguirUsuario);
        Perfil_configUsuario = findViewById(R.id.Perfil_configUsuario);
        ftpManager = new FTPManager(this);
        try {
            iniciacionDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accederEdicionPerfil(View View){
        Intent i = new Intent(this, EdicionPerfil.class );
        startActivity(i);
    }

    /**
     * Método de inicialización de los datos del perfil.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void iniciacionDatos(){
        Handler uiHandlerPerfil = new Handler(this.getMainLooper());
        uiHandlerPerfil.post(new Runnable() {
            @Override
            public void run() {
                Log.d("PRUEBAS", "ANTES CARGAR PERFIL: "+System.currentTimeMillis());
                cargarDatosPerfil();
            }
        });
        Log.d("PRUEBAS", "ANTES CONSULTA AMIGOS: "+System.currentTimeMillis());
        ConsultaAmigos selectAmigos = new ConsultaAmigos(mContext);
                selectAmigos.execute();
    }

    /**
     * Obtiene el ID del usuario pasado con el intent que llama a la clase.
     * Una vez obtenida la ID, se genera la consulta del usuario a la BD y rellena los campos del perfil con los datos del usuario.
     */
    private void cargarDatosPerfil(){
        usuarioPerfilID = getIntent().getExtras().getInt("usuario");
        try {
            usuarioPerfil = new ConsultaUsuario(this).execute(usuarioPerfilID).get();
            if(usuarioPerfil !=null){
                if(usuarioPerfil.getAvatar()!=null && usuarioPerfil.getAvatar().length()>0){
                    Bitmap bitmapAvatar = null;
                    try {
                        if(usuarioPerfil.getUsuario().endsWith("google")){
                            usuarioNick.setText(usuarioPerfil.getNombre()+" "+usuarioPerfil.getApellidos());
                            bitmapAvatar = ftpManager.HTTPCargarImagen(usuarioPerfil.getAvatar());
                        }
                        else {
                            usuarioNick.setText(usuarioPerfil.getUsuario());
                            byte[] decodeValue = Base64.decode(usuarioPerfil.getAvatar(), Base64.DEFAULT);
                            bitmapAvatar = BitmapFactory.decodeByteArray(decodeValue, 0, decodeValue.length);
                        }
                        if(bitmapAvatar!=null) {
                            avatar.setImageBitmap(bitmapAvatar);
                        }
                        else{
                            avatar.setImageResource(R.drawable.user_default);
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        avatar.setImageResource(R.drawable.user_default);
                    }
                }
                else{
                    avatar.setImageResource(R.drawable.user_default);
                }
                usuarioNombre.setText(usuarioPerfil.getNombre()+" "+usuarioPerfil.getApellidos());
                descripcion.setText(usuarioPerfil.getDescripcion());
                Log.d("PRUEBAS", "DESPUÉS CARGAR PERFIL: "+System.currentTimeMillis());

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al cargar el perfil", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Recorre la lista de seguidoresID y seguidosID y carga los ArrayList de avatares y nombres que luego se enviarán al adaptador del RecyclerView.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void cargarRecyclerLists(){
        Log.d("PRUEBAS", "ANTES CARGAR ARRAYS: "+System.currentTimeMillis());
        for(RelacionAmigoDTO amigo: amigosDTO){
            if(amigo.getIdUsuario()== usuarioPerfil.getIdUsuario()){
                seguidosID.add(amigo.getIdAmigo());
                seguidosUsuarios.add(amigo.getUsuarioAmigo());
                seguidosNombres.add(amigo.getNombreAmigo());
                seguidosAvatares.add(amigo.getAvatarAmigo());
            }
            else if(amigo.getIdAmigo()== usuarioPerfil.getIdUsuario()){
                seguidoresID.add(amigo.getIdUsuario());
                seguidoresUsuarios.add(amigo.getUsuario());
                seguidoresNombres.add(amigo.getNombreUsuario());
                seguidoresAvatares.add(amigo.getAvatar());
            }
        }
        Log.d("PRUEBAS", "DESPUES CARGAR ARRAYS: "+System.currentTimeMillis());
        if(!usuarioPerfilID.equals(usuarioActivo.getIdUsuario())){
            if(!seguidoresID.contains(usuarioActivo.getIdUsuario())){
                seguirUsuario.setVisibility(View.VISIBLE);
                verRutasButton.setVisibility(View.INVISIBLE);
                verRutasButton.setVisibility(View.INVISIBLE);
            }
            else {
                seguirUsuario.setVisibility(View.INVISIBLE);
                verRutasButton.setVisibility(View.VISIBLE);
                dejarseguirUsuario.setVisibility(View.VISIBLE);
            }
        }
        else{
            Perfil_configUsuario.setVisibility(View.VISIBLE);
            verRutasButton.setVisibility(View.VISIBLE);
        }
        generarRecyclerLists();
    }

    /**
     * Carga los RecyclerList del perfil.
     */
    private void generarRecyclerLists(){
        Log.d("PRUEBAS", "ANTES GENERAR RECYCLERVIEW: "+System.currentTimeMillis());
        Handler uiHandlerRecyclerViewSeguidores = new Handler(this.getMainLooper());
        uiHandlerRecyclerViewSeguidores.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                seguidoresRecycler.setLayoutManager(layoutManager);
                Perfil_RVASeguidos adapter = new Perfil_RVASeguidos(mContext, seguidoresUsuarios, seguidoresAvatares, seguidoresID, seguidoresNombres);
                seguidoresRecycler.setAdapter(adapter);
            }
        });
        Log.d("PRUEBAS", "ANTES GENERAR RECYCLERVIEW2: "+System.currentTimeMillis());
        Handler uiHandlerRecyclerViewSeguidos = new Handler(this.getMainLooper());
        uiHandlerRecyclerViewSeguidos.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                seguidosRecycler.setLayoutManager(layoutManager2);
                Perfil_RVASeguidos adapter2 = new Perfil_RVASeguidos(mContext, seguidosUsuarios, seguidosAvatares, seguidosID, seguidosNombres);
                seguidosRecycler.setAdapter(adapter2);
            }
        });
        Log.d("PRUEBAS", "DESPUES GENERAR RECYCLERVIEW: "+System.currentTimeMillis());
    }

    public void seguirUsuario(View view){
        SeguirUsuario follow = new SeguirUsuario(this);
        Boolean exito = false;
        try {
            exito = follow.execute(usuarioPerfilID).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(exito) {
            Handler uiHandlerRecyclerViews = new Handler(this.getMainLooper());
            uiHandlerRecyclerViews.post(new Runnable() {
                @Override
                public void run() {
                    ConsultaAmigos selectAmigos = new ConsultaAmigos(mContext);
                    selectAmigos.execute();
                }
            });
            seguirUsuario.setVisibility(View.INVISIBLE);
            dejarseguirUsuario.setVisibility(View.VISIBLE);
            verRutasButton.setVisibility(View.VISIBLE);

        }
    }

    public void dejarSeguirUsuario(View view){
        DejarSeguirUsuario unfollow = new DejarSeguirUsuario(this);
        Boolean exito = false;
        try {
            exito = unfollow.execute(usuarioPerfilID).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(exito) {
            Handler uiHandlerRecyclerViews = new Handler(this.getMainLooper());
            uiHandlerRecyclerViews.post(new Runnable() {
                @Override
                public void run() {
                    ConsultaAmigos selectAmigos = new ConsultaAmigos(mContext);
                    selectAmigos.execute();
                }
            });
            seguirUsuario.setVisibility(View.VISIBLE);
            dejarseguirUsuario.setVisibility(View.INVISIBLE);
            verRutasButton.setVisibility(View.INVISIBLE);
        }
    }

    public void cargarRutas(View view){
        Intent i = new Intent(this, Perfil_mostrar_rutas.class );
        i.putExtra("idPerfil", usuarioPerfilID);
        startActivity(i);
    }
    /**
     * Tarea Asíncrona para llamar al WS de consulta en segundo plano para obtener todos los datos de la tabla Amigos de la BD.
     */
    private class ConsultaAmigos extends AsyncTask<String,Void,Boolean> {

        private Context context;

        public ConsultaAmigos(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(String... params) {

            amigosDTO.clear();
            seguidoresID.clear();
            seguidosID.clear();
            seguidoresUsuarios.clear();
            seguidoresNombres.clear();
            seguidoresAvatares.clear();
            seguidosUsuarios.clear();
            seguidosNombres.clear();
            seguidosAvatares.clear();

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
                    Integer IdUsuario = Integer.valueOf(iu.getPropertyAsString(0));
                    String Usuario = iu.getPropertyAsString(1);
                    String NombreUsuario = iu.getPropertyAsString(2);
                    String Avatar = iu.getPropertyAsString(3);
                    Integer IdAmigo = Integer.valueOf(iu.getPropertyAsString(4));
                    String UsuarioAmigo = iu.getPropertyAsString(5);
                    String NombreAmigo = iu.getPropertyAsString(6);
                    String AvatarAmigo = iu.getPropertyAsString(7);
                    RelacionAmigoDTO amigo = new RelacionAmigoDTO();
                    amigo.setIdUsuario(IdUsuario);
                    amigo.setUsuario(Usuario);
                    amigo.setNombreUsuario(NombreUsuario);
                    amigo.setAvatar(Avatar);
                    amigo.setIdAmigo(IdAmigo);
                    amigo.setUsuarioAmigo(UsuarioAmigo);
                    amigo.setNombreAmigo(NombreAmigo);
                    amigo.setAvatarAmigo(AvatarAmigo);
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
                Log.d("PRUEBAS", "DESPUÉS CONSULTA AMIGOS: "+System.currentTimeMillis());
                cargarRecyclerLists();
            }else {
                Toast.makeText(getApplicationContext(), "Error al cargar los seguidores y seguidos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Tarea Asíncrona para llamar al WS de consulta en segundo plano para obtener un usuario dado una ID.
     * Devuelve una instancia de la clase UsuarioDTO con los datos del usuario consultado.
     */
    private class ConsultaUsuario extends AsyncTask<Integer,Void,UsuarioDTO> {

        private Context context;

        public ConsultaUsuario(Context context) {
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

    /**
     * Tarea Asíncrona para llamar al WS de consulta en segundo plano para obtener un usuario dado una ID.
     * Devuelve una instancia de la clase UsuarioDTO con los datos del usuario consultado.
     */
    private class SeguirUsuario extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public SeguirUsuario(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            AmigoDTO amigo = new AmigoDTO();
            amigo.setidUsuario(usuarioActivo.getIdUsuario());
            amigo.setamigo(params[0]);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("amigo");
            pi.setValue(amigo);
            pi.setType(amigo.getClass());

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_FOLLOW);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "AmigoDTO", amigo.getClass());

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_FOLLOW,envelope);
                SoapPrimitive resSoap = (SoapPrimitive) envelope.getResponse();

            } catch (Exception e) {
                result = false;
            }
            return result;
        }
        protected void onPostExecute(Boolean result) {
            if(!result){
                Toast.makeText(this.context, "No se ha podido seguir al usuario", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this.context, "Has empezado a seguir a este usuario", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Tarea Asíncrona para llamar al WS de consulta en segundo plano para obtener un usuario dado una ID.
     * Devuelve una instancia de la clase UsuarioDTO con los datos del usuario consultado.
     */
    private class DejarSeguirUsuario extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public DejarSeguirUsuario(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UNFOLLOW);
            request.addProperty("idUsuario", usuarioActivo.getIdUsuario());
            request.addProperty("amigo", usuarioPerfilID);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_UNFOLLOW,envelope);
                SoapPrimitive resSoap = (SoapPrimitive)envelope.getResponse();

            } catch (XmlPullParserException | IOException e) {
                result = false;
            }
            return result;
        }
        protected void onPostExecute(Boolean result) {
            if(!result){
                Toast.makeText(this.context, "No se ha podido dejar de seguir al usuario", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this.context, "Has dejado de seguir a este usuario", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatosPerfil();
        Handler uiHandlerRecyclerViews = new Handler(this.getMainLooper());
        uiHandlerRecyclerViews.post(new Runnable() {
            @Override
            public void run() {
                ConsultaAmigos selectAmigos = new ConsultaAmigos(mContext);
                selectAmigos.execute();
            }
        });
    }

}
