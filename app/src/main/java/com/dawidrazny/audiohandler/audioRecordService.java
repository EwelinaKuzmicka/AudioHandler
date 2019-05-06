package com.dawidrazny.audiohandler;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.util.ArrayList;

public class audioRecordService extends Service
{
    // File name variables
    private String mFileName = "";
    private String mFilePath = "";

    // List of Records
    ArrayList<audioItem> recordsList;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        startRecording();
        return START_STICKY;
    }

    public void addRecordtoList(audioItem audioItem)
    {
        recordsList.add(audioItem);
    }

    public void startRecording()
    {
        // Generate name of file and path to file
        int recordsCount = 0;
        File file;

        do {
            recordsCount++;

            mFileName = "Recording " + recordsCount + ".mp4";

            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += "/AudioHandler/" + mFileName;

            file = new File(mFilePath);
        }while (file.exists() && !file.isDirectory());


        // Create audio item
        audioItem record = new audioItem(recordsCount, mFileName, mFilePath);

        

    }
}
