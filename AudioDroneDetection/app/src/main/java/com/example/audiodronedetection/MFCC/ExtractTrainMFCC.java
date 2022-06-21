package com.example.audiodronedetection.MFCC;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.example.JLibrosa.FileFormatNotSupportedException;
import com.example.JLibrosa.JLibrosa;
import com.example.JLibrosa.WavFileException;
import com.example.audiodronedetection.MFCC.interfaces.IExtraction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class ExtractTrainMFCC {

    private Activity activity = null;
    private IExtraction iExtraction = null;

    public ExtractTrainMFCC(Activity activity, IExtraction iExtraction){
        this.activity = activity;
        this.iExtraction = iExtraction;
    }

    public void doExtractionTrain(JLibrosa jLibrosa,String pathDrone, String pathNonDrone, String dataTrainPath){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //doInBackground
                File directoryDrones = new File(pathDrone);
                File directoryNonDrones = new File(pathNonDrone);
                File[] droneFiles = directoryDrones.listFiles();
                File[] nonDroneFiles = directoryNonDrones.listFiles();
                float[][] meanMFCCDronesMatrix = new float[droneFiles.length][];
                float[][] meanMFCCNonDronesMatrix = new float[nonDroneFiles.length][];

                int defaultSampleRate = -1;
                int defaultAudioDuration = 1;
                int nMFCC = 14;

                String path;

                //calcolo mfcc dei droni
                for (int i = 0; i < droneFiles.length; i++) {

                    path = pathDrone + "/" + droneFiles[i].getName();

                    try {
                        float[] audioFeatureValues = jLibrosa.loadAndRead(path, defaultSampleRate, defaultAudioDuration);

                        float[][] mfccValues = jLibrosa.generateMFCCFeatures(audioFeatureValues, defaultSampleRate, nMFCC);

                        float[] meanMFCCValues = jLibrosa.generateMeanMFCCFeatures(mfccValues, mfccValues.length, mfccValues[0].length);
                        meanMFCCDronesMatrix[i] = meanMFCCValues;

                        Log.d("tag", i + " arr: " + Arrays.toString(meanMFCCValues));
                    } catch (java.io.IOException ioException) {
                        System.out.println("ioException");
                    } catch (WavFileException fileException) {
                        System.out.println("fileException");
                    } catch (FileFormatNotSupportedException fileFormatNotSupportedException) {
                        System.out.println("fileFormatNotSupportedException");
                    }
                }
                Log.i("end", "train drones done");

                //calcolo mfcc dei non droni
                for (int i = 0; i < nonDroneFiles.length; i++) {

                    path = pathNonDrone + "/" + nonDroneFiles[i].getName();

                    try {
                        float[] audioFeatureValues = jLibrosa.loadAndRead(path, defaultSampleRate, defaultAudioDuration);

                        float[][] mfccValues = jLibrosa.generateMFCCFeatures(audioFeatureValues, defaultSampleRate, nMFCC);

                        float[] meanMFCCValues = jLibrosa.generateMeanMFCCFeatures(mfccValues, mfccValues.length, mfccValues[0].length);
                        meanMFCCNonDronesMatrix[i] = meanMFCCValues;

                        Log.d("tag", i + " arr: " + Arrays.toString(meanMFCCValues));
                    } catch (java.io.IOException ioException) {
                        System.out.println("ioException");
                    } catch (WavFileException fileException) {
                        System.out.println("fileException");
                    } catch (FileFormatNotSupportedException fileFormatNotSupportedException) {
                        System.out.println("fileFormatNotSupportedException");
                    }
                }
                Log.i("end", "train non drones done");

                try {
                    File myFile = new File(dataTrainPath);
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                    //scrittura mfcc dei droni su file-dataset
                    for (int k = 0; k < droneFiles.length; k++) {
                        myOutWriter.write("+1 ");
                        for (int l = 0; l < nMFCC; l++) {
                            String s = String.valueOf(l + 1) + ":" + String.valueOf(meanMFCCDronesMatrix[k][l]) + " ";
                            myOutWriter.write(s);
                        }
                        myOutWriter.write("\n");
                    }

                    // Scrittura mfcc dei non droni sul file-dataset
                    for (int a = 0; a < nonDroneFiles.length; a++) {
                        myOutWriter.write("-1 ");
                        for (int b = 0; b < nMFCC; b++) {
                            String s = String.valueOf(b + 1) + ":" + String.valueOf(meanMFCCNonDronesMatrix[a][b]) + " ";
                            myOutWriter.write(s);
                        }
                        myOutWriter.write("\n");
                    }

                    myOutWriter.close();
                    fOut.close();

                } catch (Exception e) {
                    e.printStackTrace();
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
