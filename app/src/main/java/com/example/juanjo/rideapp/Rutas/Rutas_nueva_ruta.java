package com.example.juanjo.rideapp.Rutas;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.juanjo.rideapp.DTO.RutaDTO;
import com.example.juanjo.rideapp.DTO.UsuarioDTO;
import com.example.juanjo.rideapp.Login;
import com.example.juanjo.rideapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.GeoApiContext;
import com.google.maps.RoadsApi;
import com.google.maps.model.SnappedPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Rutas_nueva_ruta extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener, Rutas_guardar_dialog.CallBack {

    private static final int MAX_SMS_MESSAGE_LENGTH = 160;
    private static final String SMS_SENT = "my.app";
    private static final int SMS_PORT = 8095;
    private static final String SMS_DELIVERED = "";

    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "nuevaRuta";
    public static final String SOAP_ACTION = "http://tempuri.org/nuevaRuta";
    public static final String NAMESPACE = "http://tempuri.org/";

    //
    public static final String METHOD_NAME2 = "Select_ultimaRuta_usuario";
    public static final String SOAP_ACTION2 = "http://tempuri.org/Select_ultimaRuta_usuario";
    public static final String METHOD_NAME4 = "Select_fecha";
    public static final String SOAP_ACTION4 = "http://tempuri.org/Select_fecha";

    //Estaticas para comprobar los permisos del usuario, y tambien para escribir en el log
    private static final String LOGTAG = "localizacion";
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    //Donde se almacena el mapa sobre el cual se está trabajando
    private GoogleMap mMap;
    //Utilizada para acceder a los servicios de Google
    private GoogleApiClient apiClient;
    //Utilizada para comprobar que el usuario cumple con los requisitos adecuados
    private LocationRequest locRequest;
    //Utilizada para comprobar si está entrando al activity
    private boolean inicio = true;
    //Botón de inicio/parada de ruta
    private ToggleButton start;
    //Marcador utilizado para identificar a tiempo real la ubicación
    private Marker marker = null;
    //Utilizada para guardar objetos LatLng con coordenadas
    private LinkedList<LatLng> latlngs = new LinkedList<LatLng>();
    //Linea dibujada en el mapa, que recogemos aqui para poder removerla antes de volver a cargar de nuevo todas las coordenadas
    Polyline polyline1 = null;

    //
    private boolean gps_noActivated = false;
    private Rutas_guardar_dialog prueba = null;
    private boolean finished = false;

    //
    private Integer resultado_insertar_ruta = 0;

    //
    public static RutaDTO ultimaRuta = null;
    private Boolean obtenerUltimaRuta = true;

    //
    LatLng posicionCorregida = null;

    //
    ImageButton rutas_btn_sos;

    Rutas_sos_failed_dialog sos_failed_Dialog = null;
    Rutas_sos_dialog sosDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rutas_mapa);
        // Se obtiene el fragment del mapa, y se obtiene el mapa asincronamente
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        start = (ToggleButton)findViewById(R.id.rutas_tBtn);
        rutas_btn_sos = (ImageButton)findViewById(R.id.rutas_btn_sos);

        /*
        Se declara el client de Google API, configurando una serie de parametros
         */
        apiClient = new GoogleApiClient.Builder(this)
                //Gestión de conexión a los servicios de manera automatica, ademas de gestionar
                //pequeños errores
                .enableAutoManage(this, this)
                //Callbacks que gestionan la conexión y desconexión de la actualización de la ubicación
                .addConnectionCallbacks(this)
                //La API a utilizar
                .addApi(LocationServices.API)
                .build();

        /*
        Listener utilizado para iniciar/parar la ruta
         */
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start.isChecked()){
                    enableLocationUpdates();
                    //Se mantiene la pantalla siempre encendida
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }else{
                    disableLocationUpdates();
                    mostrar_guardar_dialog();
                }
            }
        });

        rutas_btn_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrar_sos_dialog();
            }
        });
    }

    public void generarMensajeSOS(int codigo){
        if(start.isChecked()){
            String mensaje = codigo + "\n" + Login.getUsuari().getNombre() + ", " + Login.getUsuari().getApellidos() + "\n\n" +
                    "Latitud: " + posicionCorregida.latitude + "\nLongitud: " + posicionCorregida.longitude +
                    "\nDirección: " + getAddressInfoForSMS(posicionCorregida);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + 679436200));
            intent.putExtra("sms_body", mensaje);
            startActivity(intent);
        }else{
            mostrar_sos_failed_dialog();
        }
    }

    public void accidente(View view){
        generarMensajeSOS(33);
    }

    public void averia(View view){
        generarMensajeSOS(37);
    }


    private String getAddressInfoForSMS(LatLng coordenadas) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(coordenadas.latitude, coordenadas.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);

        return address;
    }

    /*
    Metodo que implementa la interfaz OnMapReadyCallback, es llamado cuando el mapa esta
    listo para ser utilizado
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*
        Se añade el estilo en formato JSON creado en la web de Google
         */
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.formato_mapa));

        //Se enciende la actualización automatica para detecta la primera ubicación
        enableLocationUpdates();
    }

    /*
    Metodo encargado de configurar los requisitos para actualizar la ubicación,
    que más tarde serán comparados con los del usuario
     */
    private void enableLocationUpdates() {

        locRequest = new LocationRequest();
        //Intervalo definido para recibir la ubicación cada 2 segs. Haciendo prioritario el intervalo menor de cualquier otra aplicación
        locRequest.setInterval(2000);
        //Intervalo definido para decirle el minimo de tiempo que es capaz nuestro sistema de procesar las actualizaciones
        locRequest.setFastestInterval(1000);
        //Se define como de exacta va a ser la ubicación devuelta
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        /*
        Se cargan los requisitos en un objeto LocationSettingsRequest
         */
        LocationSettingsRequest locSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locRequest)
                        .build();


        /*
        Se compara la configuración requerida con la del usuario, mediante el objeto definido arriba
         */
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        apiClient, locSettingsRequest);

        /*
        Se configura el ResultCallback, que puede recibir 3 posibles valores, SUCCES (la configuración
        es correcta), RESOLUTION_REQUIRED(hace falta solucionar la configuración del usuario),
        SETTINGS_CHANGE_UNAVAILABLE(el dispositivo del usuario es incapaz de configurar los requisitos)
         */
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(LOGTAG, "Configuración correcta");
                        /*
                        Se comienza con la actualización de ubicaciones
                         */
                        startLocationUpdates();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.i(LOGTAG, "Se requiere actuación del usuario");
                            /*
                            Se intenta que el usuario pueda arreglar el conflicto de requisitos
                             */
                            status.startResolutionForResult(Rutas_nueva_ruta.this, PETICION_CONFIG_UBICACION);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(LOGTAG, "Error al intentar solucionar configuración de ubicación");
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(LOGTAG, "No se puede cumplir la configuración de ubicación necesaria");
                        /*
                        En el caso de que no se pueda actualizar la configuración, se pone el Toogle
                        Button apagado
                         */
                        start.setChecked(false);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        start.setChecked(false);
                        mostrar_gps_dialog();
                        gps_noActivated = true;
                        Log.i(LOGTAG, "El usuario no ha realizado los cambios de configuración necesarios");
                        break;
                }
                break;
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(Rutas_nueva_ruta.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Sería recomendable implementar la posible petición en caso de no tenerlo.

            Log.i(LOGTAG, "Inicio de recepción de ubicaciones");

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    apiClient, locRequest, Rutas_nueva_ruta.this);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
        Toast.makeText(this, "Se están produciendo errores de conexión..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        //Se ha interrumpido la conexión con Google Play Services
        Toast.makeText(this, "Se ha interrumpido la conexiçon..", Toast.LENGTH_SHORT).show();
        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOGTAG, "Recibida nueva ubicación!");
        MarkerOptions options;

        /*
        Si la booleana inicio es true (el usuario acaba de entrar a la sección de rutas),
        se recoge la localización y se posiciona la camara, además se añade un marcador
        personalizado, con la foto del usuario actualmente logueado
         */
        if(inicio){

            //Se recoge la posición actual
            LatLng posicionActual = new LatLng(location.getLatitude(), location.getLongitude());

            //Prueba API Request
            try {
                new CallAPI(getApplicationContext()).execute(posicionActual.latitude + "," + posicionActual.longitude).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //Se crea un nuevo marcador, se le pasa la posición
            options = new MarkerOptions().position(posicionCorregida);

            //Se crea la imagen en formato Bitmap con la función definida más abajo
            Bitmap bitmap = createUserBitmap();
            if(bitmap!=null) {
                options.title("Nombre de usuario");
                options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                options.anchor(0.5f, 0.907f);
                marker = mMap.addMarker(options);
            }

            //Se crea la nueva posición de la camara
            CameraPosition camera = new CameraPosition.Builder()
                    .target(posicionCorregida)
                    .zoom(18)
                    .bearing(45)
                    .tilt(70)
                    .build();

            //Se inicia el cambio de posición de la camara
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

            //Se pone la booleana a false y se apaga la actualización de ubicaciones automaticas,
            //la proxima vez cuando comience a grabar la ruta, la booleana estará a false y
            // si que recibirá actualizaciones sin parar
            inicio = false;
            disableLocationUpdates();
        }else{
            //Se remueven el marcador y las lineas cada vez que se entra, para que no se repitan
            if(marker != null){
                marker.remove();
            }

            if(polyline1 != null){
                polyline1.remove();
            }

            //Se recoge la posición actual
            LatLng posicionActual = new LatLng(location.getLatitude(), location.getLongitude());

            //Prueba API Request
            try {
                new CallAPI(getApplicationContext()).execute(posicionActual.latitude + "," + posicionActual.longitude).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //Se crea un nuevo marcador, se le pasa la posición
            options = new MarkerOptions().position(posicionCorregida);

            //Se crea la imagen en formato Bitmap con la función definida más abajo
            Bitmap bitmap = createUserBitmap();
            if(bitmap!=null) {
                options.title("Nombre de usuario");
                options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                options.anchor(0.5f, 0.907f);
                marker = mMap.addMarker(options);
            }

            //Se crea la nueva posición de la camara
            CameraPosition camera = new CameraPosition.Builder()
                    .target(posicionCorregida)
                    .zoom(19)
                    .bearing(45)
                    .tilt(70)
                    .build();

            //Se inicia el cambio de posición de la camara
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

            //Se van añadiendo las coordenadas en una lista
            latlngs.add(posicionCorregida);

            /*
            Se configura como se van a dibujar las lineas
             */
            PolylineOptions polyoptions = new PolylineOptions()
                    .clickable(true)
                    .color(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                    .width(40)
                    .startCap(new RoundCap())
                    .endCap(new RoundCap());

            //Se añaden todas las coordenadas
            for (LatLng i: latlngs){
                polyoptions.add(i);
            }

            //Se dibujan en el mapa
            polyline1 = mMap.addPolyline(polyoptions);

        }

    }

    /*
    Utilizado para desactivar la localización automatica
     */
    private void disableLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                apiClient, this);

    }

    /*
    TODO: Falta conectar con el usuario actual
    Metodo utilizado para crear un Bitmap con la imagen de perfil
     */
    private Bitmap createUserBitmap() {
        Bitmap result = null;
        try {
            //Se crea el bitmap resultante
            result = Bitmap.createBitmap(dp(62), dp(76), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            //Creamos un canvas donde ir pintando el resultado
            Canvas canvas = new Canvas(result);
            //Se crea un drawable con la imagen inicial, y configuramos su tamaño, finalmente se pinta en el canvas
            Drawable drawable = getResources().getDrawable(R.drawable.rutas_pin_localizacion);
            drawable.setBounds(0, 0, dp(62), dp(76));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            byte[] decodedString = Base64.decode(Login.getUsuari().getAvatar(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rutas_avatar2);
            //Bitmap bitmap = manager.FTPCargarImagen(Login.getUsuari().getAvatar());
            //Bitmap bitmap = BitmapFactory.decodeFile(path.toString()); /*WS_URL*/
            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(52) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(5), dp(5), dp(52 + 5), dp(52 + 5));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {}
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    /*
    Utilizado para ajustar la imagen con la densidad del telefono
     */
    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }

    /*
    Utilizado para mostrar el dialogo de señal gps
     */
    private void mostrar_gps_dialog() {
        Rutas_gps_dialog prueba = null;
        FragmentManager fm = getSupportFragmentManager();
        prueba = Rutas_gps_dialog.newInstance("Some Title");
        prueba.show(fm, "fragment_edit_name");
    }

    private void mostrar_guardar_dialog() {
        prueba = null;
        FragmentManager fm = getSupportFragmentManager();
        prueba = Rutas_guardar_dialog.newInstance("Some Title");
        prueba.show(fm, "fragment_edit_name");
        finished = true;
    }

    public void mostrar_sos_dialog() {
        FragmentManager fm = getSupportFragmentManager();
        sosDialog = Rutas_sos_dialog.newInstance("Some Title");
        sosDialog.show(fm, "fragment_edit_name");
    }

    public void mostrar_sos_failed_dialog() {
        FragmentManager fm = getSupportFragmentManager();
        sos_failed_Dialog = Rutas_sos_failed_dialog.newInstance("Some Title");
        sos_failed_Dialog.show(fm, "fragment_edit_name");
    }

    public void sos_failed_dialog_atras(View view){
        if(sos_failed_Dialog != null){
            sos_failed_Dialog.dismiss();
            sosDialog.dismiss();
        }
    }

    /*
    Utilizado para mostrar el dialogo de permisos y comenzar con las actualizaciones automaticas
     */
    public void activarGps(View view){
        enableLocationUpdates();
    }

    /*
    Utilizado para volver a la actividad principal de rutas
     */
    public void returnMain(View view){
        Intent intent = new Intent(this, Rutas_main.class);
        startActivity(intent);
        this.finish();
    }

    /*
    Utilizado para comprobar la señal GPS
     */
    private boolean señalGPS_On() throws Settings.SettingNotFoundException {
        int gpsSignal = Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE);

        if(gpsSignal == 0){
            return false;
        }

        return true;
    }

    /*
    Utilizado para que al volver de comprobar la activación de permisos, en el caso de no activarse
    vuelve a mostrar el dialogo, sino empieza a recibir actualizaciones de ubicación
     */
    @Override
    protected void onResume() {
        if(gps_noActivated){
            try {
                if(!señalGPS_On()){
                    mostrar_gps_dialog();
                }else{
                    gps_noActivated = false;
                    enableLocationUpdates();
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            if(finished){
                enableLocationUpdates();
                start.setChecked(true);
                finished = false;
            }
        }

        super.onResume();
    }



    public void atras(View view){
        if(prueba != null){
            prueba.dismiss();
            enableLocationUpdates();
            start.setChecked(true);
        }
    }

    public void guardar(View view){

        //Se recoge el usuario actual
        UsuarioDTO usuario_actual = Login.getUsuari();

        /*
        Se inserta una nueva ruta solamente con el usuario, esto se hace para que al generar el fichero
        gpx, pueda consultar despues el id de ruta, y asi identificar el fichero con un nombre unico
        */
        new insertarRuta(getApplicationContext(), this).execute(usuario_actual.getIdUsuario());

    }

    private void generarGPX(int idRuta) {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<gpx version=\"1.0\">\n";
        String footer = "</gpx>";


        OutputStreamWriter outputStreamWriter = null;
        String file = "ruta" + String.valueOf(idRuta) + ".gpx";

        File ruta = new File(file);

        try {
            outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput(file, Activity.MODE_PRIVATE));

            outputStreamWriter.write(header);

            for(LatLng coord : latlngs){
                String wpt = "<wpt lat=\"";
                wpt += String.valueOf(coord.latitude);
                wpt += "\" lon=\"";
                wpt += String.valueOf(coord.longitude);
                wpt += "\">\n</wpt>\n";

                outputStreamWriter.write(wpt);
            }

            outputStreamWriter.write(footer);
            outputStreamWriter.flush();

        } catch (FileNotFoundException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }finally {
            if(outputStreamWriter != null){
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            }
        }
    }

    @Override
    public void onMyDialogFragmentDetached() {
        start.setChecked(true);
        enableLocationUpdates();
    }

    @Override
    public void onBackPressed() {
        if(start.isChecked()){
            mostrar_guardar_dialog();
        }else{
            Intent i = new Intent(this, Rutas_main.class);
            startActivity(i);
            this.finish();
        }
    }

    /*
    Utilizado para insertar un nuevo registro de 'Ruta' en bd
     */
    private class insertarRuta extends AsyncTask<Integer,Void,Integer> {

        private Activity activity;
        private Context context;

        public insertarRuta(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
        }

        protected Integer doInBackground(Integer... params) {

            Integer result = 0;

            RutaDTO ruta = new RutaDTO(params[0], "", "", "", 0, 0, 0, "");

            PropertyInfo pi = new PropertyInfo();
            pi.setName("ruta");
            pi.setValue(ruta);
            pi.setType(ruta.getClass());

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "RutaDTO", ruta.getClass());

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION,envelope);
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
            if(result != 0){
                //Se recoge el usuario actual
                UsuarioDTO usuario_actual = Login.getUsuari();

                /*
                Se recoge la ultima ruta, para tener el ultimo idRuta, y asi poder trabajar en la
                siguiente activity
                 */
                new obtener_ultima_ruta(getApplicationContext(), activity).execute(usuario_actual.getIdUsuario());
            }
        }
    }

    /*
    Utilizado para obtener la ultima ruta insertada por el usuario
     */
    private class obtener_ultima_ruta extends AsyncTask<Integer,Void,Boolean> {

        private Activity activity;
        private Context context;

        public obtener_ultima_ruta(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME2);
            request.addProperty("idUsuario", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION2,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();
                ultimaRuta = new RutaDTO(Integer.valueOf(resSoap.getPropertyAsString(0)),
                        Integer.valueOf(resSoap.getPropertyAsString(1)),
                        resSoap.getPropertyAsString(2), resSoap.getPropertyAsString(3),
                        resSoap.getPropertyAsString(4), Integer.valueOf(resSoap.getPropertyAsString(5)),
                        Integer.valueOf(resSoap.getPropertyAsString(6)),
                        Integer.valueOf(resSoap.getPropertyAsString(7)), resSoap.getPropertyAsString(8));
            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                generarGPX(ultimaRuta.getIdRuta());
                new obtener_fecha(context, activity).execute(ultimaRuta.getIdRuta());
                Intent i = new Intent(getApplicationContext(), Rutas_guardar_ruta.class);
                i.putExtra("idRuta", ultimaRuta.getIdRuta());
                i.putExtra("guardado", false);

            }
        }
    }

    private class obtener_fecha extends AsyncTask<Integer,Void,Boolean> {

        private Context context;
        private Activity activity;

        public obtener_fecha(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME4);
            request.addProperty("idRuta", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION4,envelope);
                SoapPrimitive resSoap = (SoapPrimitive) envelope.getResponse();
                Intent i = new Intent(context, Rutas_guardar_ruta.class);
                i.putExtra("fecha", resSoap.toString());
                startActivity(i);
                activity.finish();

            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(Boolean result) {

        }
    }

    public class CallAPI extends AsyncTask<String, String, String> {

        private Context context;

        public CallAPI(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {

            String data = params[0]; //data to post

            String urlString = "https://roads.googleapis.com/v1/snapToRoads?path=" +
                    data + "&interpolate=true&key=AIzaSyDi09dRhP4BFBECSqdnDZaHpGK2x78QbC8";

            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {

                java.net.URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            String finalResult = result.toString();

            JSONObject jsonObject = null;
            JSONArray snappedPoints = null;
            try {
                jsonObject = new JSONObject(finalResult);
                snappedPoints = (JSONArray)jsonObject.get("snappedPoints");
                JSONObject jsonObject2 = new JSONObject(snappedPoints.getJSONObject(0).getString("location"));
                Double latitude = jsonObject2.getDouble("latitude");
                Double longitude = jsonObject2.getDouble("longitude");

                posicionCorregida = new LatLng(latitude, longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return result.toString();
        }
    }
}
