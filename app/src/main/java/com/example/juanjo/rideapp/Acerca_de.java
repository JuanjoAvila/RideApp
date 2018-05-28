package com.example.juanjo.rideapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Acerca_de extends AppCompatActivity {
    private Button correo_juanjo;
    private Button correo_richard;
    private Button correo_jesus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        View descorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        descorView.setSystemUiVisibility(uiOptions);
        correo_jesus = findViewById(R.id.correo_jesus);
        correo_juanjo = findViewById(R.id.correo_juanjo);
        correo_richard = findViewById(R.id.correo_richard);
        correo_juanjo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "juanjo.avila.chavero@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "RideApp");
                    intent.putExtra(Intent.EXTRA_TEXT, "Introduce lo que quieras comentar...");
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(),"No hay mail encontrado\nPorfavor instale el gmail o alguna aplicacion de correos.",Toast.LENGTH_LONG).show();
                }
            }
        });
        correo_jesus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "floressegobiajesus@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "RideApp");
                    intent.putExtra(Intent.EXTRA_TEXT, "Introduce lo que quieras comentar...");
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(),"No hay mail encontrado\nPorfavor instale el gmail o alguna aplicacion de correos.",Toast.LENGTH_LONG).show();
                }
            }
        });
        correo_richard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "richardal.94@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "RideApp");
                    intent.putExtra(Intent.EXTRA_TEXT, "Introduce lo que quieras comentar...");
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(),"No hay mail encontrado\nPorfavor instale el gmail o alguna aplicacion de correos.",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
