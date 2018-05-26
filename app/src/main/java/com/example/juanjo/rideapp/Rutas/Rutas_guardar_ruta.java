package com.example.juanjo.rideapp.Rutas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanjo.rideapp.FTP.FTPManager;
import com.example.juanjo.rideapp.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Rutas_guardar_ruta extends AppCompatActivity implements Rutas_guardar2_dialog.CallBack, Rutas_guardar3_dialog.CallBack{
    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "obtenerRuta";
    public static final String SOAP_ACTION = "http://tempuri.org/obtenerRuta";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String METHOD_NAME2 = "borrarRuta";
    public static final String SOAP_ACTION2 = "http://tempuri.org/borrarRuta";
    public static final String METHOD_NAME3 = "modificarRuta";
    public static final String SOAP_ACTION3 = "http://tempuri.org/modificarRuta";


    private Rutas_guardar2_dialog prueba = null;
    private boolean guardado = false;
    private RutaDTO ruta = null;

    private EditText rutas_titulo, rutas_descripcion;
    private TextView rutas_fecha;
    private Rutas_guardar3_dialog corregir_dialog = null;

    private RatingBar rutas_ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rutas_guardar_ruta);

        getSupportActionBar().hide();

        rutas_titulo = (EditText)findViewById(R.id.rutas_titulo);
        rutas_descripcion = (EditText)findViewById(R.id.rutas_descripcion);
        rutas_ratingBar = (RatingBar)findViewById(R.id.rutas_ratingBar);
        rutas_fecha = (TextView)findViewById(R.id.rutas_fecha);

        if(Rutas_nueva_ruta.ultimaRuta != null){
            ruta = Rutas_nueva_ruta.ultimaRuta;
        }else{
            ruta = Rutas_cargar_ruta.ultimaRuta;
        }

        String fecha = getIntent().getExtras().getString("fecha");
        rutas_fecha.setText(fecha);

    }

    @Override
    public void onBackPressed() {
        if(!guardado){
            mostrar_guardar2_dialog();
        }else{
            super.onBackPressed();
        }
    }

    private void mostrar_guardar2_dialog() {
        FragmentManager fm = getSupportFragmentManager();
        prueba = Rutas_guardar2_dialog.newInstance("Some Title");
        prueba.show(fm, "fragment_edit_name");
    }

    @Override
    public void onMyDialogFragmentDetached() {

    }

    /*
    Utilizado para volver a la actividad principal de rutas
     */
    public void returnMain(View view){
        Intent intent = new Intent(this, Rutas_main.class);
        startActivity(intent);
        this.finish();
    }

    public void help(View view){
        Toast.makeText(this, "Elige la dificultad de la ruta.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        boolean saved = getIntent().getExtras().getBoolean("guardado");
        if(!saved){
            new borrar_ruta(this).execute(ruta.getIdRuta());
            //TODO: borrar fotos y ruta del FTP
        }

        this.finish();

        super.onPause();
    }

    private class borrar_ruta extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public borrar_ruta(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME2);
            request.addProperty("ruta", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION2,envelope);
                SoapPrimitive resSoap = (SoapPrimitive) envelope.getResponse();

            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(Boolean result) {

        }
    }

    public void salir_sin_guardar(View view){
        new borrar_ruta(this).execute(ruta.getIdRuta());
        Intent intent = new Intent(this, Rutas_main.class);
        startActivity(intent);
        this.finish();

        //TODO: borrar fotos y ruta del FTP
    }

    public void guardar_ruta(View view){

        if(comprobar_campos()){
            getIntent().removeExtra("guardado");
            getIntent().putExtra("guardado", true);
            ruta.setTitulo(rutas_titulo.getText().toString());
            ruta.setDescripcion(rutas_descripcion.getText().toString());
            ruta.setMapa("ruta" + ruta.getIdRuta() + ".gpx");
            ruta.setDificultad((int)rutas_ratingBar.getRating());
            //TODO: Falta nombre foto

            new modificarRuta(this, this).execute(ruta);

            subir_ruta_FTP("ruta" + ruta.getIdRuta() + ".gpx");

        }else{
            mostrar_corregir_dialog();
        }
    }

    /*
    Utilizada para validar los campos al guardar la ruta
     */
    private boolean comprobar_campos() {
        boolean guardar_correcto = true;

        if(rutas_titulo.getText().toString().equals("")){
            rutas_titulo.setBackgroundColor(getResources().getColor(R.color.rutas_guardar));
            guardar_correcto = false;
        }else{
            rutas_titulo.setBackgroundDrawable(getResources().getDrawable(R.drawable.rutas_fondo_guardar2));
        }

        if(rutas_descripcion.getText().toString().equals("")){
            rutas_descripcion.setBackgroundColor(getResources().getColor(R.color.rutas_guardar));
            guardar_correcto = false;
        }else{
            rutas_descripcion.setBackgroundDrawable(getResources().getDrawable(R.drawable.rutas_fondo_guardar2));
        }

        return guardar_correcto;
    }

    private void mostrar_corregir_dialog() {
        FragmentManager fm = getSupportFragmentManager();
        corregir_dialog = Rutas_guardar3_dialog.newInstance("Some Title");
        corregir_dialog.show(fm, "fragment_edit_name");
    }

    public void corregir (View view){
        if(corregir_dialog != null){
            corregir_dialog.dismiss();
        }
    }

    /*
    Utilizado para modificar una ruta con los datos definitivos
     */
    private class modificarRuta extends AsyncTask<RutaDTO,Void,Integer> {

        private Context context;
        private Activity activity;

        public modificarRuta(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
        }

        protected Integer doInBackground(RutaDTO... params) {

            Integer result = 0;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("ruta");
            pi.setValue(params[0]);
            pi.setType(params[0].getClass());

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME3);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "RutaDTO", params[0].getClass());

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION3,envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                result = Integer.parseInt(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Integer result) {
            if(result.equals(new Integer(0))){

            }else{
                Intent i = new Intent(context, Rutas_main.class);
                startActivity(i);
                activity.finish();
            }
        }
    }

    public void subir_ruta_FTP(String file){
        File ruta = new File(getApplicationContext().getFilesDir() + "/" + file);

        try {
            new FTPManager(getApplicationContext()).FTPSubir(ruta);
            ruta.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
