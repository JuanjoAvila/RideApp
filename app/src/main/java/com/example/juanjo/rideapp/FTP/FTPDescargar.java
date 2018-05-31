package com.example.juanjo.rideapp.FTP;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

public class FTPDescargar extends AsyncTask<String , Integer, Boolean> {
    //Credenciales
    private static String IP =  "rideapp2.somee.com";
    public static  String USUARIO = "rideapp";				//Almacena el usuario
    public static String PASS = "rideapp123A";			//Almacena la contraseña

    FTPClient ftpClient;            //Crea la conexión con el servidor
    Context mContext;                //Almacena el contexto de la aplicacion

    /**
     * Crea una instancia de FTP sin credenciales
     */
    public FTPDescargar(Context context) {

        //Inicialización de campos
        ftpClient = null;

        this.mContext = context;
    }

    /**
     * Realiza el login en el servidor
     * @return	Verdad en caso de haber realizado login correctamente
     * @throws SocketException
     * @throws IOException
     */
    public boolean login () throws IOException {
        //Establece conexión con el servidor
        try{
            ftpClient = new FTPClient();
            ftpClient.connect(IP);
        }
        catch (Exception e){
            e.printStackTrace();
            return false;	//En caso de que no sea posible la conexion
        }
        //Hace login en el servidor

        if (ftpClient.login(USUARIO, PASS)){
            return true;	//En caso de login correcto
        }
        else{
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
        if (ftpClient.changeWorkingDirectory("./www.rideapp.somee.com/Rutas/")){
            System.out.println(ftpClient.printWorkingDirectory());

            //Genera fichero local.
            File fichero = new File(mContext.getFilesDir() + "/" + nombreArchivo);
            System.out.println(fichero.getAbsolutePath());
            //Crea un buffer hacia el servidor de subida
            FileOutputStream outputStream = new FileOutputStream(fichero);

            //Descarga el fichero y vuelca el array de bytes en el fichero local.
            if (ftpClient.retrieveFile(nombreArchivo, outputStream)){
                System.out.println("Descarga hecha");
                outputStream.close();		//Cierra el bufer
                return true;		//Se ha bajado con éxito
            }
            else{
                outputStream.close();
                return false;		//No se ha subido
            }
        }
        else{
            return false;		//Imposible cambiar de directo en servidor ftp
        }
    }
    @Override
    protected Boolean doInBackground(String... ruta) {
        try {
            login();
            BajarArchivo(ruta[0]);
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