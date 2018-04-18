package com.example.ginioginio.mangoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.Vector;

public class camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private CameraBridgeViewBase mOpenCvCameraView;
    private EditText varEditTxtNombre;
    private EditText varEditTxtTiempo;
    private EditText intCapturas;
    public Mat img=null,imgt=null;
    double avg1 = 0;
    double avg2 = 0;
    double avg3 = 0;
    double avg4 = 0;
    String path;
    String path2;
    String capturas_t;
    String nombrePrueba="";
    String Tiempo;
    double av1 = 0;
    double av2 = 0;
    double av3 = 0;
    double av4=0;
    public int caja;
    public int anchomedio;
    int puntoA;
    int puntoB;
    int puntoC;
    int puntoD;
    private CountDownTimer timer;
    boolean working =false;
    int temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //boton de regreso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        varEditTxtNombre = (EditText)findViewById(R.id.EditTextNombre);
        varEditTxtTiempo = (EditText)findViewById(R.id.EditTextTiempo);

        intCapturas = (EditText) findViewById(R.id.editText2);
        /*se quita la lectura de variables globales y se hace uso de shared preference
        Globals g = Globals.getInstance();
        caja =g.getData();
        puntoA =g.getLh();
        puntoB = g.getLv();
        puntoC = g.getLh()+caja+g.getSeparacion();
        puntoD = g.getLv();
        */
        SharedPreferences configuracionapp = getSharedPreferences("DatosConfiguracion", Context.MODE_PRIVATE);
        caja = configuracionapp.getInt("area",25);
        puntoA = configuracionapp.getInt("horizontal",55);
        puntoB = configuracionapp.getInt("vertical",60);
        puntoC = configuracionapp.getInt("separacion",16)+caja+puntoA;
        puntoD = puntoB;

         String Tiempo = varEditTxtTiempo.getText().toString();

        /*
        Bundle bundle =getIntent().getExtras();
        if (bundle != null){
            //int greeter = bundle.getInt("finalNumid");
            String greeter2 = bundle.getString("finalNumid");
            Toast.makeText(camera.this,"valor recibido"+(greeter2), Toast.LENGTH_SHORT).show();
            //txt.setText(greeter);
            //pix2 = greeter;
            //Toast.makeText(camera.this,"pix2 vale"+String.valueOf(pix2), Toast.LENGTH_SHORT).show();
            caja = Integer.parseInt(greeter2);
            Toast.makeText(camera.this,"caja ahora vale"+String.valueOf(caja), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(camera.this,"Esta vacio", Toast.LENGTH_LONG).show();
        }*/
    }
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("Log", "OpenCV loaded successfully");

                    System.loadLibrary("opencv_java3");

                    mOpenCvCameraView.setMaxFrameSize(176,144);

                    mOpenCvCameraView.enableView();

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }

    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OPENCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OPENCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        img = new Mat(height,width, CvType.CV_8UC4);
        imgt = new Mat(width,width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Core.transpose(inputFrame.rgba(), imgt);
        Imgproc.resize(imgt, img, img.size(), 0, 0, 0);
        Core.flip(img, img, 1);

        Vector<Mat> spl = new Vector<>(3);
        Core.split(img,spl);//ARGB

        img = spl.get(2);
        anchomedio = (img.width()/2);

        Imgproc.rectangle(img, new Point(puntoA, puntoB), new Point(puntoA+caja, puntoB+caja),new Scalar(0, 255, 0));
        Imgproc.rectangle(img, new Point(puntoC, puntoD), new Point(puntoC+caja, puntoD+caja),new Scalar(0, 255, 0));

        Rect rect1 = new Rect(puntoA, puntoB, caja, caja);
        Mat img1 = img.submat(rect1); //= new Mat(img, rect1);

        int size = (int) img1.total() * img1.channels();
        double[] buff;

        double sum1 = 0;
        for(int i = 0; i < img1.height(); i++)
        {
            for(int j = 0; j < img1.width(); j++){
                buff = img1.get(i,j);
                sum1  += buff[0];
            }

        }
        avg1 = sum1/size;

        Rect rect2 = new Rect(puntoC, puntoD, caja, caja);
        Mat img2 = img.submat(rect2); //= new Mat(img, rect1);
        int size2 = (int) img2.total() * img2.channels();
        double[] buff2;
        double sum2 = 0;
        for(int i = 0; i < img2.height(); i++)
        {
            for(int j = 0; j < img2.width(); j++){
                buff2 = img2.get(i,j);
                sum2  += buff2[0];
            }

        }
        avg2 = sum2/size2;

        avg3 = avg1/avg2;
        avg4 = avg2/avg1;
        //float alto = img.height();

        Imgproc.putText(img,"E: " + String.format(Locale.US,"%.0f",avg1), new Point(10,10), 1, 1, new Scalar(255, 0, 0, 255), 1);
        Imgproc.putText(img,"R: " + String.format(Locale.US,"%.0f",avg2), new Point((img.width()/2)+10,10), 1, 1, new Scalar(255, 0, 0, 255), 1);
        Imgproc.putText(img,"P: " + String.format(Locale.US,"%.2f",avg3), new Point((img.width()/2)-10,img.height()), 1, 1, new Scalar(255, 0, 0, 255), 1);
        //Imgproc.putText(img,"C: " + String.format(Locale.US,"%.0f",capturas_t), new Point((img.width())+10,img.height()), 1, 1, new Scalar(255, 0, 0, 255), 1);
        //Imgproc.putText(img,"H: " + String.format(Locale.US,"%.2f",alto), new Point((img.width()/2)-10,img.height()), 1, 1, new Scalar(255, 0, 0, 255), 2);

        return img;
    }

    public void iniciar(View view){
        Thread serverThread = null;
        nombrePrueba="";
        if(!intCapturas.getText().toString().equals("") && !intCapturas.getText().toString().equals("0")){
            capturas_t = intCapturas.getText().toString();
            intCapturas.setText("");
            nombrePrueba = varEditTxtNombre.getText().toString();
            varEditTxtNombre.setText("");
            Tiempo = varEditTxtTiempo.getText().toString();
            varEditTxtTiempo.setText("");

            serverThread = new Thread(new ServerThread());
            serverThread.start();
            int denomidor2 = Integer.parseInt(Tiempo)*1000;
            Toast.makeText(getApplicationContext(),"Análisis iniciado, por favor espere" , Toast.LENGTH_SHORT).show();


            /*AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setMessage("Capturas almacenadas en /sdcard/mangoApp/ ")
                    .setPositiveButton("Continuar..", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Análisis terminado")
                    .setIcon(R.drawable.ic_menu_camera)
                    .create();
            alerta.show();*/

        }else{
            Log.d("OPENCV", "Error numero capturas");
        }

    }

    public void guardarTxt(String data) {
        try {

            File directorio = new File("sdcard/mangoApp");

            if(!directorio.exists()){
                directorio.mkdir();
            }

            File myFile = new File("sdcard/mangoApp/"+path+"/resultados.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();
            //Toast.makeText(camera.this,"se creo con exito la carpeta", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("OPENCV", "Error txt");
        }
    }

    class ServerThread implements Runnable {

        public void run() {
            SimpleDateFormat fecha =new SimpleDateFormat("ddMMyyyy_HH:mm:ss");
             path = fecha.format(new Date());
            if ((nombrePrueba != null) && !nombrePrueba.isEmpty()){
                //path =nombrePrueba +"|"+path;
                path =nombrePrueba +"";
            }



            int capturas = 0;
            String nombre;
            String txt="";
            av1 = 0;
            av2 = 0;
            av3 = 0;
            av4=0;
            int denomidor = Integer.parseInt(Tiempo)*1000;
            //while (!Thread.currentThread().isInterrupted()) {
            while (capturas<=(Integer.parseInt(capturas_t))) {

                SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
                String path2 = hora.format(new Date());
                capturas++;
                nombre = "Captura"+capturas+".png";
                Log.d("OPENCV", "Guardando "+nombre);
                guardarImagen(nombre);
                txt = txt + (capturas) + path + "|"+path2+"-"+String.format(Locale.US,"%.2f",avg1) + "-"+String.format(Locale.US,"%.2f",avg2) + "-"+String.format(Locale.US,"%.2f",avg3) + "\n";
                av1 += avg1;
                av2 += avg2;
                av3 += avg3;
                av4 +=avg4;



                try {
                    Thread.sleep(denomidor);
                    //Thread.sleep(3000);
                    //Thread.sleep(1000*Integer.parseInt(Tiempo));
                    //Toast.makeText(camera.this,"se hizo la toma numero: "+ String.valueOf(varEditTxtTiempo) , Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (capturas == Integer.parseInt(capturas_t)){
                    av1 = av1/capturas;
                    av2 = av2/capturas;
                    av3 = av3/capturas;
                    av4 =av4/capturas;
                    txt = txt + (capturas+1) + path + "-promedio E:  "+String.format(Locale.US,"%.2f",av1) + "-promedio R: "+String.format(Locale.US,"%.2f",av2) + "-E/R: "+String.format(Locale.US,"%.2f",av3)+ "-R/E: "+String.format(Locale.US,"%.2f",av4)  +"\n";
                    guardarTxt(txt);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"analisis terminado" , Toast.LENGTH_LONG).show();
                            AlertDialog.Builder alerta = new AlertDialog.Builder(camera.this);
                            alerta.setMessage("Capturas almacenadas en /mangoApp/ ")
                                    .setPositiveButton("Continuar..", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                            dialog.dismiss();
                                        }
                                    })
                                    .setTitle("Análisis terminado")
                                    .setIcon(R.drawable.ic_menu_camera)
                                    .create();
                            alerta.show();

                        }
                    });


                    break;
                }
            }
        }
    }


    public void guardarImagen(String filename){
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img, bmp);
        } catch (CvException e) {
            Log.d("OPENCV", e.getMessage());
        }


        FileOutputStream out = null;

        //File sd = new File(Environment.getExternalStorageDirectory() + "/"+path);
        File sd = new File("sdcard/mangoApp/"+path);
        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
        }
        if (success) {
            File dest = new File(sd, filename);

            try {
                out = new FileOutputStream(dest);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("OPENCV", e.getMessage());
            } finally {
                try {
                    if (out != null) {
                        out.close();
                        Log.d("OPENCV", "OK!!");
                    }
                } catch (IOException e) {
                    Log.d("OPENCV", e.getMessage() + "Error");
                    e.printStackTrace();
                }
            }
        }else{
            Log.d("OPENCV", "No sd");

        }
    }
}
