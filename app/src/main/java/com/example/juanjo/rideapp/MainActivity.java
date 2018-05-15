package com.example.juanjo.rideapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean esPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Asigna la barra de abajo como la predeterminada
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Adquiere la ventana principal para al poder cambiar de ventana con el menu si estas en la misma no te añada una nueva ventana
        esPrincipal = true;


        //Crea el boton del menu para poderse abrir y cerrar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Al apretar adquiere el valor del boton y te muestra el menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    //Este metodo te crea el efecto de abrir y cerrar el menu .
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //Es el menu aun incompleto del usuario que hay en la parte derecha , donde se podra cambiar contraseña , cerrar session
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Crea el menu y hace que se muestre solo faltaran añadirle las opciones
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //Para seleccionar las opciones que pasara en cada una de ellas
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_perfil) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Al apretar las opciones del menu lateral lo que pasa
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio && !esPrincipal) {
            Intent i = new Intent(this, MainActivity.class );
            startActivity(i);

        } else if (id == R.id.nav_perfil) {
            Intent i = new Intent(this, Perfil.class );
            esPrincipal=false;
            startActivity(i);
        } else if (id == R.id.nav_rutas) {
            Intent i = new Intent(this, Rutas.class );
            esPrincipal=false;
            startActivity(i);
        } else if (id == R.id.nav_eventos) {
            Intent i = new Intent(this, Eventos.class );
            esPrincipal=false;
            startActivity(i);
        } else if (id == R.id.nav_acerca_de) {
            Toast.makeText(getApplicationContext(),"Acerca de",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_ayuda) {
            Toast.makeText(getApplicationContext(),"Ayuda",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(getBaseContext(), Login.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }else if (id == R.id.nav_salir) {
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //mantiene la actividad principal como la actual en este momento para evitar el problema del menu al inciar otra vez la misma actividad
    @Override
    protected  void onResume() {
        super.onResume();
        esPrincipal = true;
    }

}
