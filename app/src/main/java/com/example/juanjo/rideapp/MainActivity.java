package com.example.juanjo.rideapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.juanjo.rideapp.Evento.Eventos;
import com.example.juanjo.rideapp.FTP.FTPManager;
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
import java.util.ArrayList;
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

    // Inicializar Rutas en el tab
    List<RutaRecicleView> items = new ArrayList<>();

    private RecyclerView recycler;
    private RecyclerView.Adapter<RecicleViewAdapterRutas.RutasViewHolder> adapter;
    private RecyclerView.LayoutManager lManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        esPrincipal = true;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();



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

        items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por Montserrat", R.drawable.imagenrutaprueba, "12/07/2018",4,R.drawable.caritasonriente,20,R.drawable.caritatriste, 5));
        items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por el puerto de Barcelona", R.drawable.imagenrutaprueba, "11/05/2018",2,R.drawable.caritasonriente,20,R.drawable.caritatriste, 15));
        items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por Tarragona", R.drawable.imagenrutaprueba, "20/01/2018",3,R.drawable.caritasonriente,40,R.drawable.caritatriste, 20));
// Obtener el Recycler
        recycler = findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

// Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(lManager);

// Crear un nuevo adaptador
        adapter = new RecicleViewAdapterRutas(items);
        recycler.setAdapter(adapter);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ultimas_rutas:
                    items.clear();
                    items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por Montserrat", R.drawable.imagenrutaprueba, "12/07/2018",4,R.drawable.caritasonriente,20,R.drawable.caritatriste, 5));
                    items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por el puerto de Barcelona", R.drawable.imagenrutaprueba, "11/05/2018",2,R.drawable.caritasonriente,20,R.drawable.caritatriste, 15));
                    items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por Tarragona", R.drawable.imagenrutaprueba, "20/01/2018",3,R.drawable.caritasonriente,40,R.drawable.caritatriste, 20));
// Obtener el Recycler
                    recycler = findViewById(R.id.reciclador);
                    recycler.setHasFixedSize(true);

// Usar un administrador para LinearLayout
                    lManager = new LinearLayoutManager(getApplicationContext());
                    recycler.setLayoutManager(lManager);

// Crear un nuevo adaptador
                    adapter = new RecicleViewAdapterRutas(items);
                    recycler.setAdapter(adapter);
                    return true;
                case R.id.mejores_rutas:
                    items.clear();
                    items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por Madrid", R.drawable.imagenrutaprueba, "12/07/2018",4,R.drawable.caritasonriente,150,R.drawable.caritatriste, 14));
                    items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por Olesa", R.drawable.imagenrutaprueba, "11/05/2018",2,R.drawable.caritasonriente,145,R.drawable.caritatriste, 52));
                    items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por Arco de triunfo", R.drawable.imagenrutaprueba, "20/01/2018",3,R.drawable.caritasonriente,120,R.drawable.caritatriste, 19));
                    // Obtener el Recycler
                    recycler = findViewById(R.id.reciclador);
                    recycler.setHasFixedSize(true);

// Usar un administrador para LinearLayout
                    lManager = new LinearLayoutManager(getApplicationContext());
                    recycler.setLayoutManager(lManager);

// Crear un nuevo adaptador
                    adapter = new RecicleViewAdapterRutas(items);
                    recycler.setAdapter(adapter);
                    return true;
                case R.id.rutas_mas_dificiles:
                    items.clear();
                    items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por Andorra", R.drawable.imagenrutaprueba, "12/07/2018",5,R.drawable.caritasonriente,30,R.drawable.caritatriste, 4));
                    items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta por los puertos", R.drawable.imagenrutaprueba, "11/05/2018",5,R.drawable.caritasonriente,22,R.drawable.caritatriste, 55));
                    items.add(new RutaRecicleView(R.mipmap.usuariologin, "Ruta Sant Antoni", R.drawable.imagenrutaprueba, "20/01/2018",4,R.drawable.caritasonriente,40,R.drawable.caritatriste, 60));
                    // Obtener el Recycler
                    recycler = findViewById(R.id.reciclador);
                    recycler.setHasFixedSize(true);

// Usar un administrador para LinearLayout
                    lManager = new LinearLayoutManager(getApplicationContext());
                    recycler.setLayoutManager(lManager);

// Crear un nuevo adaptador
                    adapter = new RecicleViewAdapterRutas(items);
                    recycler.setAdapter(adapter);
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
                try {
                    bitmap = ftpManager.FTPCargarImagen(foto);
                    if(bitmap!=null){
                        imagenUsuarioMenu.setImageBitmap(bitmap);
                    }else{
                        imagenUsuarioMenu.setImageResource(R.mipmap.perfil_defecto_avatar_usuario);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    imagenUsuarioMenu.setImageResource(R.mipmap.perfil_defecto_avatar_usuario);
                }
            }else{
                imagenUsuarioMenu.setImageResource(R.mipmap.perfil_defecto_avatar_usuario);
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
            Toast.makeText(getApplicationContext(),"Acerca de",Toast.LENGTH_SHORT).show();
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
