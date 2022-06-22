package com.example.audiodronedetection.SVM;

import android.app.Activity;

import com.example.audiodronedetection.SVM.interfaces.ITest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

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
                        iTest.onTestDone(readResult(appfolderpath + "result.txt"));
                    }
                });
            }
        }).start();
    }

    //readResult reads resultFile wich contains predictions of 500 drones and non drones,
    //extracts each of them and checks if prediction was correct or not.
    //Finally it computes the percentage of correct guesses
    private String readResult(String path){

        File file = new File(path);
        int[] results = new int[500];
        int correctGuesses = 0;
        String percentage = null;

        FileInputStream in = null;
        BufferedReader reader = null;

        //Streamreader creation
        try {
            in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            try {
                String line;
                int i = 0;

                //Prediction reading (1 prediction per line)
                while((line = reader.readLine()) != null){
                    results[i] = Integer.parseInt(line);
                    i++;
                }
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

        //Check for how many predictions were correct
        for(int j = 0; j < results.length / 2; j++){
            if(results[j] == 1)
                correctGuesses++;
        }
        for(int j = results.length / 2; j < results.length; j++){
            if(results[j] == -1)
                correctGuesses++;
        }
        //Computing percentage of correct predictions
        percentage = String.valueOf((float) (((float)correctGuesses / (float)results.length) * 100));
        return "Accuracy: " + percentage + "% (" + String.valueOf(correctGuesses) + "/" + String.valueOf(results.length) + ")";
    }
}
