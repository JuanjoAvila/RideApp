package com.example.juanjo.rideapp.Evento;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.juanjo.rideapp.DTO.EventosRutasDTO;
import com.example.juanjo.rideapp.DTO.Ruta_infoDTO;
import com.example.juanjo.rideapp.FTP.FTPManager;
import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.Usuario.Perfil;
import com.example.juanjo.rideapp.Usuario.Perfil_RVAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVEventos extends RecyclerView.Adapter<RVEventos.EventosViewHolder> {
    private List<EventosRutasDTO> items;

    public static class EventosViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagenRuta;
        private TextView nombreRuta;
        private TextView fechaEvento;

        private EventosViewHolder(View v) {
            super(v);
            imagenRuta = v.findViewById(R.id.Eventos_rutaMapa);
            nombreRuta = v.findViewById(R.id.Eventos_nombreRuta);
            fechaEvento = v.findViewById(R.id.Eventos_fechaPublicacion);
        }
    }

    public RVEventos(List<EventosRutasDTO> items) {
        this.items = items;
    }

    public EventosRutasDTO getEvento(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount () {
        return items.size();
    }

    @NonNull
    @Override
    public EventosViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.eventos_recycledview, viewGroup, false);
        return new EventosViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull EventosViewHolder viewHolder, int i){
        viewHolder.nombreRuta.setText(items.get(i).getTitulo());
        viewHolder.fechaEvento.setText(items.get(i).getFecha_evento());
        byte[] decodeValue = Base64.decode(items.get(i).getFoto_ruta(), Base64.DEFAULT);
        Bitmap bitmapAvatar = BitmapFactory.decodeByteArray(decodeValue, 0, decodeValue.length);
        if(bitmapAvatar!=null) {
            viewHolder.imagenRuta.setImageBitmap(bitmapAvatar);
        }
        else{
            viewHolder.imagenRuta.setImageResource(R.drawable.imagenrutaprueba);
        }
    }

}

