package com.example.juanjo.rideapp.Usuario;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.juanjo.rideapp.Login;
import com.example.juanjo.rideapp.R;
import com.example.juanjo.rideapp.DTO.UsuarioDTO;
import com.mvc.imagepicker.ImagePicker;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class EdicionPerfil extends AppCompatActivity {
    public static final String URL = "http://rideapp.somee.com/WebService.asmx";
    public static final String METHOD_NAME = "modificarUsuario";
    public static final String SOAP_ACTION = "http://tempuri.org/modificarUsuario";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private AutoCompleteTextView contraseña;
    private AutoCompleteTextView contraseña2;
    private AutoCompleteTextView nombre;
    private AutoCompleteTextView apellidos;
    private AutoCompleteTextView descripcion;
    private AutoCompleteTextView correo;
    private ImageView avatar;
    private String imagen_rutaBASE64;
    private UsuarioDTO usuarioEditado;
    private Editar_perfil_mail_dialog dialogMail;
    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_perfil);
        mContext = this;
        imagen_rutaBASE64 = Login.getUsuari().getAvatar();
        contraseña2 =findViewById(R.id.Edicion_perfil_contrasena_repetida);
        contraseña = findViewById(R.id.Edicion_perfil_contrasena);
        nombre = findViewById(R.id.Edicion_perfil_nombre);
        apellidos = findViewById(R.id.Edicion_perfil_apellidos);
        correo = findViewById(R.id.Edicion_perfil_correo);
        descripcion = findViewById(R.id.Edicion_perfil_descripcion);
        avatar = findViewById(R.id.Edicion_perfil_avatar);
        byte[] decodeValue = Base64.decode(imagen_rutaBASE64, Base64.DEFAULT);
        Bitmap bitmapAvatar = BitmapFactory.decodeByteArray(decodeValue, 0, decodeValue.length);
        avatar.setImageBitmap(bitmapAvatar);
    }

    public void atrasEdicion(View view){
        finish();
    }
    public void updateUsuario(View view) {
        usuarioEditado = Login.getUsuari();
        usuarioEditado.setAvatar(codificarImagenaBase64());
        if(contraseña.getText()!=null && !contraseña.getText().toString().isEmpty()){
            if(contraseña2.getText()!=null && !contraseña2.getText().toString().isEmpty()){
                if(contraseña.getText().toString().equals(contraseña2.getText().toString())){
                    usuarioEditado.setPassword(contraseña.getText().toString());
                }
                else{
                    contraseñasDiferentes();
                    return;
                }
            }
            else{
                contraseñasDiferentes();
                return;
            }
        }
        if(nombre.getText()!=null && !nombre.getText().toString().isEmpty()) {
            usuarioEditado.setNombre(nombre.getText().toString());
        }
        if(apellidos.getText()!=null && !apellidos.getText().toString().isEmpty()) {
            usuarioEditado.setApellidos(apellidos.getText().toString());
        }
        if(correo.getText()!=null && !correo.getText().toString().isEmpty()) {
            if(validarEmail(correo.getText().toString())) {
                usuarioEditado.setCorreo(correo.getText().toString());
            }
            else{
                formatoCorreoNoValido();
                return;
            }
        }
        if(descripcion.getText()!=null && !descripcion.getText().toString().isEmpty()) {
            usuarioEditado.setDescripcion(descripcion.getText().toString());
        }
        EditarUsuario editarUsuario = new EditarUsuario(mContext);
        editarUsuario.execute(usuarioEditado);
        this.finish();
    }

    public void cambiarAvatar(View view){
            alert(this, "Escoge una opción", "Haz una foto o selecciona una de tu galería",
                    "Cámara", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ContextCompat.checkSelfPermission(EdicionPerfil.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(EdicionPerfil.this, Manifest.permission.CAMERA)) {

                                } else {
                                    ActivityCompat.requestPermissions(EdicionPerfil.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                                }
                            } else {
                                abrirCamara();
                            }
                        }
                    },
                    "Galería", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                ImagePicker.pickImage(EdicionPerfil.this, "Selecciona la imagen:");
                            } catch (Exception e) {
                                Toast.makeText(EdicionPerfil.this, "Problemas al abrir la galería.\nUsa la cámara o prueba más tarde.", Toast.LENGTH_SHORT).show();
                                Log.e("LOG", "Erro: " + e);
                            }
                        }
                    });
        }

        private void abrirCamara() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        //  =================================================================================
        public static void alert(Context c, String t, String m, String msgBtnPositive, DialogInterface.OnClickListener cliqueOk, String msgBtnNegative, DialogInterface.OnClickListener cliqueNegative) {
            AlertDialog.Builder a = new AlertDialog.Builder(c);
            a.setTitle(t);
            a.setMessage(m);
            a.setPositiveButton(msgBtnPositive, cliqueOk);
            a.setNegativeButton(msgBtnNegative, cliqueNegative);
            a.show();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            //  camara
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    try {
                        avatar.setImageBitmap((Bitmap) extras.get("data"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //  galería
                try {
                    avatar.setImageBitmap(ImagePicker.getImageFromResult(this, requestCode, resultCode, data));
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        abrirCamara();
                    } else {

                    }
                    break;
                default:
                    break;
            }
        }

    private String codificarImagenaBase64() {
        Bitmap avatarBitmap = ((BitmapDrawable)avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = Bitmap.createScaledBitmap(avatarBitmap, 150, 150, false);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void contraseñasDiferentes(){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Contraseñas diferentes");
        dialogo1.setMessage("Las contraseñas no coinciden, por favor, escribir la misma contraseña.");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                return;
            }
        });
        dialogo1.show();
    }

    private void formatoCorreoNoValido() {
        dialogMail = null;
        FragmentManager fm = getSupportFragmentManager();
        dialogMail = Editar_perfil_mail_dialog.newInstance("Some Title");
        dialogMail.show(fm, "fragment_edit_name");
    }

    public void cerrarMailDialog(View view){
        if(dialogMail!=null){
            dialogMail.dismiss();
        }
    }

    public void cerrarPassDialog(View view){
        if(dialogMail!=null){
            dialogMail.dismiss();
        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    @SuppressLint("StaticFieldLeak")
    private class EditarUsuario extends AsyncTask<UsuarioDTO, Void, Integer> {

        private Context context;

        EditarUsuario(Context context) {
            this.context = context;
        }

        protected Integer doInBackground(UsuarioDTO... params) {

            Integer result = 0;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("usuario");
            pi.setValue(params[0]);
            pi.setType(params[0].getClass());

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "UsuarioDTO", params[0].getClass());

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                result = Integer.parseInt(response.toString());
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }


            return result;
        }

        protected void onPostExecute(Integer result) {
            if (result.equals(0)) {
                Toast.makeText(this.context, "No se ha podido modificar el usuario", Toast.LENGTH_LONG).show();
            } else {
                Login.setUsuario(usuarioEditado);
                Toast.makeText(this.context, "Se ha modificado correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }
}
