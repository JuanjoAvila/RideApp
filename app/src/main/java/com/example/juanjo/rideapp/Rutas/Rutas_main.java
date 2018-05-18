package com.example.juanjo.rideapp.Rutas;

import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.juanjo.rideapp.R;
import com.viewpagerindicator.CirclePageIndicator;

public class Rutas_main extends AppCompatActivity {

    private Button rutas_button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rutas_main);

        rutas_button1 = (Button)findViewById(R.id.rutas_button1);

        getSupportActionBar().hide();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new Rutas_page_adapter(this));
        viewPager.setCurrentItem(0);

        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(viewPager);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        rutas_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!señalGPS_On()){
                        mostrar_gps_dialog();
                    }else{
                        mostrar_opciones_dialog();
                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void mostrar_gps_dialog() {
        Rutas_gps_dialog prueba = null;
        FragmentManager fm = getSupportFragmentManager();
        prueba = Rutas_gps_dialog.newInstance("Some Title");
        prueba.show(fm, "fragment_edit_name");
    }

    private void mostrar_opciones_dialog() {
        Rutas_opciones_dialog prueba = null;
        FragmentManager fm = getSupportFragmentManager();
        prueba = Rutas_opciones_dialog.newInstance("Some Title");
        prueba.show(fm, "fragment_edit_name");
    }

    private boolean señalGPS_On() throws Settings.SettingNotFoundException {
        int gpsSignal = Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE);

        if(gpsSignal == 0){
            return false;
        }

        return true;
    }

    public void nuevaRuta(View view){
        Intent intent = new Intent(getApplicationContext(), Rutas_nueva_ruta.class);
        startActivity(intent);
    }

    public void cargarRuta(View view){
        Intent intent = new Intent(getApplicationContext(), Rutas_cargar_ruta.class);
        startActivity(intent);
    }

    public void activarGps(View view){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
}
