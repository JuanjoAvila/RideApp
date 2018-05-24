package com.example.juanjo.rideapp;

import android.annotation.SuppressLint;
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
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Objects;

/**
 * Esta actividad abre un login capaz de registrar a un usuario en la base de datos , dejar entrar a la aplicación
 * principal a este mismo usuario con una cuenta registrada en base de datos y por ultimo poder acceder mediante Google.
 * Es capáz de recordar el usuario introducido, cosa que a la hora de salir si el campo de recordar ha sido apretado, el
 * usuario aparecerá automaticamente en los campos.
 * @author Juanjo Avila Chavero
 * @version 1.0
 */
public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //Estas cuatro variables final acceden al web service para las consultas ya creadas en la base de datos, daran la posiblidad de obtener el usuario.
    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "obtenerUsuario";
    public static final String SOAP_ACTION = "http://tempuri.org/obtenerUsuario";
    public static final String NAMESPACE = "http://tempuri.org/";

    //Campos para introducir el usuario y la contraseña
    public AutoCompleteTextView loginusuario;
    public AutoCompleteTextView logincontrasena;

    //Checkbox para guardar el usuario una vez se active para cuando salga automaticamente se apunte solo en los campos de usuario y contraseña.
    public CheckBox guarda;

    //Esta variable permite guardar el usuario actual para luego en otras clases utilizarlo
    public static com.example.juanjo.rideapp.UsuarioDTO user = null;

    //Permite saber si es un usuario google o un usuario de la propia aplicacion.
    public static boolean usuarioGoogle;

    //Api de google con lo que permite poder logear con google en la aplicacion
    private GoogleApiClient googleApiClient;
    public static final int  SIGN_IN_CODE =777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Al apretar para logear con el usuario o contraseña que no se mueva el fondo
        this.getWindow().setBackgroundDrawableResource(R.drawable.fondo);

        //Sirve para que no salga al inciar el login , el pop up para escribir .
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Declaracion de los campos de usuario, contraseña y guardado checkbox.
        loginusuario = findViewById(R.id.loginusuario);
        logincontrasena = findViewById(R.id.logincontraseña);
        guarda = findViewById(R.id.guarda);

        //Personalizacion del boton de google dandole variedad.
        SignInButton signInButton = findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);

        //Funcion del checkbox
        funcionDelCheckbox();

        /* Inicializa el mecanismo de logeo de google. Primeramente si ya esta logeado te abre una ventana emergente donde te muestra los correos
        * utilizados anteriormente. Si ese no es el caso te abre una nueva actividad donde se te pedirá introducir el correo y la contraseña que tengas
        * en google con la que recabará los datos posteriormente en otra función , se validará y se accederá dentro de la aplicación.
        */
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

    /* Esta funcion permite obtener si es la primera vez que entra el usuario a la aplicacion para dejar los campos vacios de usuario y contraseña
    * Esto permite que que cuando el usuario le de al boton del checkBox , la siguiente vez que aparezca en el login detectara que esta apretado
    * y ya no sera la primera vez que entre por lo tanto asignara en los campos el nombre y contraseña de usuario introducidos anteriormente.
    */
    private void funcionDelCheckbox() {
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
    }
    //Si da problemas de conexion o ha habido algun fallo saltara el error de conexión, aun asi se podra acceder despues a la aplicación.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion!", Toast.LENGTH_SHORT).show();
        Log.e("GoogleSignIn", "OnConnectionFailed: " + connectionResult);
    }

    /**
     * Maneja el control del login con google en la actividad principal.
     * En ella se recibe un codigo de peticion que se le hace a google , si coincide con el codigo declarado, se manejara el resultado con una funcion externa
     * por comodidad.
     * @param requestCode Se le pasa el codigo resultante de la petición de google.
     * @param resultCode Recibe el codigo con el que comparara si se acepta o no .
     * @param data Se pasara la informacion de google para poder llevar a cabo el registro con google
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     * Manejara el Resultado del registro de google
     * Si el registro ha sido correcto , cogera los datos de la cuenta de google y obtendrá si el usuario existe en la base de datos
     * principal de la aplicación , si no existe creará el usuario dentro de la base de datos con lo que permitirá juntar las diferencias
     * de un usuario de google y un usuario de aplicación haciendolos iguales. Y si ya existe irá directamente a la pantalla principal mediante una
     * función para facilitar las cosas.
     * @param result Devuelve un inicio de sesión satisfactorio o en contra un error al que no se puede iniciar sesión
     */
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

    /**
     * Este metodo permite acceder a la ventana principal de la aplicación siendo un usuario de Google.
     * Cabe destacar que en este caso se añaden flags que permiten cerrar la actividad anterior, asi el usuario si tira para atras
     * para intentar volver al login se saldra de la aplicación automaticamente.
     */
    private void goMainScreen() {
        usuarioGoogle = true;
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    /**
     * Obtiene el usuario , en este caso el actual por el login. Este metodo se utilizara en las demas clases
     * cuando se necesite acceder al usuario actual.
     * @return Devuelve el usuario actual de la aplicacion al loguear.
     */
    public static com.example.juanjo.rideapp.UsuarioDTO getUsuari(){
        return user;
    }

    /**
     * Esta funcion se activa al apretar el boton del checkBox recuerdame. Permite asignar el usuario y la contraseña en los
     * campos para guardarlo. Si se quita el check se borraran automaticamente los datos en la lista para una siguiente vez poderlos añadir con el
     * mismo u otro usuario distinto.
     * @param view Nos obtiene la id para poder identificar el checkbox apretado en el onClick
     */
    public void onCheckboxClicked(View view) {
        // Saber si esta apretado el checkbox
        boolean checked = ((CheckBox) view).isChecked();

        // Consigue la id del checkbox para saber cual es
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

    /**
     * Esta es una funcion que se activa al apretar el boton de registrarse, abriendo asi una nueva actividad dónde dará
     * la posibilidad de poder introducir sus datos para guardarlos en la base de datos.
     * @param view Permite obtener el boton mediante el onClick para ejecute la funcion de registrarse en una nueva actividad.
     */
    public void signUP(View view){
        Intent i = new Intent(this, Registro.class );
        startActivity(i);
    }

    /**
     * En esta funcion llama a una clase creada expresamente para la consulta a la base de datos, obtenermos el usaurio y ejecutamos la consulta
     * junto con su resultado que luego se tratará.
     * @param view Obtiene el boton que al darle obtendra el usuario y entrará en la ventana principal .
     */
    public void iniciarboton(View view){
        TareaWSConsulta twsc = new TareaWSConsulta();
        twsc.execute(String.valueOf(loginusuario.getText()));
    }


    /**
     * Esta clase esta creada exclusivamente para uso de la consulta al Web Service mediante una tarea asincrona en trasfondo ,
     * por lo tanto no afectaria a la aplicacion para nada. Esta obtiene el usuario si existe en la base de datos o no .
     */
    @SuppressLint("StaticFieldLeak")
    private class TareaWSConsulta extends AsyncTask<String,Void,Boolean> {

        private TareaWSConsulta() {
        }

        //Esta funcion la hace a trasfondo llamando a un metodo donde hace las comprobaciones si existe el usuario o no.
        protected Boolean doInBackground(String... params) {

            return existeUsuario(params[0]);
        }

        /**
         * Esta ultima funcion de la clase cogemos el resultado que nos devuelve booleano de si true existe o si es false no existe el usuario
         * de ahi , tratarlo con cada resultado.
         * @param result Devuelve un booleano que nos comprueba si existe o no existe apartir de ahi ya hacer cosas en cada caso.
         */
        protected void onPostExecute(Boolean result) {
            //Comprueba si el usuario existe , si es asi iniciara la ventana principal asignada a ese usuario logeado, sino marcara un mensaje de error al usuario
            if(result){
                MainActivity.usuario.clear();
                //Comprueba si esta checkeado el recuerdame y aquí guarda el texto introducido del usuario para guardarlo.
                if(guarda.isChecked()) {
                    MainActivity.usuario.add(user.getUsuario());
                    MainActivity.usuario.add(user.getPassword());
                    MainActivity.otras=true;
                }else{
                    MainActivity.otras=false;
                }
                MainActivity.primeraVez = true;
                /* Comprueba que la contraseña sea la correspondiente al usuario introducido para validarla si es asi entrará en la ventana principal mediante una funcion
                * externa para facilitar las cosas. Sino saldra un mensaje de error de contraseña para el usuario .
                */
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

    /**
     * En este funcion comprueba si existe el usuario o no mediante Soap.
     * Hace una peticion al web service para la consulta , especificada en las variables declaradas al principio. Una de ellas
     * obtenia el usuario mediante la consulta de base de datos.
     * @param param
     * @return Devuelve un boolean , si es true el usuario existe sino es que no esta en la base de datos y no existe.
     */
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

            // Elimina caracteres de control '%00'. Tambien elimina espacios a la hora de introducir la contraseña.
            String pass = resSoap.getPropertyAsString(2).replaceAll("\\W", "");
            user = new com.example.juanjo.rideapp.UsuarioDTO(Integer.valueOf(resSoap.getPropertyAsString(0)), resSoap.getPropertyAsString(1), pass, resSoap.getPropertyAsString(3),
                    resSoap.getPropertyAsString(4), resSoap.getPropertyAsString(5), resSoap.getPropertyAsString(6), resSoap.getPropertyAsString(7));

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }


        return result;
    }

    /**
     * Esta funcion a diferencia de la anterior goMainScreen iniciará a la ventana principal igual que la otra pero con
     * el usuario del registro mediante la base de datos de la aplicación.
     */
    public void iniciar(){
        usuarioGoogle = false;
        startActivity(new Intent(getBaseContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }



}
