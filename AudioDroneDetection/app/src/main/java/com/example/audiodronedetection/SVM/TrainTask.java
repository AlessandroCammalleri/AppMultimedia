package com.example.audiodronedetection.SVM;

import android.app.Activity;

import com.example.audiodronedetection.SVM.interfaces.ITrain;

import umich.cse.yctung.androidlibsvm.LibSVM;

public class TrainTask {

    private Activity activity = null;
    private ITrain iTrain = null;

    public TrainTask(Activity activity, ITrain iTrain){
        this.activity = activity;
        this.iTrain = iTrain;
    }

    public void doTrain(LibSVM svm, String appfolderpath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //doinbackground
                svm.train("-t 1 -d 2 "/* svm kernel: quadratic polynomial */ + appfolderpath + "dataTrain.txt " + appfolderpath + "model.txt");

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //onPostExecute
                        iTrain.onTrainDone();
                    }
                });
            }
        }).start();
    }
}
