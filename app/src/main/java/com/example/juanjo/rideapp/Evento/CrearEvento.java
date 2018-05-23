package com.example.juanjo.rideapp.Evento;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.juanjo.rideapp.R;

import java.util.ArrayList;

/**
 * Clase encargada de gestionar la ventana de creacioón de un Evento
 */
public class CrearEvento extends AppCompatActivity {

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);
    }
}
