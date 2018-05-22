package com.example.juanjo.rideapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.example.juanjo.rideapp.UsuarioDTO;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Objects;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "obtenerUsuario";
    public static final String SOAP_ACTION = "http://tempuri.org/obtenerUsuario";
    public static final String NAMESPACE = "http://tempuri.org/";
    public AutoCompleteTextView loginusuario;
    public AutoCompleteTextView logincontrasena;
    public CheckBox guarda;
    public static UsuarioDTO user = null;
    public static boolean usuarioGoogle;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    public static final int  SIGN_IN_CODE =777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Al apretar para logear con el usuario o contraseña que no se mueva el fondo

        this.getWindow().setBackgroundDrawableResource(R.drawable.fondo);

        //Sirve para que no salga al inciar el login , el pop up para escribir .
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        loginusuario = findViewById(R.id.loginusuario);
        logincontrasena = findViewById(R.id.logincontraseña);
        guarda = findViewById(R.id.guarda);
        signInButton = findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        if(!MainActivity.primeraVez || !MainActivity.otras) {
            MainActivity.primeraVez = false;
            MainActivity.usuario.add("");
            MainActivity.usuario.add("");
        }else {
            guarda.setChecked(true);
            if(guarda.isChecked()){
                loginusuario.setText(MainActivity.usuario.get(0));
                logincontrasena.setText(MainActivity.usuario.get(1));
            }
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion!", Toast.LENGTH_SHORT).show();
        Log.e("GoogleSignIn", "OnConnectionFailed: " + connectionResult);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    public UsuarioDTO getUsuari(){
        return user;
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();

            assert account != null;
            if(existeUsuario(Objects.requireNonNull(account).getId()))goMainScreen();
            else{
                RegistroGoogle regGog = new RegistroGoogle(result, this);
                regGog.anadir();
            }
            goMainScreen();
        }else{
            Toast.makeText(this, R.string.not_log_in_google,Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen() {
        usuarioGoogle = true;
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.guarda:
                if (checked && MainActivity.primeraVez) {
                    loginusuario.setText(MainActivity.usuario.get(0));
                    logincontrasena.setText(MainActivity.usuario.get(1));

                } else {
                    MainActivity.usuario.clear();
                    MainActivity.primeraVez=false;
                    MainActivity.otras = false;
                    break;
                }
        }
    }
    public void iniciar(){
        usuarioGoogle = false;
        startActivity(new Intent(getBaseContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }
    public void signUP(View view){
        Intent i = new Intent(this, Registro.class );
        startActivity(i);
    }

    public void iniciarboton(View view){
        TareaWSConsulta twsc = new TareaWSConsulta(this);
        twsc.execute(String.valueOf(loginusuario.getText()));
    }




    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    @SuppressLint("StaticFieldLeak")
    private class TareaWSConsulta extends AsyncTask<String,Void,Boolean> {

        private Context context;

        private TareaWSConsulta(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(String... params) {

            return existeUsuario(params[0]);
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                MainActivity.usuario.clear();
                if(guarda.isChecked()) {
                    MainActivity.usuario.add(user.getUsuario());
                    MainActivity.usuario.add(user.getPassword());
                    MainActivity.otras=true;
                }else{
                    MainActivity.otras=false;
                }
                MainActivity.primeraVez = true;

                if (user.getPassword().equals(String.valueOf(logincontrasena.getText()))) {
                    iniciar();
                }else{
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    MainActivity.usuario.clear();
                    MainActivity.primeraVez = false;
                    MainActivity.otras = false;
                }
            }else{
                Toast.makeText(getApplicationContext(), "Usuario incorrecto", Toast.LENGTH_SHORT).show();
                MainActivity.usuario.clear();
                MainActivity.otras = false;
                MainActivity.primeraVez = false;
            }
        }
    }

    @NonNull
    private Boolean existeUsuario(String param) {
        Boolean result = true;

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("idUsuario", param);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION,envelope);
            SoapObject resSoap = (SoapObject)envelope.getResponse();

            // Se corrige el password obtenido, eliminando caracteres de control '%00'. Acuerdate que tambien elimina espacios, VALIDAR PASS AL CREAR USUARIO
            String pass = resSoap.getPropertyAsString(2).replaceAll("\\W", "");
            user = new UsuarioDTO(Integer.valueOf(resSoap.getPropertyAsString(0)), resSoap.getPropertyAsString(1), pass, resSoap.getPropertyAsString(3),
                    resSoap.getPropertyAsString(4), resSoap.getPropertyAsString(5), resSoap.getPropertyAsString(6), resSoap.getPropertyAsString(7));

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }


        return result;
    }
}
