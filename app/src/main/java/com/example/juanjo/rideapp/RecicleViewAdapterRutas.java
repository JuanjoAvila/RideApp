package com.example.juanjo.rideapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class RecicleViewAdapterRutas extends RecyclerView.Adapter<RecicleViewAdapterRutas.RutasViewHolder> {
    private List<RutaRecicleView> items;

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

        private RutasViewHolder(View v) {
            super(v);
            fotoUsuario = v.findViewById(R.id.fotoUsuario);
            nombreRuta = v.findViewById(R.id.nombreRuta);
            rutaMapa = v.findViewById(R.id.rutaMapa);
            fechaPublicacion = v.findViewById(R.id.fechaPublicacion);
            puntuacionEstrellas = v.findViewById(R.id.puntuacionEstrellas);
            likesCarita = v.findViewById(R.id.likesCarita);
            likes = v.findViewById(R.id.likes);
            dislikesCarita = v.findViewById(R.id.dislikesCarita);
            dislikes = v.findViewById(R.id.dislikes);

        }
    }

    RecicleViewAdapterRutas(List<RutaRecicleView> items) {
        this.items = items;
    }

    @Override
    public int getItemCount () {
        return items.size();
    }

    @NonNull
    @Override
    public RutasViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rutas_card, viewGroup, false);
        return new RutasViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull RutasViewHolder viewHolder, int i){
        viewHolder.fotoUsuario.setImageResource(items.get(i).getFotoUsuario());
        viewHolder.nombreRuta.setText(items.get(i).getNombreRuta());
        viewHolder.rutaMapa.setImageResource(items.get(i).getRutaMapa());
        viewHolder.fechaPublicacion.setText(String.valueOf(items.get(i).getFechaPublicacion()));
        viewHolder.puntuacionEstrellas.setRating(items.get(i).getPuntuacionEstrellas());
        viewHolder.likesCarita.setImageResource(items.get(i).getLikesCarita());
        viewHolder.likes.setText(String.valueOf(items.get(i).getLikes()));
        viewHolder.dislikesCarita.setImageResource(items.get(i).getDislikesCarita());
        viewHolder.dislikes.setText(String.valueOf(items.get(i).getDislikes()));
    }
}

