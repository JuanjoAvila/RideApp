package com.example.juanjo.rideapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Locale;

public class FTPDescargar extends AsyncTask<Void , Integer, Long> {
    //Credenciales
    private static String IP =  "rideapp.somee.com";
    public static  String USUARIO = "jesus93";				//Almacena el usuario
    public static String PASS = "rideapp@M";			//Almacena la contraseña

    FTPClient ftpClient;            //Crea la conexión con el servidor
    BufferedInputStream bufferIn;   //Crea una buffer de lectura
    BufferedOutputStream bufferOut; //Crea una buffer de escritura
    File rutaSd;                    //Almacena la ruta sd
    File rutaCompleta;                //Almacena la ruta completa del archivo
    Context context;                //Almacena el contexto de la aplicacion

    /**
     * Crea una instancia de FTP sin credenciales
     */
    public FTPDescargar(Context context) {

        //Inicialización de campos
        ftpClient = null;
        bufferIn = null;
        bufferOut = null;
        rutaSd = null;
        rutaCompleta = null;

        this.context = context;
    }

    /**
     * Realiza el login en el servidor
     * @return	Verdad en caso de haber realizado login correctamente
     * @throws SocketException
     * @throws IOException
     */
    public boolean login () throws IOException {
        //Establece conexión con el servidor
        System.out.println("Conectando . . .");
        try{
            ftpClient = new FTPClient();
            ftpClient.connect(IP);
        }
        catch (Exception e){
            e.printStackTrace();
            //Informa al usuario
            System.out.println("Imposible conectar . . .");
            return false;	//En caso de que no sea posible la conexion
        }
        //Hace login en el servidor

        if (ftpClient.login(USUARIO, PASS)){
            //Informa al usuario
            System.out.println("Login correcto . . .");
            return true;	//En caso de login correcto
        }
        else{
            //Informa al usuario
            System.out.println("Login incorrecto . . .");
            return false;	//En caso de login incorrecto
        }
    }

    /**
     *  Descarga un archivo al servidor FTP si previamente se ha hecho login correctamente
     * @param nombreArchivo		Nombre del archivo que se quiere bajar
     * @return	Verdad en caso de que se haya bajado con éxito
     * @throws IOException
     */
    public boolean BajarArchivo (String nombreArchivo) throws IOException{

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        //Cambia la carpeta Ftp
        if (ftpClient.changeWorkingDirectory("./www.rideapp.somee.com/Imagenes/")){
            System.out.println(ftpClient.printWorkingDirectory());
            //Informa al usuario
            System.out.println("Carpeta ftp cambiada . . .");

            //Obtiene la dirección de la ruta sd
            //rutaSd = Environment.getExternalStorageDirectory();
            System.out.println("Ruta SD obtenida . . .");
            // externalStorage
            String ExternalStorageDirectory = Environment.getExternalStorageDirectory() + File.separator;
            // uri de la imagen seleccionada
            Uri uri = Uri.fromFile(new File(ExternalStorageDirectory + "Download/imagenseleccionada.jpg"));
            //carpeta "imagenesguardadas"
            String rutacarpeta = "RideApp/";
            // nombre del nuevo png
            String nombre = nombreArchivo;
            // Compruebas si existe la carpeta "imagenesguardadas", sino, la crea
            File directorioImagenes = new File(ExternalStorageDirectory + rutacarpeta);
            if (!directorioImagenes.exists())
                directorioImagenes.mkdirs();

            //Obtiene la ruta completa donde se encuentra el archivo
            rutaCompleta = new File(ExternalStorageDirectory + rutacarpeta + nombre);
            System.out.println("Ruta completa archivo obtenida . . .");

            //Crea un buffer hacia el servidor de subida
            bufferOut = new BufferedOutputStream(new FileOutputStream(rutaCompleta));

            if (ftpClient.retrieveFile(nombreArchivo, bufferOut)){
                //Informa al usuario
                System.out.println("Archivo bajado . . .");
                bufferOut.close();		//Cierra el bufer
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Titulo");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción");
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
                values.put(MediaStore.Images.ImageColumns.BUCKET_ID, rutaCompleta.toString().toLowerCase(Locale.getDefault()).hashCode());
                values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, rutaCompleta.getName().toLowerCase(Locale.getDefault()));
                values.put("_data", rutaCompleta.getAbsolutePath());
                ContentResolver cr = context.getContentResolver();
                cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                return true;		//Se ha bajado con éxito
            }
            else{
                //Informa al usuario
                System.out.println("Imposible bajar archivo . . .");
                bufferOut.close();		//Cierra el bufer
                return false;		//No se ha subido
            }
        }
        else{

            //Informa al usuario
            System.out.println("Carpeta ftp imposible cambiar . . .");
            return false;		//Imposible cambiar de directo en servidor ftp
        }
    }
    @Override
    protected Long doInBackground(Void... voids) {
        try {
            login();
            BajarArchivo("default.png");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Imposible conectar . . .");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        //Termina proceso
        Log.i("TAG" , "Termina proceso de lectura de archivos.");
    }
}
