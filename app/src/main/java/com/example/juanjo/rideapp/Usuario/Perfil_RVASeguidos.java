package com.example.juanjo.rideapp.Usuario;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.*;

import com.example.juanjo.rideapp.FTP.FTPManager;
import com.example.juanjo.rideapp.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Clase encargada de generar el adaptador del RecyclerView.
 */

public class Perfil_RVASeguidos extends RecyclerView.Adapter<Perfil_RVASeguidos.ViewHolder> {

    private static final String TAG = "Perfil_RVASeguidos";

    //vars
    private ArrayList<String> mNombresUsuarios;
    private ArrayList<String> mNombres;
    private ArrayList<String> mImagenesUsuarios;
    private ArrayList<Integer> midsUsuarios;
    private Activity mContext;
    private FTPManager ftpManager;
    private Bitmap bitmap = null;

    /**
     * Constructor del adaptador para generar el RecyclerView.
     * @param context Contexto de la actividad donde se va a ubicar el RecyclerView.
     * @param names Nombres de usuario de los seguidores o seguidos.
     * @param imageUrls WS_URL o nombre de los avatares de los seguidores o seguidos.
     * @param idsUsers ID del seguidor o seguido, necesario para poder luego acceder a su perfil puslando en su avatar.
     * @param nombres Nombres de la cuenta de los usuarios, necesario para sustituir los Nombres de usuario de Google por sus nombres de la cuenta.
     */
    public Perfil_RVASeguidos(Activity context, ArrayList<String> names, ArrayList<String> imageUrls, ArrayList<Integer> idsUsers, ArrayList<String> nombres) {
        mNombresUsuarios = names;
        mImagenesUsuarios = imageUrls;
        midsUsuarios = idsUsers;
        mNombres = nombres;
        mContext = context;
        ftpManager = new FTPManager(mContext);
    }

    /**
     * Descarga una imagen a través del FTP o por HTTP según si el usuario es propio de la aplicación,
     * entonces tiene su avatar en el FTP o según si el usuario se registró con cuenta de Google,
     * que entonces cogerá la foto de avatar de la WS_URL de Google.
     * @param imgPosition Posición en la que se encuentra el String con el nombre o el WS_URL de la imágen en el ArrayList del adaptador.
     * @param usuarioNombre Nombre del usuario para poder identificar si es usuario de Google o propio de la aplicación.
     * @return Devuelve un Bitmap con el avatar del usuario.
     */
    public Bitmap descargarImagen(int imgPosition, String usuarioNombre){
        Bitmap avatarBitmap = null;
        try {
            if(usuarioNombre.endsWith("google")){
                avatarBitmap = ftpManager.HTTPCargarImagen(mImagenesUsuarios.get(imgPosition));
            }
            else {
                byte[] decodeValue = Base64.decode(mImagenesUsuarios.get(imgPosition), Base64.DEFAULT);
                avatarBitmap = BitmapFactory.decodeByteArray(decodeValue, 0, decodeValue.length);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return avatarBitmap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.perfil_seguidores_recycledview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final int usuarioPosition = position;
        Log.d(TAG, "onBindViewHolder: called.");
        /*
         *Comprobamos si la imagen del usuario es Null o no tiene ningún avatar asignado.
         *En caso de no tener ningun avatar asignado se le asigna uno por defecto integrado en la aplicación.
         */
        if(mImagenesUsuarios.get(position)!=null && mImagenesUsuarios.get(position)!="") {
            bitmap = descargarImagen(usuarioPosition, mNombresUsuarios.get(usuarioPosition));
            // Para los casos en los que la descarga falle, se le asignará el avatar por defecto.
            if(bitmap!=null)holder.image.setImageBitmap(bitmap);
            else holder.image.setImageResource(R.drawable.user_default);
        }
        else{
            holder.image.setImageResource(R.drawable.user_default);
        }
        // En los casos que la cuenta es de Google, se mostrará su nombre de la cuenta.
        if(mNombresUsuarios.get(usuarioPosition).endsWith("google")){
            holder.name.setText(mNombres.get(usuarioPosition));
        }
        // Para los usuarios de la aplicación se le mostrará su nombre de usuario.
        else {
            holder.name.setText(mNombresUsuarios.get(position));
        }
        // Hacemos que la imagen se quede a la espera de un evento onClick para abrir el perfil del usuario clickado.
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.finish();
                Intent i = new Intent(mContext, Perfil.class );
                i.putExtra("usuario", midsUsuarios.get(usuarioPosition));
                mContext.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mImagenesUsuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }
}
