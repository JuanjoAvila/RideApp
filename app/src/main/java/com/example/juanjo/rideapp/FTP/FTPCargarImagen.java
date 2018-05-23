package com.example.juanjo.rideapp.FTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * Clase encargada de gestionar mediante un hilo la descarga de una imagen para poderla cargar en la aplicación.
 * Para poder descargar la imagen necesita recibir un string, con el cual identificará la imagen del FTP que debe descargar
 * y devolverá un Bitmap con su contenido.
 */
public class FTPCargarImagen extends AsyncTask<String, Integer, Bitmap>  {
    private String imagenID;
    private String ip =  "rideapp.somee.com";
    public static  String USUARIO = "jesus93";				//Almacena el usuario
    public static String PASS = "rideapp@M";			//Almacena la contraseña
    private FTPClient ftpClient;
    private Bitmap loadedImage;
    private Context mContext;

    /**
     * Crea una instancia de FTPCargarImagen sin credenciales
     */
    public FTPCargarImagen(Context context) {
        mContext = context;
    }

    /**
     * Realiza el login en el servidor
     * @return	Verdad en caso de haber realizado login correctamente
     * @throws IOException
     */
    public boolean login () throws IOException {
        //Establece conexión con el servidor
        try{
            ftpClient = new FTPClient();
            ftpClient.connect(ip);
        }
        catch (Exception e){
            e.printStackTrace();
            //Informa al usuario
            return false;	//En caso de que no sea posible la conexion
        }
        //Hace login en el servidor
        ftpClient.enterLocalPassiveMode();

        if (ftpClient.login(USUARIO, PASS)){
            //Informa al usuario
            return true;	//En caso de login correcto
        }
        else{
            //Informa al usuario
            return false;	//En caso de login incorrecto
        }
    }

    /**
     * Pasamos a modo pasivo en el FTP, cambiamos el directorio a donde tenemos las imagenes guardadas y cargamos
     * con un stream el bitmap con los datos de la imagen correspondiente.
     */
    public void cargarImagen(){
        try {
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.changeWorkingDirectory("./www.rideapp.somee.com/Imagenes/");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // el factor de escala a minimizar la imagen, siempre es potencia de 2
            loadedImage = BitmapFactory.decodeStream(ftpClient.retrieveFileStream(imagenID), new Rect(0, 0, 0, 0), options);
        } catch (IOException e) {
            loadedImage = null;
        }
    }

    @Override
    protected Bitmap doInBackground(String... imagenID) {
        try {
            this.imagenID = imagenID[0];
            login();
            cargarImagen();
        } catch (IOException e) {
            return null;
        }
        return loadedImage;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }
}
