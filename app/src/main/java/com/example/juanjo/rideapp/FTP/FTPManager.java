package com.example.juanjo.rideapp.FTP;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class FTPManager {
    private Context mContext;

    public FTPManager(Context mContext) {
        this.mContext = mContext;
    }

    public Bitmap FTPCargarImagen(String usuarioID) throws ExecutionException, InterruptedException {
        FTPCargarImagen ftpCI = new FTPCargarImagen(mContext);
        Bitmap bitmap = ftpCI.execute(usuarioID).get();
        return bitmap;
    }

    public Boolean FTPDescargar(String file) throws ExecutionException, InterruptedException {
        FTPDescargar ftpD = new FTPDescargar(mContext);
        Boolean exito = ftpD.execute(file).get();
        return exito;
    }

    public Boolean FTPSubir(File fichero) throws ExecutionException, InterruptedException {
        FTPSubir ftpS = new FTPSubir(mContext);
        Boolean exito = ftpS.execute(fichero).get();
        return exito;
    }
}
