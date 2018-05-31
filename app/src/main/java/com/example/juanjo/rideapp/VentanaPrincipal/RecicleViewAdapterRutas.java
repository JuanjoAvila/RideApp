package com.example.juanjo.rideapp.VentanaPrincipal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.juanjo.rideapp.DTO.Ruta_infoDTO;
import com.example.juanjo.rideapp.R;

import java.util.List;

public class RecicleViewAdapterRutas extends RecyclerView.Adapter<RecicleViewAdapterRutas.RutasViewHolder> {
    private List<Ruta_infoDTO> items;

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

            puntuacionEstrellas.setIsIndicator(true);
        }
    }

    public RecicleViewAdapterRutas(List<Ruta_infoDTO> items) {
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
        byte[] decodeValue = Base64.decode(items.get(i).getFoto_usuario(), Base64.DEFAULT);
        Bitmap bitmapAvatar = BitmapFactory.decodeByteArray(decodeValue, 0, decodeValue.length);
        viewHolder.fotoUsuario.setImageBitmap(bitmapAvatar);
        viewHolder.nombreRuta.setText(items.get(i).getTitulo());
        byte[] decodeValuemapa = Base64.decode(items.get(i).getFoto_ruta(), Base64.DEFAULT);
        Bitmap bitmapmapa = BitmapFactory.decodeByteArray(decodeValuemapa, 0, decodeValuemapa.length);
        viewHolder.rutaMapa.setImageBitmap(bitmapmapa);
        viewHolder.fechaPublicacion.setText(items.get(i).getFecha_ruta());
        viewHolder.puntuacionEstrellas.setRating(items.get(i).getDificultad());
        viewHolder.likesCarita.setImageResource(R.drawable.happy);
        viewHolder.likes.setText(String.valueOf(items.get(i).getLikes()));
        viewHolder.dislikesCarita.setImageResource(R.drawable.sad);
        viewHolder.dislikes.setText(String.valueOf(items.get(i).getDislikes()));
    }

    public Ruta_infoDTO getItem(int position) {
        return items.get(position);
    }
}

