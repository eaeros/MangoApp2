package com.example.ginioginio.mangoapp;

import android.content.Context;
import android.graphics.Bitmap;
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
import java.util.Vector;

public class camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private CameraBridgeViewBase mOpenCvCameraView;
    public Mat img=null,imgt=null;
    double avg1 = 0;
    double avg2 = 0;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);


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

        Imgproc.rectangle(img, new Point(10, 40), new Point((img.width()/2)-10, img.height()-40),new Scalar(0, 255, 0));
        Imgproc.rectangle(img, new Point((img.width()/2)+10, 40), new Point((img.width())-10, img.height()-40),new Scalar(0, 255, 0));

        Rect rect1 = new Rect(10, 40, (img.width()/2)-10, img.height()-40);
        Mat img1 = img.submat(rect1); //= new Mat(img, rect1);

        int size = (int) img1.total() * img1.channels();
        double[] buff;

        avg1 = 0;
        for(int i = 0; i < img1.height(); i++)
        {
            for(int j = 0; j < img1.width(); j++){
                buff = img1.get(i,j);
                avg1  += buff[0];
            }

        }
        avg1 = avg1/size;

        Rect rect2 = new Rect((img.width()/2)+10, 40, (img.width()/2)-10, img.height()-40);
        Mat img2 = img.submat(rect2); //= new Mat(img, rect1);

        avg2 = 0;
        for(int i = 0; i < img2.height(); i++)
        {
            for(int j = 0; j < img2.width(); j++){
                buff = img2.get(i,j);
                avg2  += buff[0];
            }

        }
        avg2 = avg2/size;

        Imgproc.putText(img, String.format(Locale.US,"%.2f",avg1), new Point(10,10), 1, 1, new Scalar(255, 0, 0, 255), 2);
        Imgproc.putText(img, String.format(Locale.US,"%.2f",avg2), new Point((img.width()/2)+10,10), 1, 1, new Scalar(255, 0, 0, 255), 2);

        return img;
    }

    public void iniciar(View view){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        path = sdf.format(new Date());
        String txt = path + "|"+ String.format(Locale.US,"%.2f",avg1) + "|"+ String.format(Locale.US,"%.2f",avg2) + "\n";
        String nombre;
        nombre = path+".png";
        Log.d("OPENCV", "Guardando "+nombre);
        int result = guardarImagen(nombre);
        if(result == 1){
            if(guardarTxt(txt) == 1){
                Log.d("OPENCV", "Se guardo image.... txt.... "+nombre);
            }else{
                Log.d("OPENCV", "Se guardo image.... error txt "+nombre);
            }
        }else{
            Log.d("OPENCV", "Error image "+nombre);
        }
    }

    public int guardarTxt(String data) {
        try {
            File myFile = new File("sdcard/mangoApp/data.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int guardarImagen(String filename){
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img, bmp);
        } catch (CvException e) {
            Log.d("OPENCV", e.getMessage());
            return  2;//error
        }


        FileOutputStream out = null;

        //File sd = new File(Environment.getExternalStorageDirectory() + "/"+path);
        File sd = new File("sdcard/mangoApp");
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
                return 2;//error
            } finally {
                try {
                    if (out != null) {
                        out.close();
                        Log.d("OPENCV", "OK!!");
                        return 1;//Guardado
                    }
                } catch (IOException e) {
                    Log.d("OPENCV", e.getMessage() + "Error");
                    e.printStackTrace();
                    return 2; //error
                }
            }
        }else{
            Log.d("OPENCV", "No sd");
            return 0;//No sd
        }
        return 2;
    }
}
