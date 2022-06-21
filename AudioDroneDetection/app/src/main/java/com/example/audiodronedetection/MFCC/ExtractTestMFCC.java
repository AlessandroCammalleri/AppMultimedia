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

public class ExtractTestMFCC {

    private Activity activity = null;
    private IExtraction iExtraction = null;

    public ExtractTestMFCC(Activity activity, IExtraction iExtraction){
        this.activity = activity;
        this.iExtraction = iExtraction;
    }

    public void doExtractionTest(JLibrosa jLibrosa, String pathDroneTest, String pathNonDroneTest, String dataTestPath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //doInBackground
                File directoryDroneTests = new File(pathDroneTest);
                File directoryNonDroneTest = new File(pathNonDroneTest);
                File[] droneTestFiles = directoryDroneTests.listFiles();
                File[] nonDroneTestFiles = directoryNonDroneTest.listFiles();

                float[][] meanMFCCDroneTestsMatrix = new float[droneTestFiles.length][];
                float[][] meanMFCCNonDroneTestMatrix = new float[nonDroneTestFiles.length][];

                int defaultSampleRate = -1;
                int defaultAudioDuration = 1; //Seconds
                int nMFCC = 14; //Number of MFCCs extracted for each file

                String path;

                //Drone files MFCCs extraction for testing
                //These files are not used to train the SVM
                for (int i = 0; i < droneTestFiles.length; i++) {

                    path = pathDroneTest + "/" + droneTestFiles[i].getName();

                    try {
                        float[] audioFeatureValues = jLibrosa.loadAndRead(path, defaultSampleRate, defaultAudioDuration);

                        float[][] mfccValues = jLibrosa.generateMFCCFeatures(audioFeatureValues, defaultSampleRate, nMFCC);

                        float[] meanMFCCValues = jLibrosa.generateMeanMFCCFeatures(mfccValues, mfccValues.length, mfccValues[0].length);
                        meanMFCCDroneTestsMatrix[i] = meanMFCCValues;

                        Log.d("tag", i + " arr: " + Arrays.toString(meanMFCCValues));
                    } catch (java.io.IOException ioException) {
                        System.out.println("ioException");
                    } catch (WavFileException fileException) {
                        System.out.println("fileException");
                    } catch (FileFormatNotSupportedException fileFormatNotSupportedException) {
                        System.out.println("fileFormatNotSupportedException");
                    }
                }
                Log.i("end", "train drone test done");

                //Non-Drone files MFCCs extraction for testing
                //These files are not used to train the SVM
                for (int i = 0; i < nonDroneTestFiles.length; i++) {

                    path = pathNonDroneTest + "/" + nonDroneTestFiles[i].getName();

                    try {
                        float[] audioFeatureValues = jLibrosa.loadAndRead(path, defaultSampleRate, defaultAudioDuration);

                        float[][] mfccValues = jLibrosa.generateMFCCFeatures(audioFeatureValues, defaultSampleRate, nMFCC);

                        float[] meanMFCCValues = jLibrosa.generateMeanMFCCFeatures(mfccValues, mfccValues.length, mfccValues[0].length);
                        meanMFCCNonDroneTestMatrix[i] = meanMFCCValues;

                        Log.d("tag", i + " arr: " + Arrays.toString(meanMFCCValues));
                    } catch (java.io.IOException ioException) {
                        System.out.println("ioException");
                    } catch (WavFileException fileException) {
                        System.out.println("fileException");
                    } catch (FileFormatNotSupportedException fileFormatNotSupportedException) {
                        System.out.println("fileFormatNotSupportedException");
                    }
                }
                Log.i("end", "train non drone test done");

                //Test File creation. It will be used to test SVM
                try {
                    File myFile = new File(dataTestPath);
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                    //Drone files MFCCs writing in TestFile
                    for (int k = 0; k < droneTestFiles.length; k++) {
                        myOutWriter.write("+1 ");
                        for (int l = 0; l < nMFCC; l++) {
                            String s = String.valueOf(l + 1) + ":" + String.valueOf(meanMFCCDroneTestsMatrix[k][l]) + " ";
                            myOutWriter.write(s);
                        }
                        myOutWriter.write("\n");
                    }

                    //Non-Drone files MFCCs writing in TestFile
                    for (int a = 0; a < nonDroneTestFiles.length; a++) {
                        myOutWriter.write("-1 ");
                        for (int b = 0; b < 14; b++) {
                            String s = String.valueOf(b + 1) + ":" + String.valueOf(meanMFCCNonDroneTestMatrix[a][b]) + " ";
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
