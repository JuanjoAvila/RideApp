package com.example.juanjo.rideapp.Rutas;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jesus on 16/05/18.
 */

public class Rutas_page_adapter extends PagerAdapter {

    private Context mContext;

    public Rutas_page_adapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Rutas_viewpager_enum rutasviewpagerenum = Rutas_viewpager_enum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(rutasviewpagerenum.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return Rutas_viewpager_enum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Rutas_viewpager_enum customPagerEnum = Rutas_viewpager_enum.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

}