package com.example.ginioginio.mangoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    private EditText varTxtNum;
    private View varBtnNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        varTxtNum = (EditText) findViewById(R.id.editTextNum);
        varBtnNum = (Button) findViewById(R.id.btnNum);

        varBtnNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pixNumber = varTxtNum.getText().toString();
                if (pixNumber != null && !pixNumber.isEmpty()) {
                    //pix =Integer.getInteger(pixNumber);
                    Toast.makeText(Settings.this,"caja de texto vale: "+pixNumber, Toast.LENGTH_SHORT).show();
                    Intent intend  = new Intent(Settings.this, camera.class);
                    //mandamos in inend con el id greeter y la variable GREETER
                    intend.putExtra("finalNumid", pixNumber);
                    startActivity(intend);
                }
            }
        });
    }
}
