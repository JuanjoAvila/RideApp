package com.example.juanjo.rideapp.Rutas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ToggleButton;

import com.example.juanjo.rideapp.R;

/**
 * Created by jesus on 17/05/18.
 */

public class Rutas_guardar_dialog extends DialogFragment {

    public Rutas_guardar_dialog() {
    }

    public static Rutas_guardar_dialog newInstance(String title) {
        Rutas_guardar_dialog frag = new Rutas_guardar_dialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallBack = (CallBack) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rutas_guardar_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @Override
    public void onPause() {
        this.dismiss();
        super.onPause();
    }

    @Override
    public void onDetach() {
        mCallBack.onMyDialogFragmentDetached();

        super.onDetach();
    }

    public interface CallBack{
        public void onMyDialogFragmentDetached();
    }

    public CallBack mCallBack;
}
