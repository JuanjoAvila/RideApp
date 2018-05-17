package com.example.juanjo.rideapp.Evento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.juanjo.rideapp.R;

public class Eventos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
    }

    public void crearEvento(View view){
        Intent i = new Intent(this, CrearEvento.class );
        startActivity(i);
    }
}
