package com.example.juanjo.rideapp.Rutas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.juanjo.rideapp.Login.Login;
import com.example.juanjo.rideapp.R;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * @author RideApp
 * @version Final
 * Actividad donde se inicia Rutas
 */
public class Rutas_main extends AppCompatActivity {

    private Button rutas_button1;
    private Rutas_opciones_dialog prueba = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rutas_main);

        rutas_button1 = (Button)findViewById(R.id.rutas_button1);

        getSupportActionBar().hide();

        /*
        Se inicializa el ViewPager y se le asigna su adaptador
         */
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new Rutas_page_adapter(this));
        viewPager.setCurrentItem(0);

        /*
        Sirve para indicar en que pagina del ViewPager nos encontramos
         */
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(viewPager);

        /*
        Se le asigna un listener, cuando se clica aparece el dialogo de opciones
         */
        rutas_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mostrar_opciones_dialog();
            }
        });
    }

    /**
     * Dialogo utilizado para mostrar las opciones disponibles en Rutas
     */
    private void mostrar_opciones_dialog() {
        FragmentManager fm = getSupportFragmentManager();
        prueba = Rutas_opciones_dialog.newInstance("Some Title");
        prueba.show(fm, "fragment_edit_name");
    }

    /**
     * Lanza la actividad Rutas_nueva_ruta
     * @param view
     */
    public void nuevaRuta(View view){
        Intent intent = new Intent(getApplicationContext(), Rutas_nueva_ruta.class);
        startActivity(intent);
        prueba.dismiss();
        this.finish();
    }

    /**
     * Lanza la actividad Rutas_mostrar_rutas
     * @param view
     */
    public void cargarRuta(View view){
        Intent intent = new Intent(getApplicationContext(), Rutas_mostrar_rutas.class);
        intent.putExtra("idUsuario", Login.getUsuari().getIdUsuario());
        startActivity(intent);
        prueba.dismiss();
        this.finish();
    }

}
