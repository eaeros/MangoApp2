package com.example.ginioginio.mangoapp;

import java.io.File;
import 	java.util.Date;



public class ManejoArchivo {

    public File getArchivo(){

        File directorio = new File("sdcard/mangoapp");

        if(!directorio.exists()){
            directorio.mkdir();
        }


        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File imagen = new File(directorio,"app_mango.img");
        return  imagen;
    }
}
