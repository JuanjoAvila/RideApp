package com.example.juanjo.rideapp.Usuario;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanjo.rideapp.DTO.RutaDTO;
import com.example.juanjo.rideapp.DTO.Ruta_infoDTO;
import com.example.juanjo.rideapp.Login.Login;
import com.example.juanjo.rideapp.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Perfil_RVARutas extends RecyclerView.Adapter<Perfil_RVARutas.RutasViewHolder> {
    private List<Ruta_infoDTO> items;
    private Integer idPerfil;
    public static final String URL = "http://rideapp2.somee.com/WebService.asmx";
    public static final String METHOD_NAME_BORRARRUTA = "borrarRuta";
    public static final String SOAP_ACTION_BORRARRUTA = "http://tempuri.org/borrarRuta";
    public static final String METHOD_NAME_GUARDARRUTA = "nuevaRuta";
    public static final String SOAP_ACTION_GUARDARRUTA = "http://tempuri.org/nuevaRuta";
    public static final String METHOD_NAME_OBTENERRUTA = "obtenerRuta";
    public static final String SOAP_ACTION_OBTENERRUTA = "http://tempuri.org/obtenerRuta";
    public static final String NAMESPACE = "http://tempuri.org/";
    public Context mContext;
    public RutaDTO rutaSeleccionada;

    public static class RutasViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView fotoUsuario;
        public TextView nombreRuta;
        public ImageView rutaMapa;
        public TextView fechaPublicacion;
        public RatingBar puntuacionEstrellas;
        public ImageView likesCarita;
        public TextView likes;
        public ImageView dislikesCarita;
        public TextView dislikes;
        public ImageView borrar;
        public ImageView descargar;

        private RutasViewHolder(View v) {
            super(v);
            fotoUsuario = v.findViewById(R.id.Perfil_Rutas_fotoUsuario);
            nombreRuta = v.findViewById(R.id.Perfil_Rutas_nombreRuta);
            rutaMapa = v.findViewById(R.id.Perfil_Rutas_rutaMapa);
            fechaPublicacion = v.findViewById(R.id.Perfil_Rutas_fechaPublicacion);
            likesCarita = v.findViewById(R.id.Perfil_Rutas_likesCarita);
            likes = v.findViewById(R.id.Perfil_Rutas_likes);
            dislikesCarita = v.findViewById(R.id.Perfil_Rutas_dislikesCarita);
            dislikes = v.findViewById(R.id.Perfil_Rutas_dislikes);
            borrar = v.findViewById(R.id.Perfil_Rutas_Borrar);
            descargar = v.findViewById(R.id.Perfil_Rutas_Descargar);
        }
    }

    public Perfil_RVARutas(List<Ruta_infoDTO> items, Integer idPerfil, Context context) {
        this.idPerfil = idPerfil;
        this.items = items;
        this.mContext = context;
    }

    @Override
    public int getItemCount () {
        return items.size();
    }

    @NonNull
    @Override
    public RutasViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.perfil_rutas, viewGroup, false);
        return new RutasViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull final RutasViewHolder viewHolder, int i){
        final int POSICION = i;
        byte[] bytesImagenRuta = Base64.decode(items.get(i).getFoto_ruta(), Base64.DEFAULT);
        Bitmap bitmapFoto = BitmapFactory.decodeByteArray(bytesImagenRuta, 0, bytesImagenRuta.length);
        if (bitmapFoto!=null)viewHolder.rutaMapa.setImageBitmap(bitmapFoto);
        byte[] bytesImagenUsuario = Base64.decode(items.get(i).getFoto_usuario(), Base64.DEFAULT);
        Bitmap bitmapFotoUsuario = BitmapFactory.decodeByteArray(bytesImagenUsuario, 0, bytesImagenUsuario.length);
        if (bitmapFotoUsuario!=null)viewHolder.rutaMapa.setImageBitmap(bitmapFoto);viewHolder.fotoUsuario.setImageBitmap(bitmapFotoUsuario);
        viewHolder.nombreRuta.setText(items.get(i).getTitulo());
        viewHolder.fechaPublicacion.setText(items.get(i).getFecha_ruta());
        viewHolder.likesCarita.setImageResource(R.drawable.caritasonriente);
        viewHolder.likes.setText(String.valueOf(items.get(i).getLikes()));
        viewHolder.dislikesCarita.setImageResource(R.drawable.caritatriste);
        viewHolder.dislikes.setText(String.valueOf(items.get(i).getDislikes()));
        if(idPerfil.equals(Login.getUsuari().getIdUsuario())){
            viewHolder.borrar.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.descargar.setVisibility(View.VISIBLE);
        }
        viewHolder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsultaRuta consultaRuta = new ConsultaRuta(mContext);
                try {
                    rutaSeleccionada = consultaRuta.execute(items.get(POSICION).getIdRuta()).get();
                    BorrarRuta borrarRuta = new BorrarRuta(mContext);
                    borrarRuta.execute(rutaSeleccionada.getIdRuta());
                    viewHolder.borrar.setVisibility(View.INVISIBLE);
                    viewHolder.descargar.setVisibility(View.VISIBLE);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        viewHolder.descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsultaRuta consultaRuta = new ConsultaRuta(mContext);
                try {
                    rutaSeleccionada = consultaRuta.execute(items.get(POSICION).getIdRuta()).get();
                    InsertarRuta insertarRuta = new InsertarRuta(mContext);
                    insertarRuta.execute(rutaSeleccionada);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Ruta_infoDTO getItem(int position) {
        return items.get(position);
    }

    private class InsertarRuta extends AsyncTask<RutaDTO,Void,Boolean> {

        private Context context;

        public InsertarRuta(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(RutaDTO... rutaDTOS) {
            Boolean result = true;
            RutaDTO ruta = new RutaDTO();
            ruta = rutaDTOS[0];
            ruta.setIdUsuario(Login.getUsuari().getIdUsuario());
            PropertyInfo pi = new PropertyInfo();
            pi.setName("ruta");
            pi.setValue(ruta);
            pi.setType(ruta.getClass());

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME_GUARDARRUTA);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "RutaDTO", ruta.getClass());

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_GUARDARRUTA,envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                result = false;
            }

            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(!result){
                Toast.makeText(this.context, "No se ha podido guardar la ruta.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this.context, "Has añadido la ruta a tus rutas.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class BorrarRuta extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public BorrarRuta(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Integer... rutaDTOS) {

            Boolean result = true;
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME_BORRARRUTA);
            request.addProperty("ruta", rutaDTOS[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_BORRARRUTA,envelope);
                SoapPrimitive resSoap = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }

            return result;
        }


        protected void onPostExecute(Boolean result) {
            if(!result){
                Toast.makeText(this.context, "No se ha podido borrar la ruta", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this.context, "Has borrada la ruta.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ConsultaRuta extends AsyncTask<Integer,Void,RutaDTO> {

        private Context context;

        public ConsultaRuta(Context context) {
            this.context = context;
        }

        protected RutaDTO doInBackground(Integer... params) {

            RutaDTO result = null;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_OBTENERRUTA);
            request.addProperty("ruta", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION_OBTENERRUTA,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                result = new RutaDTO(Integer.valueOf(resSoap.getPropertyAsString(0)), Integer.valueOf(resSoap.getPropertyAsString(1)), resSoap.getPropertyAsString(2), resSoap.getPropertyAsString(3),
                        resSoap.getPropertyAsString(4), Integer.valueOf(resSoap.getPropertyAsString(5)), Integer.valueOf(resSoap.getPropertyAsString(6)),
                        Integer.valueOf(resSoap.getPropertyAsString(7)),resSoap.getPropertyAsString(8));

            } catch (Exception e) {
                return result;
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

