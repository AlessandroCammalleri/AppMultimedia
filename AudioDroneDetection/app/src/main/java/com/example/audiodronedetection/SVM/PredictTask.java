package com.example.audiodronedetection.SVM;

import android.app.Activity;

import com.example.audiodronedetection.SVM.interfaces.IPredict;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

import umich.cse.yctung.androidlibsvm.LibSVM;

public class PredictTask {

    private Activity activity = null;
    private IPredict iPredict = null;

    public PredictTask(Activity activity, IPredict iPredict){
        this.activity = activity;
        this.iPredict = iPredict;
    }

    public void doPredict(LibSVM svm, String appfolderpath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //doinbackground
                svm.predict(appfolderpath + "targetFile.txt " + appfolderpath + "model.txt " + appfolderpath + "result.txt");

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //onPostExecute
                        iPredict.onPredictionDone(readResult(appfolderpath + "result.txt"));
                    }
                });
            }
        }).start();
    }

    ////readResult reads resultFile wich contains prediction of Audiorecording
    private String readResult(String path){
        int result = 0;
        String _res;

        File file = new File(path);
        int length = (int) file.length();
        byte[] bytes = new byte[length];

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                in.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String contents = new String(bytes);
        try {
            result = NumberFormat.getInstance().parse(contents).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(result == -1)
        {
            _res = "Not a Drone";
        }
        else
        {
            _res = "It's a Drone";
        }
        return _res;
    }
}