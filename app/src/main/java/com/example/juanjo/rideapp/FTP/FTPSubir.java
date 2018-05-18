package com.example.juanjo.rideapp.FTP;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

public class FTPSubir extends AsyncTask<File , Integer, Boolean> {
    private static final int SELECT_FILE = 1;
    //Credenciales
    private String ip =  "rideapp.somee.com";
    public static  String USUARIO = "jesus93";				//Almacena el usuario
    public static String PASS = "rideapp@M";			//Almacena la contraseña

    FTPClient ftpClient;            //Crea la conexión con el servidor
    BufferedInputStream bufferIn;   //Crea una buffer de lectura
    BufferedOutputStream bufferOut; //Crea una buffer de escritura
    File rutaCompleta;                //Almacena la ruta completa del archivo
    Context context;                //Almacena el contexto de la aplicacion

    /**
     * Crea una instancia de FTPSubir sin credenciales
     */
    public FTPSubir(Context context) {

        //Inicialización de campos
        ftpClient = null;
        bufferIn = null;
        bufferOut = null;
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
            ftpClient.connect(ip);
        }
        catch (Exception e){
            e.printStackTrace();
            //Informa al usuario
            System.out.println("Imposible conectar . . .");
            return false;	//En caso de que no sea posible la conexion
        }
        //Hace login en el servidor
        ftpClient.enterLocalPassiveMode();

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
     *  Sube un archivo al servidor FTP si previamente se ha hecho login correctamente
     * @return	Verdad en caso de que se haya subido con éxito
     * @throws IOException
     */
    public boolean SubirArchivo () throws IOException{

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        //Cambia la carpeta Ftp
        if(rutaCompleta.getName().endsWith("gpx")){
            ftpClient.changeWorkingDirectory("./www.rideapp.somee.com/Rutas/");
        }
        else{
            ftpClient.changeWorkingDirectory("./www.rideapp.somee.com/Imagenes/");
        }
        //Informa al usuario
        System.out.println("Carpeta ftp cambiada . . .");

        //Crea un buffer hacia el servidor de subida
        bufferIn = new BufferedInputStream(new FileInputStream(rutaCompleta));

        if (ftpClient.storeFile(rutaCompleta.getName(), bufferIn)){
            //Informa al usuario
            System.out.println("Archivo subido . . .");
            bufferIn.close();		//Cierra el bufer
            return true;		//Se ha subido con éxito
        }
        else{
            //Informa al usuario
            System.out.println("Imposible subir archivo . . .");
            bufferIn.close();		//Cierra el bufer
            return false;		//No se ha subido
        }
    }

    @Override
    protected Boolean doInBackground(File... files) {
        try {
            for (int i = 0; i < files.length; i++){
                rutaCompleta = files[0];
            }
            login();
            SubirArchivo();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Imposible conectar . . .");
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //Termina proceso
        Log.i("TAG" , "Termina proceso de lectura de archivos.");
    }

}
