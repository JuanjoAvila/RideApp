package com.example.juanjo.rideapp.FTP;

import android.content.Context;
import android.graphics.Bitmap;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * El FTPManager es la clase capaz de poder gestionar todas las funcionalidades que queremos definir del FTP,
 * como poder cargar una imagen del FTP en la aplicación, descargar un archivo, en nuestro caso serán rutas y la
 * subida de rutas y imagenes al FTP en sus correspondientes carpetas.
 */
public class FTPManager {
    private Context mContext;

    /**
     * Instancia del FTPManager con el contexto de la actividad que llamará a los métodos.
     * @param mContext
     */
    public FTPManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Crea una instancia de la clase FTPDescargar y la ejecuta.
     * @see FTPDescargar
     * @return True para cuando la descarga del fichero ha sido exitosa, False para lo contrario.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Boolean FTPDescargar(String file) throws ExecutionException, InterruptedException {
        FTPDescargar ftpD = new FTPDescargar(mContext);
        Boolean exito = ftpD.execute(file).get();
        return exito;
    }

    /**
     * * Crea una instancia de la clase FTPSubir y la ejecuta.
     * @see FTPSubir
     * @return True para cuando la carga del fichero ha sido exitosa, False para lo contrario.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Boolean FTPSubir(File fichero) throws ExecutionException, InterruptedException {
        FTPSubir ftpS = new FTPSubir(mContext);
        Boolean exito = ftpS.execute(fichero).get();
        return exito;
    }

    public Bitmap HTTPCargarImagen(String imagenID) throws ExecutionException, InterruptedException {
        HTTPCargarImagen httpCargarImagen = new HTTPCargarImagen(mContext);
        Bitmap bitmap = httpCargarImagen.execute(imagenID).get();
        return bitmap;
    }
}
