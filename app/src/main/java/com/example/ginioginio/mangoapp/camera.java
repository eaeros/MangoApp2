package com.example.ginioginio.mangoapp;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private CameraBridgeViewBase mOpenCvCameraView;
    public Mat img=null,imgt=null;
    private EditText intCapturas;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        intCapturas = (EditText) findViewById(R.id.editText2);

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

        return img;
    }

    public void iniciar(View view){
        Thread serverThread = null;
        if(!intCapturas.getText().toString().equals("") && !intCapturas.getText().toString().equals("0")){
            serverThread = new Thread(new ServerThread());
            serverThread.start();
        }else{
            Log.d("OPENCV", "Error numero capturas");
        }

    }

    class ServerThread implements Runnable {

        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            path = sdf.format(new Date());
            int capturas = 0;
            String nombre;
            while (!Thread.currentThread().isInterrupted()) {
                capturas++;
                nombre = "Captura"+capturas+".png";
                Log.d("OPENCV", "Guardando "+nombre);
                guardar(nombre);
                try {
                    Thread.sleep(1000/Integer.parseInt(intCapturas.getText().toString()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (capturas == Integer.parseInt(intCapturas.getText().toString())){
                    break;
                }
            }
        }
    }

    public void guardar(String filename){
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
