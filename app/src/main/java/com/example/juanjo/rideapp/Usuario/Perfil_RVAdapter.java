package com.example.juanjo.rideapp.Usuario;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
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
 * Created by User on 2/12/2018.
 */

public class Perfil_RVAdapter extends RecyclerView.Adapter<Perfil_RVAdapter.ViewHolder> {

    private static final String TAG = "Perfil_RVAdapter";

    //vars
    private ArrayList<String> mNombresUsuarios = new ArrayList<>();
    private ArrayList<String> mImagenesUsuarios = new ArrayList<>();
    private ArrayList<Integer> midsUsuarios = new ArrayList<>();
    private Context mContext;
    private FTPManager ftpManager;
    private Bitmap bitmap = null;

    public Perfil_RVAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls, ArrayList<Integer> idsUsers) {
        mNombresUsuarios = names;
        mImagenesUsuarios = imageUrls;
        midsUsuarios = idsUsers;
        mContext = context;
        ftpManager = new FTPManager(mContext);
    }

    public Bitmap descargarImagen(int imgPosition, String usuarioNombre){
        Bitmap avatarBitmap = null;
        try {
            if(usuarioNombre.endsWith("google")){
                avatarBitmap = ftpManager.HTTPCargarImagen(mImagenesUsuarios.get(imgPosition));
            }
            else {
                avatarBitmap = ftpManager.FTPCargarImagen(mImagenesUsuarios.get(imgPosition));
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

        if(mImagenesUsuarios.get(position)!=null && mImagenesUsuarios.get(position)!="") {
            Handler uiHandler = new Handler(mContext.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    bitmap = descargarImagen(usuarioPosition, mNombresUsuarios.get(usuarioPosition));
                    if(bitmap!=null)holder.image.setImageBitmap(bitmap);
                    else holder.image.setImageResource(R.mipmap.perfil_defecto_avatar_usuario);
                    }
            });
        }
        else{
            holder.image.setImageResource(R.mipmap.perfil_defecto_avatar_usuario);
        }
        holder.name.setText(mNombresUsuarios.get(position));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
