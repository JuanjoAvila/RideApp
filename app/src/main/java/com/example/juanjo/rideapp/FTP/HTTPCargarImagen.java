package com.example.juanjo.rideapp.FTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPCargarImagen extends AsyncTask<String, Integer, Bitmap> {
    URL imageUrl = null;
    HttpURLConnection conn = null;
    private String imageHttpAddress = "";
    private Context mContext;
    private Bitmap loadedImage;


    public HTTPCargarImagen(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Bitmap doInBackground(String... imgUrl) {
            try {
                imageUrl = new URL(imgUrl[0]);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                loadedImage = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
            } catch (IOException e) {
               e.printStackTrace();
            }

        return loadedImage;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }
}
