package com.dawidrazny.audiohandler;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Layout elements declarations
    private Button mRecordButton;
    private TextView mNotificationTextView;
    private Chronometer mChronometer;

    // Time variables
    private Boolean isRecording = false;
    int ChronometerTickCounter = 0;

    // file data variables
    String recordLenght = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        // home screen layout elements binding
        mRecordButton = (Button) findViewById(R.id.recordButton);
        mNotificationTextView = (TextView) findViewById(R.id.notificationTextView);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);

        // Record Button events ( start & record, pause and stop )
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(isRecording);
            }
        });
    }

    private void onRecord(boolean event)
    {
        // initialization of Recording Service
        Intent intent = new Intent( this.getApplicationContext(), audioRecordService.class);

        //record
        if(!event)
        {
            Toast.makeText(this, "Recording started!", Toast.LENGTH_LONG).show();

            // Chronometer start counting time
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            // changing a text in notification textview
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener()
            {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    if (ChronometerTickCounter == 0) {
                        mNotificationTextView.setText("Recording.");
                    } else if (ChronometerTickCounter == 1) {
                        mNotificationTextView.setText("Recording..");
                    } else if (ChronometerTickCounter == 2) {
                        mNotificationTextView.setText("Recording...");
                        ChronometerTickCounter = -1;
                    }
                    ChronometerTickCounter++;
                }
            });

            // Trigering recording event
            isRecording = true;

            startService(intent);
        }
        // stop recording
        else if(event)
        {
            Toast.makeText(this, "Recording stoped!", Toast.LENGTH_LONG).show();

            recordLenght = mChronometer.getText().toString();

            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());

            mNotificationTextView.setText("Recording finished.");

            //trigering recording event
            isRecording = false;
        }
    }

}
