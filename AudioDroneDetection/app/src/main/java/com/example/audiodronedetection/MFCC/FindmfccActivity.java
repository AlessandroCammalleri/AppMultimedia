package com.example.audiodronedetection.MFCC;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.JLibrosa.JLibrosa;
import com.example.audiodronedetection.MFCC.interfaces.IExtraction;
import com.example.audiodronedetection.R;

public class FindmfccActivity extends AppCompatActivity implements IExtraction {

    Button btnTrainMFCC = null;
    Button btnTestMFCC = null;
    Button btnRecorderMFCC = null;
    TextView tvInfoMFCC = null;

    ExtractTrainMFCC extractTrainMFCC = null;
    ExtractTestMFCC extractTestMFCC = null;
    ExtractRecorderMFCC extractRecorderMFCC = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findmfcc);

        btnTrainMFCC = findViewById(R.id.btnTrainMFCC);
        btnTestMFCC = findViewById(R.id.btnTestMFCC);
        btnRecorderMFCC = findViewById(R.id.btnRecorderMFCC);
        tvInfoMFCC = findViewById(R.id.tvInfoMFCC);

        JLibrosa jLibrosa = new JLibrosa();

        String pathDrone = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/dataset/yes_drone";
        String pathNonDrone = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/dataset/unknown";
        String pathDroneTest = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/dataset/yes_drone_test";
        String pathNonDroneTest = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/dataset/unknown_test";
        String recordingFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/AudioRecording.wav";

        String dataTestPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/svm/testFile.txt";
        String dataPredictPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/svm/targetFile.txt";
        String dataTrainPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/svm/dataTrain.txt";

        extractTrainMFCC = new ExtractTrainMFCC(this, this);
        extractTestMFCC = new ExtractTestMFCC(this, this);
        extractRecorderMFCC = new ExtractRecorderMFCC(this, this);

        btnTrainMFCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OnPreExecute
                tvInfoMFCC.setText("Extracting...");
                extractTrainMFCC.doExtractionTrain(jLibrosa, pathDrone, pathNonDrone, dataTrainPath);
            }
        });

        btnTestMFCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onPreExecute
                tvInfoMFCC.setText("Extracting...");
                extractTestMFCC.doExtractionTest(jLibrosa, pathDroneTest, pathNonDroneTest, dataTestPath);
            }
        });

        btnRecorderMFCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onPreExecute
                tvInfoMFCC.setText("Extracting...");
                extractRecorderMFCC.doExtractionRecorder(jLibrosa, recordingFilePath,dataPredictPath);
            }
        });
    }


    @Override
    public void onExtractionDone() {
        tvInfoMFCC.setText("Extraction finished");
    }
}