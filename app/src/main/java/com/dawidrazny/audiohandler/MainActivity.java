package com.dawidrazny.audiohandler;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Layout elements declarations
    private Button mRecordButton;
    private TextView mNotificationTextView;
    private Chronometer mChronometer;

    // Time variables
    private Boolean isRecording = false;

    int ChronometerTickCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        // home screen layout elements binding
        mRecordButton = (Button)findViewById(R.id.recordButton);
        mNotificationTextView = (TextView)findViewById(R.id.notificationTextView);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);

        // Record Button events ( start & record, pause and stop )
        mRecordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText( v.getContext() , "Recording started!", Toast.LENGTH_LONG).show();

                // Chronometer start counting time
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener()
                {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        if (ChronometerTickCounter == 0)
                        {
                            mNotificationTextView.setText("Recording.");
                        } else if (ChronometerTickCounter == 1)
                        {
                            mNotificationTextView.setText("Recording..");
                        } else if (ChronometerTickCounter == 2)
                        {
                            mNotificationTextView.setText("Recording...");
                            ChronometerTickCounter = -1;
                        }
                        ChronometerTickCounter++;
                    }
                });
            }
        });
    }
}
