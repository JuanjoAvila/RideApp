package com.example.juanjo.rideapp.Evento;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanjo.rideapp.DTO.EventosRutasDTO;
import com.example.juanjo.rideapp.DTO.ParticipanteDTO;
import com.example.juanjo.rideapp.DTO.ParticipantesEventoDTO;
import com.example.juanjo.rideapp.DTO.UsuarioDTO;
import com.example.juanjo.rideapp.FTP.FTPManager;
import com.example.juanjo.rideapp.Login.Login;
import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.Usuario.Perfil;
import com.example.juanjo.rideapp.Usuario.Perfil_RVASeguidos;

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
 * Clase encargada de gestionar la ventana de la vista de un evento
 */
public class Evento extends AppCompatActivity {
    public static final String URL = "http://rideapp2.somee.com/WebService.asmx";
    public static final String METHOD_NAME_EVENTOS = "Select_eventos_rutas_byIdEvento";
    public static final String SOAP_ACTION_EVENTOS = "http://tempuri.org/Select_eventos_rutas_byIdEvento";
    public static final String METHOD_NAME_USUARIO = "obtenerUsuario_byIdUsuario";
    public static final String SOAP_ACTION_USUARIO = "http://tempuri.org/obtenerUsuario_byIdUsuario";
    public static final String METHOD_NAME_ASISTIR = "nuevoParticipante";
    public static final String SOAP_ACTION_ASISTIR = "http://tempuri.org/nuevoParticipante";
    public static final String METHOD_NAME_NOASISTIR = "borrarParticipante";
    public static final String SOAP_ACTION_NOASISTIR = "http://tempuri.org/borrarParticipante";
    public static final String METHOD_NAME_PARTICIPANTES = "Select_participantes_usuarios";
    public static final String SOAP_ACTION_PARTICIPANTES = "http://tempuri.org/Select_participantes_usuarios";
    public static final String NAMESPACE = "http://tempuri.org/";

    private Activity mContext;
    private int eventoID;
    private EventosRutasDTO eventoDatos;
    private UsuarioDTO usuarioEvento;
    private TextView nombreRuta;
    private ImageView imagenRuta;
    private TextView fechaEvento;
    private TextView descripcion;
    private TextView participarTexto;
    private RecyclerView participantesRecycler;
    private ArrayList<ParticipantesEventoDTO> participantes;
    private ArrayList<Integer> participantesID;
    private ArrayList<String> participantesUsuario;
    private ArrayList<String> participantesNombre;
    private ArrayList<String> participantesAvatar;
    private ImageView avatar;
    private TextView nombreUsuario;
    private FTPManager ftpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        mContext = this;
        nombreRuta = findViewById(R.id.Evento_rutaTitulo);
        imagenRuta = findViewById(R.id.Evento_Imagen_Ruta);
        fechaEvento = findViewById(R.id.Evento_fechaValor);
        participantesRecycler = findViewById(R.id.Evento_recyclerView);
        descripcion = findViewById(R.id.Evento_Descripcion);
        avatar = findViewById(R.id.Evento_UsuarioAvatar);
        nombreUsuario = findViewById(R.id.Evento_UsuarioNombre);
        participarTexto = findViewById(R.id.Evento_textoAsistir);
        usuarioEvento = new UsuarioDTO();
        participantes = new ArrayList<>();
        participantesID = new ArrayList<>();
        participantesUsuario = new ArrayList<>();
        participantesNombre = new ArrayList<>();
        participantesAvatar = new ArrayList<>();
        cargarDatosEvento();
        eventoID = getIntent().getExtras().getInt("idEvento");
        eventoDatos = new EventosRutasDTO();
    }

    /**
     * Genera tres consulas en 3 hilos de forma paralela, uno para la carga de datos del evento, otro para la cara de datos del autor y el último para la carga de participantes del evento.
     */
    private void cargarDatosEvento(){
        Handler uiHandlerPerfil = new Handler(this.getMainLooper());
        uiHandlerPerfil.post(new Runnable() {
            @Override
            public void run() {
                ConsultaEvento consultaEvento = new ConsultaEvento(mContext);
                try {
                    eventoDatos = consultaEvento.execute(eventoID).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if(eventoDatos!=null){
                    nombreRuta.setText(eventoDatos.getTitulo());
                    fechaEvento.setText(eventoDatos.getFecha_evento());
                    descripcion.setText(eventoDatos.getComentario_Evento());
                    byte[] decodeValue = Base64.decode(eventoDatos.getFoto_ruta(), Base64.DEFAULT);
                    Bitmap bitmapAvatar = BitmapFactory.decodeByteArray(decodeValue, 0, decodeValue.length);
                    if(bitmapAvatar!=null) {
                        imagenRuta.setImageBitmap(bitmapAvatar);
                    }
                    else{
                        imagenRuta.setImageResource(R.drawable.imagenrutaprueba);
                    }
                }
            }
        });
        Handler uiHandlerUsuario = new Handler(this.getMainLooper());
        uiHandlerUsuario.post(new Runnable() {
            @Override
            public void run() {
                try {
                    usuarioEvento = new ConsultaUsuario(mContext).execute(eventoDatos.getIdUsuario()).get();
                    if(usuarioEvento.getAvatar()!=null && usuarioEvento.getAvatar().length()>0){
                        Bitmap bitmapAvatar = null;
                        try {
                            if(usuarioEvento.getUsuario().endsWith("google")){
                                nombreUsuario.setText(usuarioEvento.getNombre()+" "+usuarioEvento.getApellidos());
                                bitmapAvatar = ftpManager.HTTPCargarImagen(usuarioEvento.getAvatar());
                            }
                            else {
                                nombreUsuario.setText(usuarioEvento.getUsuario());
                                byte[] decodeValue = Base64.decode(usuarioEvento.getAvatar(), Base64.DEFAULT);
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
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        cargarParticipantes();
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, Perfil.class );
                i.putExtra("usuario", eventoDatos.getIdUsuario());
                mContext.startActivity(i);
            }
        });

    }
    public void cargarParticipantes(){
        Handler uiHandlerConsultaParticipantes = new Handler(this.getMainLooper());
        uiHandlerConsultaParticipantes.post(new Runnable() {
            @Override
            public void run() {
                ConsultaParticipantes consultaParticipantes = new ConsultaParticipantes(mContext);
                consultaParticipantes.execute(eventoID);
            }
        });
    }
    /**
     * En caso de no estar participando en el evento, genera un insert en la BD en la tabla participates, como participante con el ID del usuario activo en la aplicaición y ID del evento que se está visualizando en ese momento.
     * En caso de ya participar en el evento se genera un delete de la BD en la tabla de participantes, borrando el usuario actual para el evento actual.
     * @param view
     */
    public void asistirEvento(View view){
        if(!participantesID.contains(Login.getUsuari().getIdUsuario())) {
            ParticiparEvento participar = new ParticiparEvento(this);
            Boolean exito = false;
            try {
                exito = participar.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (exito) {
                participarTexto.setText("No asistiré");
            }
        }
        else{
            DejarParticiparEvento noParticipar = new DejarParticiparEvento(this);
            Boolean exito = false;
            try {
                exito = noParticipar.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (exito) {
                participarTexto.setText("Asistir");
            }
        }
        cargarParticipantes();
    }

    public void cargarRecyclerLists(){
        for (ParticipantesEventoDTO part : participantes) {
            participantesID.add(part.getIdUsuario());
            participantesUsuario.add(part.getUsuario());
            participantesNombre.add(part.getNombre());
            participantesAvatar.add(part.getAvatar());
        }
        Handler uiHandlerRecyclerViewparticipantes = new Handler(this.getMainLooper());
        uiHandlerRecyclerViewparticipantes.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                participantesRecycler.setLayoutManager(layoutManager);
                Perfil_RVASeguidos adapter = new Perfil_RVASeguidos(mContext, participantesUsuario, participantesAvatar, participantesID, participantesUsuario);
                participantesRecycler.setAdapter(adapter);
            }
        });
        if(participantesID.contains(Login.getUsuari().getIdUsuario())){
            participarTexto.setText("No asistiré");
        }
    }

    private class ConsultaEvento extends AsyncTask<Integer,Void,EventosRutasDTO> {

        private Context context;

        public ConsultaEvento(Context context) {
            this.context = context;
        }

        protected EventosRutasDTO doInBackground(Integer... params) {

            EventosRutasDTO result = null;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_EVENTOS);
            request.addProperty("idEvento", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_EVENTOS,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                result = new EventosRutasDTO(Integer.valueOf(resSoap.getPropertyAsString(0)),
                        Integer.valueOf(resSoap.getPropertyAsString(1)), Integer.valueOf(resSoap.getPropertyAsString(2)),
                        resSoap.getPropertyAsString(3), resSoap.getPropertyAsString(4),
                        Integer.valueOf(resSoap.getPropertyAsString(5)), Integer.valueOf(resSoap.getPropertyAsString(6)),
                        resSoap.getPropertyAsString(7), resSoap.getPropertyAsString(8), resSoap.getPropertyAsString(9),
                        Integer.valueOf(resSoap.getPropertyAsString(10)), Integer.valueOf(resSoap.getPropertyAsString(11)),
                        Integer.valueOf(resSoap.getPropertyAsString(12)), resSoap.getPropertyAsString(13), resSoap.getPropertyAsString(14));

            } catch (Exception e) {
                return result;
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
            }else{
                Toast.makeText(getApplicationContext(), "Evento incorrecto", Toast.LENGTH_SHORT).show();
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

    private class ParticiparEvento extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public ParticiparEvento(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            ParticipanteDTO participante = new ParticipanteDTO();
            participante.setEvento(eventoDatos.getIdEvento());
            participante.setIdUsuario(Login.getUsuari().getIdUsuario());

            PropertyInfo pi = new PropertyInfo();
            pi.setName("participante");
            pi.setValue(participante);
            pi.setType(participante.getClass());

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_ASISTIR);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "ParticipanteDTO", participante.getClass());

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_ASISTIR,envelope);
                SoapPrimitive resSoap = (SoapPrimitive) envelope.getResponse();

            } catch (Exception e) {
                result = false;
            }
            return result;
        }
        protected void onPostExecute(Boolean result) {
            if(!result){
                Toast.makeText(this.context, "No se ha podido unir al evento", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this.context, "¡Te has unido al evento!", Toast.LENGTH_LONG).show();
            }
        }
    }
    private class DejarParticiparEvento extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public DejarParticiparEvento(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_NOASISTIR);
            request.addProperty("participante", Login.getUsuari().getIdUsuario());
            request.addProperty("evento", eventoID);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_NOASISTIR,envelope);
                SoapPrimitive resSoap = (SoapPrimitive)envelope.getResponse();

            } catch (XmlPullParserException | IOException e) {
                result = false;
            }
            return result;
        }
        protected void onPostExecute(Boolean result) {
            if(!result){
                Toast.makeText(this.context, "No se ha podido cancelar asistencia.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this.context, "Ya no participas en el evento.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ConsultaParticipantes extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public ConsultaParticipantes(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;
            participantes.clear();
            participantesID.clear();
            participantesUsuario.clear();
            participantesNombre.clear();
            participantesAvatar.clear();
            ParticipantesEventoDTO participante;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_PARTICIPANTES);
            request.addProperty("evento", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_PARTICIPANTES,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                for (int i = 0; i < resSoap.getPropertyCount(); i++) {
                    SoapObject iu = (SoapObject)resSoap.getProperty(i);
                    participante = new ParticipantesEventoDTO(
                            Integer.valueOf(iu.getPropertyAsString(1)), Integer.valueOf(iu.getPropertyAsString(2)),
                            iu.getPropertyAsString(3), iu.getPropertyAsString(4), iu.getPropertyAsString(5),
                            iu.getPropertyAsString(6), iu.getPropertyAsString(7), iu.getPropertyAsString(8));

                    participantes.add(participante);
                }

            } catch (Exception e) {
                return result;
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                cargarRecyclerLists();
            }else {
                Toast.makeText(getApplicationContext(), "Error al cargar los participantes.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}