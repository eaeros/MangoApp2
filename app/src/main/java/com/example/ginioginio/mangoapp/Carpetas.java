package com.example.ginioginio.mangoapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Carpetas extends AppCompatActivity {
    ListView listView ;
    private List listaNombresArchivos;
    private List listaRutasArchivos;
    private ArrayAdapter adaptador;
    private String directorioRaiz;
    private TextView carpetaActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpetas);
        //boton de regreso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listaFiles);
        // Defined Array values to show in ListView
        String fileList[] = new File("sdcard/mangoApp/").list();
        if(fileList == null){
            return;
        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, fileList);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert 
                Toast.makeText(getApplicationContext(),"Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //Uri uri = Uri.parse("sdcard/mangoApp/"); // a directory
                //intent.setDataAndType(uri, "*/*");
                //startActivity(Intent.createChooser(intent, "Open folder"));



                //Intent intentExplorer = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE, Uri.parse("txt"));
               //startActivity(Intent.createChooser(intentExplorer,"Elige una app"));

                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "");
                //intent.setDataAndType(uri, "text/csv");
                //startActivity(Intent.createChooser(intent, "Open folder"));
                //intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("*/*");
                //startActivityForResult(intent, 7);


            }
        });
    }


    }

