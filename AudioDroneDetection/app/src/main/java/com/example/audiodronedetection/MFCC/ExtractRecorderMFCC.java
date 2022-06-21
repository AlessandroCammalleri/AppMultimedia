package com.example.audiodronedetection.MFCC;

import android.app.Activity;
import android.util.Log;

import com.example.JLibrosa.FileFormatNotSupportedException;
import com.example.JLibrosa.JLibrosa;
import com.example.JLibrosa.WavFileException;
import com.example.audiodronedetection.MFCC.interfaces.IExtraction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class ExtractRecorderMFCC {

    private Activity activity = null;
    private IExtraction iExtraction = null;

    public ExtractRecorderMFCC(Activity activity, IExtraction iExtraction){
        this.activity = activity;
        this.iExtraction = iExtraction;
    }

    public void doExtractionRecorder (JLibrosa jLibrosa, String recorderFilePath, String predictFilePath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //doInBackground

                int defaultSampleRate = -1;
                int defaultAudioDuration = 1;
                int nMFCC = 14;

                try {
                    float[] audioFeatureValues = jLibrosa.loadAndRead(recorderFilePath, defaultSampleRate, defaultAudioDuration);

                    float[][] mfccTarget = jLibrosa.generateMFCCFeatures(audioFeatureValues, defaultSampleRate, nMFCC);

                    float[] meanMFCCTarget = jLibrosa.generateMeanMFCCFeatures(mfccTarget, mfccTarget.length, mfccTarget[0].length);

                    try {
                        File myFile = new File(predictFilePath);
                        myFile.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                        myOutWriter.write("+1 ");
                        for (int l = 0; l < 14; l++)
                        {
                            String s = String.valueOf(l + 1) + ":" + String.valueOf(meanMFCCTarget[l]) + " ";
                            myOutWriter.write(s);
                        }
                        myOutWriter.close();
                        fOut.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("tag", "arr: " + Arrays.toString(meanMFCCTarget));
                } catch (java.io.IOException ioException) {
                    System.out.println("ioException");
                } catch (WavFileException fileException) {
                    System.out.println("fileException");
                } catch (FileFormatNotSupportedException fileFormatNotSupportedException) {
                    System.out.println("fileFormatNotSupportedException");
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //onPostExecute
                        iExtraction.onExtractionDone();
                    }
                });
            }
        }).start();
    }
}
