package com.example.juanjo.rideapp.VentanaPrincipal;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.juanjo.rideapp.R;

public class Acerca_de extends AppCompatActivity {
    private Button correo_juanjo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        View descorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        descorView.setSystemUiVisibility(uiOptions);
        correo_juanjo = findViewById(R.id.correo_juanjo);
        correo_juanjo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "rideapp@outlook.es"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "RideApp");
                    intent.putExtra(Intent.EXTRA_TEXT, "Introduce lo que nos quieras comentar...");
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(),"No hay mail encontrado\nPorfavor instale el gmail o alguna aplicacion de correos.",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}
