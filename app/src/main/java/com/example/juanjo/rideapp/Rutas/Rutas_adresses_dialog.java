package com.example.juanjo.rideapp.Rutas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.juanjo.rideapp.R;

/**
 * Created by jesus on 17/05/18.
 */

@SuppressLint("ValidFragment")
public class Rutas_adresses_dialog extends DialogFragment {

    public static String text;

    @SuppressLint("ValidFragment")
    public Rutas_adresses_dialog(String text) {
        this.text = text;
    }

    public static Rutas_adresses_dialog newInstance(String title, String text) {
        Rutas_adresses_dialog frag = new Rutas_adresses_dialog(text);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rutas_addresses_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView tv = view.findViewById(R.id.tv_rutas_farRoute);
        tv.setText(Html.fromHtml(this.text));

    }

    @Override
    public void onPause() {
        this.dismiss();
        super.onPause();
    }

}
