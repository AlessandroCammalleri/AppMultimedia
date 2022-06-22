/*
    Activity used to manage the SVM:
        - Train SVM:    Uses a dataset file to read MFCCs of 1082 drones and 1082 non-drones
                        and creates a model
        - Test SVM:     Uses a file with MFCCs of 250 drones and 250 non-drones to make a
                        prediction for each of them.
                        In this case we know which ones are drones and which ones not, so we can
                        know how many of them were predicted correctly.
                        (these 500 recordings were not used to train the SVM, otherwise we would
                        have a 100% accuracy)
        - Predict:      Uses a file with MFCCs of AudioRecording.wav, recorder with audiorecorder
                        activity, to predict if it's a recording of a drone or not.
                        Prediction results are saved in result.txt (1 = drone, -1 = non-drone)

    All source files have a sampling rate of 16kHz
 */

package com.example.audiodronedetection.SVM;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audiodronedetection.R;
import com.example.audiodronedetection.SVM.interfaces.IPredict;
import com.example.audiodronedetection.SVM.interfaces.ITest;
import com.example.audiodronedetection.SVM.interfaces.ITrain;

import umich.cse.yctung.androidlibsvm.LibSVM;

public class SvmActivity extends AppCompatActivity implements IPredict, ITrain, ITest {

    //Button and Textview definitions
    private Button btnTrain = null;
    private Button btnTest = null;
    private Button btnPredict = null;
    private TextView tvResults = null; //Shows result for predict and accuracy for testing

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

        btnTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onPreExecute
                Toast.makeText(getApplicationContext(), "Training Started", Toast.LENGTH_LONG).show();
                tvResults.setText("Training...");
                trainTask.doTrain(svm, appFolderPath); //Trains the SVM
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onPreExecute
                Toast.makeText(getApplicationContext(), "Test Started", Toast.LENGTH_SHORT).show();
                testTask.doTest(svm, appFolderPath); //Tests SVM accuracy
            }
        });

        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onPreExecute
                predictTask.doPredict(svm, appFolderPath); //Uses SVM to predict if Audiorecorder was a drone or not
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
        tvResults.setText("");
        Toast.makeText(getApplicationContext(), "Train done", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTestDone(String _res) {
        tvResults.setText(_res);
        Toast.makeText(getApplicationContext(), "Test done", Toast.LENGTH_SHORT).show();
    }

}