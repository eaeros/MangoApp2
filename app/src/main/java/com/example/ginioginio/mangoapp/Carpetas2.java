package com.example.ginioginio.mangoapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;

import com.example.ginioginio.mangoapp.FileChooser;


public class Carpetas2 extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpetas2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        File myFile = new File("your any type of file url");

        try {
            FileChooser.openFile(mContext, myFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}


