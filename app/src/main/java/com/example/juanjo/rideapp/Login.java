package com.example.juanjo.rideapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Al apretar para logear con el usuario o contraseña que no se mueva el fondo
        getWindow().setBackgroundDrawableResource(R.drawable.fondo);
        //Sirve para que no salga al inciar el login , el pop up para escribir .
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    //Sirve para cerrar la ventana anterior es decir esta en el login , pasa al main activity y cierra el login . Si vuelve para atras saldra de la aplicacion
    public void iniciar(View view){
        startActivity(new Intent(getBaseContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }
}
