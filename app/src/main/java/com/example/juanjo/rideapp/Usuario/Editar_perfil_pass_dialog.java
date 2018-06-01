package com.example.juanjo.rideapp.Usuario;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.juanjo.rideapp.R;

/**
 * @author RideApp
 * @version Final
 * Diálogo utilizado para informar al usuario que no puede continuar sin señal GPS
 */
public class Editar_perfil_pass_dialog extends DialogFragment {

    public Editar_perfil_pass_dialog() {
    }

    public static Editar_perfil_pass_dialog newInstance(String title) {
        Editar_perfil_pass_dialog frag = new Editar_perfil_pass_dialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.editar_perfil_passincorrecto, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
}
