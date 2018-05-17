package com.example.juanjo.rideapp.FTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;


public class FTPCargarImagen extends AsyncTask<String, Integer, Bitmap>  {
    private String usuarioID;
    private String ip =  "rideapp.somee.com";
    public static  String USUARIO = "jesus93";				//Almacena el usuario
    public static String PASS = "rideapp@M";			//Almacena la contraseña
    private FTPClient ftpClient;
    private Bitmap loadedImage;
    private Context mContext;

    public FTPCargarImagen(Context context) {
        mContext = context;
    }

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

    public void cargarImagen(){
        try {
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.changeWorkingDirectory("./www.rideapp.somee.com/Imagenes/");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2
            loadedImage = BitmapFactory.decodeStream(ftpClient.retrieveFileStream(usuarioID));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Bitmap doInBackground(String... usuarioID) {
        try {
            this.usuarioID = usuarioID[0];
            login();
            cargarImagen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedImage;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        //mContext.cambiarImagen(loadedImage);
        super.onPostExecute(bitmap);
    }
}
