package com.example.juanjo.rideapp.VentanaPrincipal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.juanjo.rideapp.Amigos.Amigos_main;
import com.example.juanjo.rideapp.DTO.Ruta_infoDTO;
import com.example.juanjo.rideapp.Evento.Eventos;
import com.example.juanjo.rideapp.FTP.FTPManager;
import com.example.juanjo.rideapp.Login.Login;
import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.Rutas.Rutas_main;
import com.example.juanjo.rideapp.Usuario.Perfil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    public static boolean esPrincipal;
    public static boolean primeraVez = false;
    public CircleImageView imagenUsuarioMenu;
    public TextView nombreUsuarioMenu;
    public TextView correoUsuarioMenu;
    public CircleImageView imagenUsuarioMenuVentanaPrincipal;
    public static  boolean otras = true;
    public static ArrayList<String> usuario = new ArrayList<>();
    private GoogleApiClient googleApiClient;


    public static final String URL = "http://rideapp2.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "listaRuta_info";
    public static final String SOAP_ACTION = "http://tempuri.org/listaRuta_info";
    public static final String NAMESPACE = "http://tempuri.org/";

    private RecyclerView recycler;
    private int idUsuario;
    private List<Ruta_infoDTO> datosRuta = new ArrayList<>();
    private List<Ruta_infoDTO> ultimaRuta = new ArrayList<>();
    private List<Ruta_infoDTO> topValoracion = new ArrayList<>();
    private List<Ruta_infoDTO> topRutas = new ArrayList<>();
    private RecicleViewAdapterRutas adapter2;

    // Inicializar Rutas en el tab


    // private RecyclerView recycler;
    private RecyclerView.LayoutManager lManager;
    Handler uiHandlerPerfil ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//Iniciar tabs
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Crea el boton del menu para poderse abrir y cerrar
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Al apretar adquiere el valor del boton y te muestra el menu
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        imagenUsuarioMenu = hView.findViewById(R.id.imagenUsuarioMenu);
        nombreUsuarioMenu = hView.findViewById(R.id.nombreUsuarioMenu);
        correoUsuarioMenu = hView.findViewById(R.id.correoUsuarioMenu);
        imagenUsuarioMenuVentanaPrincipal = findViewById(R.id.action_perfil);


        esPrincipal = true;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();




        recycler = findViewById(R.id.reciclador);
        idUsuario = getIntent().getExtras().getInt("idUsuario");
        uiHandlerPerfil = new Handler(this.getMainLooper());
        uiHandlerPerfil.post(new Runnable() {
            @Override
            public void run() {


                try {
                    datosRuta.clear();
                    new obtenerInfoRutas(getApplicationContext()).execute(idUsuario).get();

                    ultimaRuta.add(datosRuta.get(0));
                    ultimaRuta.add(datosRuta.get(1));
                    ultimaRuta.add(datosRuta.get(2));
                    adapter2 = new RecicleViewAdapterRutas(datosRuta);
                    recycler.setAdapter(adapter2);
                    recycler.setHasFixedSize(true);

                    lManager = new LinearLayoutManager(getApplicationContext());
                    recycler.setLayoutManager(lManager);
                    ultimaRuta.clear();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    private class obtenerInfoRutas extends AsyncTask<Integer,Void,Boolean> {

        private Context context;

        public obtenerInfoRutas(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Integer... params) {

            Boolean result = true;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            request.addProperty("idUsuario", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION,envelope);
                SoapObject resSoap = (SoapObject)envelope.getResponse();

                for (int i = 0; i < resSoap.getPropertyCount(); i++){
                    SoapObject iu = (SoapObject)resSoap.getProperty(i);

                    Ruta_infoDTO ruta_info = new Ruta_infoDTO(Integer.valueOf(iu.getPropertyAsString(0)),
                            iu.getPropertyAsString(1), iu.getPropertyAsString(2), iu.getPropertyAsString(3),
                            Integer.valueOf(iu.getPropertyAsString(4)), Integer.valueOf(iu.getPropertyAsString(5)),
                            Integer.valueOf(iu.getPropertyAsString(6)), iu.getPropertyAsString(7));

                    datosRuta.add(ruta_info);
                }

            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){

            }else{

            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ultimas_rutas:

                    uiHandlerPerfil.post(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                datosRuta.clear();
                                new obtenerInfoRutas(getApplicationContext()).execute(idUsuario).get();
                                /*Collections.sort(datosRuta, new Comparator<Ruta_infoDTO>(){
                                    @Override
                                    public int compare(Ruta_infoDTO r1 , Ruta_infoDTO r2){
                                        return Integer.compare(Integer.parseInt(r2.getFecha_ruta()), Integer.parseInt(r1.getFecha_ruta()));
                                    }
                                });*/
                                ultimaRuta.add(datosRuta.get(0));
                                ultimaRuta.add(datosRuta.get(1));
                                ultimaRuta.add(datosRuta.get(2));
                                adapter2 = new RecicleViewAdapterRutas(datosRuta);
                                recycler.setAdapter(adapter2);
                                recycler.setHasFixedSize(true);

                                lManager = new LinearLayoutManager(getApplicationContext());
                                recycler.setLayoutManager(lManager);

                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                    return true;
                case R.id.mejores_rutas:

                    uiHandlerPerfil.post(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                datosRuta.clear();
                                new obtenerInfoRutas(getApplicationContext()).execute(idUsuario).get();
                                Collections.sort(datosRuta, new Comparator<Ruta_infoDTO>(){
                                    @Override
                                    public int compare(Ruta_infoDTO r1 , Ruta_infoDTO r2){
                                        return Integer.compare(r2.getLikes(), r1.getLikes());
                                    }
                                });
                                topRutas.add(datosRuta.get(0));
                                topRutas.add(datosRuta.get(1));
                                topRutas.add(datosRuta.get(2));
                                adapter2 = new RecicleViewAdapterRutas(topRutas);
                                recycler.setAdapter(adapter2);
                                recycler.setHasFixedSize(true);

                                lManager = new LinearLayoutManager(getApplicationContext());
                                recycler.setLayoutManager(lManager);
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                    return true;
                case R.id.rutas_mas_dificiles:
                    uiHandlerPerfil.post(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                datosRuta.clear();
                                new obtenerInfoRutas(getApplicationContext()).execute(idUsuario).get();
                                Collections.sort(datosRuta, new Comparator<Ruta_infoDTO>(){
                                    @Override
                                    public int compare(Ruta_infoDTO r1 , Ruta_infoDTO r2){
                                        return Integer.compare(r2.getDificultad(), r1.getDificultad());
                                    }
                                });
                                topValoracion.add(datosRuta.get(0));
                                topValoracion.add(datosRuta.get(1));
                                topValoracion.add(datosRuta.get(2));

                                adapter2 = new RecicleViewAdapterRutas(topValoracion);
                                recycler.setAdapter(adapter2);
                                recycler.setHasFixedSize(true);

                                lManager = new LinearLayoutManager(getApplicationContext());
                                recycler.setLayoutManager(lManager);

                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                    return true;
            }
            return false;
        }
    };






    @Override
    protected void onStart(){
        super.onStart();
        if(Login.usuarioGoogle) {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
            if (opr.isDone()) {
                GoogleSignInResult result = opr.get();
                handleSignResult(result);
            } else {
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        handleSignResult(googleSignInResult);
                    }
                });
            }
        }else{
            String foto = Login.user.getAvatar();
            nombreUsuarioMenu.setText(Login.user.getNombre());
            correoUsuarioMenu.setText(Login.user.getCorreo());
            Bitmap bitmap;
            if(foto!=null && foto!=""){
                FTPManager ftpManager = new FTPManager(this);
                byte[] decodeValue = Base64.decode(foto, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(decodeValue, 0, decodeValue.length);

                if(bitmap!=null){
                    imagenUsuarioMenu.setImageBitmap(bitmap);
                }else{
                    imagenUsuarioMenu.setImageResource(R.drawable.user_default);
                }
            }else{
                imagenUsuarioMenu.setImageResource(R.drawable.user_default);
            }
        }
    }

    private void handleSignResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            Glide.with(this).load(Objects.requireNonNull(account).getPhotoUrl()).into(imagenUsuarioMenu);
            String nombreUsuarioGoogle = account.getDisplayName();
            String correoUsuarioGoogle = account.getEmail();
            nombreUsuarioMenu.setText(nombreUsuarioGoogle);
            correoUsuarioMenu.setText(correoUsuarioGoogle);
            //Glide.with(this).load(account.getPhotoUrl()).into(imagenUsuarioMenuVentanaPrincipal);
        }
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this,Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Este metodo te crea el efecto de abrir y cerrar el menu .
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Al apretar las opciones del menu lateral lo que pasa
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio && !esPrincipal) {
            Intent i = new Intent(this, MainActivity.class );
            startActivity(i);

        } else if (id == R.id.nav_perfil) {
            Intent i = new Intent(this, Perfil.class );
            i.putExtra("usuario", Login.getUsuari().getIdUsuario());
            esPrincipal=false;
            startActivity(i);
        } else if (id == R.id.nav_amigos) {
            Intent i = new Intent(this, Amigos_main.class );
            i.putExtra("usuario", Login.getUsuari().getIdUsuario());
            esPrincipal=false;
            startActivity(i);
        } else if (id == R.id.nav_rutas) {
            Intent i = new Intent(this, Rutas_main.class );
            esPrincipal=false;
            startActivity(i);
        } else if (id == R.id.nav_eventos) {
            Intent i = new Intent(this, Eventos.class );
            esPrincipal=false;
            startActivity(i);
        } else if (id == R.id.nav_acerca_de) {
            Intent i = new Intent(this,Acerca_de.class);
            esPrincipal=false;
            startActivity(i);
        } else if (id == R.id.nav_ayuda) {
            Toast.makeText(getApplicationContext(),"Ayuda",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            if(Login.usuarioGoogle) {
                Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            goLogInScreen();
                        } else {
                            Toast.makeText(getApplicationContext(), "No se pudo cerrar session", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                goLogInScreen();
            }
        }else if (id == R.id.nav_salir) {
            this.finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //mantiene la actividad principal como la actual en este momento para evitar el problema del menu al inciar otra vez la misma actividad
    @Override
    protected  void onResume() {
        super.onResume();
        esPrincipal = true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion!", Toast.LENGTH_SHORT).show();
        Log.e("GoogleSignIn", "OnConnectionFailed: " + connectionResult);
    }
}