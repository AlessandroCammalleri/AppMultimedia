package com.example.audiodronedetection.SVM;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audiodronedetection.R;
import com.example.audiodronedetection.SVM.interfaces.IPredict;
import com.example.audiodronedetection.SVM.interfaces.ITest;
import com.example.audiodronedetection.SVM.interfaces.ITrain;

import java.io.File;
import java.util.EmptyStackException;

import umich.cse.yctung.androidlibsvm.LibSVM;

public class SvmActivity extends AppCompatActivity implements IPredict, ITrain, ITest {

    private Button btnTrain = null;
    private Button btnTest = null;
    private Button btnPredict = null;
    private TextView tvResults = null;

    private TrainTask trainTask = null;
    private TestTask testTask = null;
    private PredictTask predictTask = null;
    private String appFolderPath = null;
    private String targetPath = null;
    private String datasetPath = null;
    public LibSVM svm = LibSVM.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svm);

        btnTrain = findViewById(R.id.btnTrain);
        btnTest = findViewById(R.id.btnTest);
        btnPredict = findViewById(R.id.btnPredict);
        tvResults = findViewById(R.id.tvResult);

        trainTask = new TrainTask(this, this::onTrainDone);
        testTask = new TestTask(this, this::onTestDone);
        predictTask = new PredictTask(this, this::onPredictionDone);

        appFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/svm/";
        targetPath = appFolderPath + "TargetFile.txt";
        datasetPath = appFolderPath + "dataTrain.txt";


        btnTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onPreExecute
                Toast.makeText(getApplicationContext(), "Training Started", Toast.LENGTH_LONG).show();
                trainTask.doTrain(svm, appFolderPath, datasetPath);
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onPreExecute
                Toast.makeText(getApplicationContext(), "Test Started", Toast.LENGTH_SHORT).show();
                testTask.doTest(svm, appFolderPath);
            }
        });

        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onPreExecute
                Toast.makeText(getApplicationContext(), "Making Prediction", Toast.LENGTH_SHORT).show();
                predictTask.doPredict(svm, appFolderPath);
            }
        });
    }

    @Override
    public void onPredictionDone(String _res) {
        tvResults.setText(_res);
        Toast.makeText(getApplicationContext(), "Prediction done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrainDone() {
        Toast.makeText(getApplicationContext(), "Train done", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTestDone() {
        Toast.makeText(getApplicationContext(), "Test done", Toast.LENGTH_SHORT).show();
    }
}