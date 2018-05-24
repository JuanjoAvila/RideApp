package com.example.juanjo.rideapp.Usuario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.juanjo.rideapp.Login;
import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.UsuarioDTO;

public class Perfil extends AppCompatActivity {
    UsuarioDTO usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

    }

}
