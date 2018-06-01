package com.example.juanjo.rideapp.PickImage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

public class SelectorImagen {

    private Activity mContext;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public SelectorImagen(Activity mContext) {
        this.mContext = mContext;
    }

    public void takeImage() {
        alert(mContext, "Choose an option", "Open with camera or take from gallery",
                "Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.CAMERA)) {

                            } else {
                                //  LIBERA A PERMISSÃO SEM PEDIR NOVAMENTE
                                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                            }
                        } else {
                            openCamera();
                        }
                    }
                },
                "Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ImagePicker.pickImage(mContext, "Select your image:");
                        } catch (Exception e) {
                            Toast.makeText(mContext, "Problem to open the gallery.\nUse the camera or try later.", Toast.LENGTH_SHORT).show();
                            Log.e("LOG", "Erro: " + e);
                        }
                    }
                });
    }

    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null)
            mContext.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    public static void alert(Context c, String t, String m, String msgBtnPositive, DialogInterface.OnClickListener cliqueOk, String msgBtnNegative, DialogInterface.OnClickListener cliqueNegative) {
        AlertDialog.Builder a = new AlertDialog.Builder(c);
        a.setTitle(t);
        a.setMessage(m);
        a.setPositiveButton(msgBtnPositive, cliqueOk);
        a.setNegativeButton(msgBtnNegative, cliqueNegative);
        a.show();
    }


}
