package com.example.juanjo.rideapp.Rutas;

import com.example.juanjo.rideapp.R;

/**
 * Created by jesus on 16/05/18.
 */

public enum Rutas_viewpager_enum {

    NUEVA_RUTA(R.string.nueva_ruta, R.layout.rutas_viewpager_nueva_ruta),
    CARGAR_RUTA(R.string.cargar_ruta, R.layout.rutas_viewpager_cargar_ruta),
    EMERGENCIAS(R.string.emergencias, R.layout.rutas_viewpager_emergencias);

    private int mTitleResId;
    private int mLayoutResId;

    Rutas_viewpager_enum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}