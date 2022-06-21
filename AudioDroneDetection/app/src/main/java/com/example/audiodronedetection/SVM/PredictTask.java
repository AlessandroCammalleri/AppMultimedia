package com.example.audiodronedetection.SVM;

import android.app.Activity;
import android.os.Environment;

import com.example.audiodronedetection.SVM.interfaces.IPredict;

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
                        iPredict.onPredictionDone("result");
                    }
                });
            }
        }).start();
    }
}