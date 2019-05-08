package com.ewelinakuzmicka.audiohandler;

import android.content.Intent;
import android.graphics.Movie;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.os.FileObserver;
import android.os.SystemClock;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Layout elements declarations
    private Button mRecordButton;
    private TextView mNotificationTextView;
    private Button refreshButton;
    private Chronometer mChronometer;
    private RecyclerView mAudioRecyclerView;

    // Time variables
    private Boolean isRecording = false;
    int ChronometerTickCounter = 0;

    // file data variables
    String audioPath = Environment.getExternalStorageDirectory().toString()+"/AudioHandler";
    String recordLenght = "";

    // Audio List Variables
    audioListAdapter mAudioListAdapter;
    private List<fileItem> mFileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        // home screen layout elements binding
        mRecordButton = (Button) findViewById(R.id.recordButton);
        mNotificationTextView = (TextView) findViewById(R.id.notificationTextView);
        refreshButton = (Button) findViewById(R.id.refreshButton);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);

        // RecyclerViewer adapter
        mAudioRecyclerView = (RecyclerView) findViewById(R.id.audioRecyclerView);
        mAudioRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        mAudioRecyclerView.setLayoutManager(llm);

        // Add Adapter to Recycle viewer
        mAudioListAdapter = new audioListAdapter(this, llm, mFileList, MainActivity.this);
        mAudioRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAudioRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAudioRecyclerView.setAdapter(mAudioListAdapter);

        prepareFileListData();

        // Record Button events ( start & record, pause and stop )
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(isRecording);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mFileList.clear();
                prepareFileListData();
            }
        });

    }

    private void prepareFileListData()
    {

        File directory = new File(audioPath);
        final File[] sortedByDate = directory.listFiles();

        if (sortedByDate != null && sortedByDate.length > 1)
        {
            Arrays.sort(sortedByDate,  new Comparator<File>()
            {
                public int compare(File f1, File f2)
                {
                    return -Long.compare(f1.lastModified(), f2.lastModified());
                }
            });
        }

        for (int i = 0; i < sortedByDate.length; i++)
        {

            fileItem fileItem = new fileItem( Integer.toString(sortedByDate.length-i) , sortedByDate[i].getName() , sortedByDate[i].getPath(), getFileDuration(sortedByDate[i]), getFileDurationMili(sortedByDate[i]) );

            mFileList.add(fileItem);

            Log.d("Files", "FileName:" + sortedByDate[i].getName());

            mAudioListAdapter.notifyDataSetChanged();
        }
    }

    private static int getFileDurationMili(File file)
    {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Integer.parseInt(durationStr);
    }

    private static String getFileDuration(File file)
    {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return formatMilliSeccond(Long.parseLong(durationStr));
    }

    public static String formatMilliSeccond(long milliseconds)
    {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }

    private void onRecord(boolean event)
    {
        // initialization of Recording Service
        Intent intent = new Intent( getApplicationContext(), audioRecordService.class);

        //record
        if(!event)
        {
            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show();

            // Create folder for keeping audio files
            File audioFolder = new File(audioPath);
            if (!audioFolder.exists())
            {
                Toast.makeText(this, "Folder created! ", Toast.LENGTH_SHORT).show();
                audioFolder.mkdir();
            }

            // Chronometer start counting time
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            // changing a text in notification textview
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener()
            {
                @Override
                public void onChronometerTick(Chronometer chronometer)
                {
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

            // Put somehow into file data
            recordLenght = mChronometer.getText().toString();

            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());

            mNotificationTextView.setText("Recording finished.");

            //trigering recording event
            isRecording = false;

            stopService(intent);
        }
    }

    // Finding files and attaching them to Recycle view


}
