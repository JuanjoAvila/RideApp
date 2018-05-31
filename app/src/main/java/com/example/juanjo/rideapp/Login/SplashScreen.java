package com.example.juanjo.rideapp.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.juanjo.rideapp.R;

/**
 * Actividad dedicada a mostrar al usuario una pantalla de carga al abrir la aplicación.
 * @author  Juanjo Avila Chavero
 * @version 1.0
 * Creada en mayo del 2018
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Cargamos el tema creado en el xml styles.
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        // Tenemos una plantilla llamada splash.xml donde mostraremos la información que queramos (logotipo, etc.)
        setContentView(R.layout.activity_splash_screen);

        //Al finalizar el tiempo de carga, seguidamente se iniciará la ventana de Login.
        Intent intent = new Intent(SplashScreen.this, Login.class);
        startActivity(intent);
        finish();
    }

}