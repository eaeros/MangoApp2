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
    private EditText varTxtH;
    private EditText varTxtV;
    private EditText varTxtS;
    private boolean flag=false;
    Globals g = Globals.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        varTxtNum = (EditText) findViewById(R.id.editTextNum);
        varBtnNum = (Button) findViewById(R.id.btnNum);
        varTxtH = (EditText)findViewById(R.id.editTextHorizontal);
        varTxtV =(EditText)findViewById(R.id.editTextVertical);
        varTxtS = (EditText)findViewById(R.id.editTextseparacion);

        varBtnNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = varTxtNum.getText().toString();
                String h2 = varTxtH.getText().toString();
                String v2 =varTxtV.getText().toString();
                String s2 =varTxtS.getText().toString();


                if (area != null && !area.isEmpty()) {
                    //pix =Integer.getInteger(pixNumber);
                    int global = Integer.parseInt(area);
                    if(global>=2 && global<=50){
                        g.setData(global);
                        flag=true;
                    } else {
                        Toast.makeText(Settings.this,"Area fuera de rango", Toast.LENGTH_SHORT).show();
                    }

                    //mandamos in inend con el id greeter y la variable GREETER
                    //intend.putExtra("finalNumid", pixNumber);
                }
                if (h2 != null && !h2.isEmpty()){
                    int h3 = Integer.parseInt(h2);
                    if ((h3>=1 && h3<=100)&&((h3+g.getData()+g.getData()+g.getSeparacion())<=176)){
                        g.setLh(h3);
                        flag=true;
                    } else{
                        Toast.makeText(Settings.this,"Limite Horizontal fuera de rango", Toast.LENGTH_SHORT).show();
                    }

                }
                if (v2 != null && !v2.isEmpty()){
                    int v3 = Integer.parseInt(v2);
                    if((v3>=1 && v3<=100)&&(v3+g.getData()<=144)){
                        g.setLv(v3);
                        flag=true;
                    } else {
                        Toast.makeText(Settings.this,"Limite Vertical fuera de rango", Toast.LENGTH_SHORT).show();
                    }


                }
                if (s2 != null && !s2.isEmpty()){
                    int s3 = Integer.parseInt(s2);
                    if((s3>=1 && s3<=100)&&((g.getLh()+g.getData()+g.getData()+s3)<=176)){
                        g.setSeparacion(s3);
                        flag=true;
                    } else {
                        Toast.makeText(Settings.this,"Separación fuera de rango", Toast.LENGTH_SHORT).show();
                    }

                }
                if (flag){
                    Intent intend  = new Intent(Settings.this, MenuPrincipal.class);
                    startActivity(intend);
                    Toast.makeText(Settings.this,"cambios realizados con exito", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Settings.this,"No se realizo ningun cambio", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
