package com.example.juanjo.rideapp.Amigos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jesus on 11/04/18.
 */

public class AmigosAdapter extends BaseAdapter {

    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "borrarAmigo";
    public static final String SOAP_ACTION = "http://tempuri.org/borrarAmigo";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String SOAP_ACTION2 = "http://tempuri.org/nuevoAmigo";
    public static final String METHOD_NAME2 = "nuevoAmigo";

    protected Context context;
    protected Activity activity;
    protected ArrayList<Integer> idAmigos;
    protected ArrayList<Usuario_adapter> usuarios;

    public AmigosAdapter(Activity activity, ArrayList<Usuario_adapter> usuarios, ArrayList<Integer> idAmigos, Context context) {
        this.activity = activity;
        this.usuarios = usuarios;
        this.idAmigos = idAmigos;
        this.context = context;
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

        if(!usuarios.get(position).getAvatar().equals(null) && !usuarios.get(position).getAvatar().equals("") && !usuarios.get(position).getAvatar().startsWith("http")){
            byte[] decodedString = Base64.decode(usuarios.get(position).getAvatar(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            avatar.setImageBitmap(bitmap);
        }else{
            avatar.setImageDrawable(activity.getResources().getDrawable(R.drawable.user_default));
        }

        TextView nombreUsuario = (TextView) vi.findViewById(R.id.tv_amigos_nombreUsuario);
        nombreUsuario.setText(usuarios.get(position).getNombre());

        TextView apellidosUsuario = (TextView) vi.findViewById(R.id.tv_amigos_apellidos);
        apellidosUsuario.setText(usuarios.get(position).getApellidos());

        final ToggleButton tb_amigos = vi.findViewById(R.id.tb_amigos);
        if(idAmigos.contains(usuarios.get(position).getIdUsuario())){
            tb_amigos.setChecked(true);
        }else{
            tb_amigos.setChecked(false);
        }

        tb_amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tb_amigos.isChecked()){
                    new insertarAmigo(context).execute(Login.getUsuari().getIdUsuario(), usuarios.get(position).getIdUsuario());
                }else{
                    new borrarAmigo(context).execute(usuarios.get(position).getIdUsuario());
                }
            }
        });

        return vi;
    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class insertarAmigo extends AsyncTask<Integer,Void,Integer> {

        private Context context;

        public insertarAmigo(Context context) {
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

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME2);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "AmigoDTO", amigo.getClass());

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION2,envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                result = Integer.parseInt(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Integer result) {
            if(result.equals(new Integer(0))){
                Toast.makeText(this.context, "No se ha insertado", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this.context, "Se ha creado correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class borrarAmigo extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public borrarAmigo(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            request.addProperty("amigo", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION,envelope);
                SoapPrimitive resSoap = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(context, "El usuario ha sido borrado", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "No ha podido borrarse el usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
