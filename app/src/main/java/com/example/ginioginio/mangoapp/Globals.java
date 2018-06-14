package com.example.ginioginio.mangoapp;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Dr_Gustavo on 06/02/2018.
 */

public class Globals {
    private static Globals instance;

    // Global variable
    private int tamano=250;
    private int lh=150;
    private int lv=150;
    private int separacion=40;



    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setData(int d){
        this.tamano=d;
    }
    public int getData(){
        return this.tamano;
    }
    public int getLh() {
        return lh;
    }

    public void setLh(int lh) {
        this.lh = lh;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getSeparacion() {
        return separacion;
    }

    public void setSeparacion(int separacion) {
        this.separacion = separacion;
    }



    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}
