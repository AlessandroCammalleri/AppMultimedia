package com.example.audiodronedetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //Buttons Definition
    Button bttDoRec = null;
    Button bttMFCC = null;
    Button bttLaunchSvm= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttDoRec = findViewById(R.id.bttDoRec);
        bttMFCC = findViewById(R.id.bttMFCC);
        bttLaunchSvm = findViewById(R.id.bttLaunchSvm);
        String svmPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/svm";
        String datasetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroneDetection/dataset";

        //App Folder Creation
        File FsvmPath = new File(svmPath);
        if (!FsvmPath.exists()) {
            if (!FsvmPath.mkdir()) {
                System.out.println("***Problem creating Image folder " + svmPath );
            }
        }

        File FdatasetPath = new File(datasetPath);
        if (!FdatasetPath.exists()) {
            if (!FdatasetPath.mkdir()) {
                System.out.println("***Problem creating Image folder " + datasetPath );
            }
        }

        bttDoRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getString(R.string.LAUNCH_RECORDER));
                startActivity(intent);
            }
        });
        bttMFCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getString(R.string.LAUNCH_MFCC));
                startActivity(intent);
            }
        });
        bttLaunchSvm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getString(R.string.LAUNCH_SVM));
                startActivity(intent);
            }
        });
    }
}