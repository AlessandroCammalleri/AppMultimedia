package com.example.audiodronedetection.SVM;

import android.app.Activity;

import com.example.audiodronedetection.SVM.interfaces.ITest;

import umich.cse.yctung.androidlibsvm.LibSVM;

public class TestTask {

    private Activity activity = null;
    private ITest iTest = null;

    public TestTask(Activity activity, ITest iTest){
        this.activity = activity;
        this.iTest = iTest;
    }

    public void doTest(LibSVM svm, String appfolderpath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //doinbackground
                svm.predict(appfolderpath + "testFile.txt " + appfolderpath + "model.txt " + appfolderpath + "result.txt");

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //onPostExecute
                        iTest.onTestDone();
                    }
                });
            }
        }).start();
    }
}
