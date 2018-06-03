package com.example.juanjo.rideapp.Amigos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.juanjo.rideapp.DTO.AmigoDTO;
import com.example.juanjo.rideapp.DTO.Usuario_adapter;
import com.example.juanjo.rideapp.Login.Login;
import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.Usuario.Perfil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author RideApp
 * @version Final
 * Adaptador para el ListView utilizado en Amigos
 */
public class AmigosAdapter extends BaseAdapter {

    public static final String WS_URL = "http://rideapp2.somee.com/WebService.asmx";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String NUEVO_AMIGO_ACTION = "http://tempuri.org/nuevoAmigo";
    public static final String NUEVO_AMIGO_METHOD = "nuevoAmigo";

    protected Context context;
    protected Activity activity;
    protected ArrayList<Integer> idAmigos;
    protected ArrayList<Usuario_adapter> usuarios;
    //Lista duplicada para utilizarla en el filtro
    protected ArrayList<Usuario_adapter> usuarios_for_filter;
    //Necesario para ejecutar los dialogos en la actividad principal de Amigos
    protected Amigos_main main;

    public AmigosAdapter(Amigos_main main, Activity activity, ArrayList<Usuario_adapter> usuarios_for_filter, ArrayList<Usuario_adapter> usuarios, ArrayList<Integer> idAmigos, Context context) {
        this.activity = activity;
        this.usuarios = usuarios;
        this.idAmigos = idAmigos;
        this.context = context;
        this.usuarios_for_filter = usuarios_for_filter;
        this.main = main;
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return usuarios.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.amigos_listview, null);
        }

        CircleImageView avatar = vi.findViewById(R.id.fotoUsuario);

        /*
        Se carga la foto de perfil
         */
        if(!usuarios.get(position).getAvatar().equals(null) && !usuarios.get(position).getAvatar().equals("") && !usuarios.get(position).getAvatar().startsWith("http")){
            byte[] decodedString = Base64.decode(usuarios.get(position).getAvatar(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            avatar.setImageBitmap(bitmap);
        }else{
            avatar.setImageDrawable(activity.getResources().getDrawable(R.drawable.user_default));
        }
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                Intent i = new Intent(activity, Perfil.class );
                i.putExtra("usuario", usuarios.get(position).getIdUsuario());
                activity.startActivity(i);

            }
        });
        /*
        Se cargan el nombre y apellidos
         */
        TextView nombreUsuario = (TextView) vi.findViewById(R.id.tv_amigos_nombreUsuario);
        nombreUsuario.setText(usuarios.get(position).getNombre());

        TextView apellidosUsuario = (TextView) vi.findViewById(R.id.tv_amigos_apellidos);
        apellidosUsuario.setText(usuarios.get(position).getApellidos());

        /*
        Se comprueba que amigos tiene ya agregados para posicionar el ToogleButton de una manera
        o otra
         */
        final ToggleButton tb_amigos = vi.findViewById(R.id.tb_amigos);
        if(idAmigos.contains(usuarios.get(position).getIdUsuario())){
            tb_amigos.setChecked(true);
        }else{
            tb_amigos.setChecked(false);
        }

        /*
        Se inserta una nueva amistad, o se borra según la posición del ToogleButton
         */
        tb_amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tb_amigos.isChecked()){
                    new amigos_insertar(context).execute(Login.getUsuari().getIdUsuario(), usuarios.get(position).getIdUsuario());
                }else{
                    main.usuario_actual = Login.getUsuari().getIdUsuario();
                    main.usuario_a_borrar = usuarios.get(position).getIdUsuario();
                    main.mostrar_no_seguir_dialog();
                }
            }
        });

        return vi;
    }

    /**
     * Método utilizado para filtar resultados por nombres y apellidos
     * @param charText El texto introducido en la búsqueda
     */
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        usuarios.clear();
        if (charText.length() == 0) {
            usuarios.addAll(usuarios_for_filter);
        } else {
            for (Usuario_adapter usuario : usuarios_for_filter) {
                String nombre_apellidos = usuario.getNombre() + " " + usuario.getApellidos();
                if (nombre_apellidos.toLowerCase(Locale.getDefault()).contains(charText)) {
                    usuarios.add(usuario);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Tarea asincrona utilizada para la inserción de una nueva amistad
     */
    private class amigos_insertar extends AsyncTask<Integer,Void,Integer> {

        private Context context;

        public amigos_insertar(Context context) {
            this.context = context;
        }

        protected Integer doInBackground(Integer... params) {

            Integer result = 0;

            AmigoDTO amigo = new AmigoDTO();
            amigo.setamigo(params[1]);
            amigo.setidUsuario(params[0]);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("amigo");
            pi.setValue(amigo);
            pi.setType(amigo.getClass());

            SoapObject request = new SoapObject(NAMESPACE, NUEVO_AMIGO_METHOD);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "AmigoDTO", amigo.getClass());

            HttpTransportSE transporte = new HttpTransportSE(WS_URL);

            try {
                transporte.call(NUEVO_AMIGO_ACTION,envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                result = Integer.parseInt(response.toString());
                idAmigos.add(params[1]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Integer result) {
            if(!result.equals(new Integer(0))){
                /*
                Si el resultado es satisfactorio, se muestra que se ha agregado correctamente
                 */
                main.mostrar_seguir_dialog();
            }
        }
    }

}
