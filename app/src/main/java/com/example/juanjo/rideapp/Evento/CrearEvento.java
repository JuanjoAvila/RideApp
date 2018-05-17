package com.example.juanjo.rideapp.Evento;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.RecyclerViewAdapter;

import java.util.ArrayList;

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
