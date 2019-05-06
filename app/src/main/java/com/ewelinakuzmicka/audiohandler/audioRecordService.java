package com.ewelinakuzmicka.audiohandler;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class audioRecordService extends Service
{
    // File name variables
    private String mFileName = "";
    private String mFilePath = "";

    // Media Recorder for handling audio recording
    private MediaRecorder mMediaRecorder;


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
        //recordsList.add(audioItem);
    }

    @Override
    public void onDestroy()
    {
        if (mMediaRecorder != null)
        {
            stopRecording();
        }
        super.onDestroy();
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

        Toast.makeText(this, " Recording file " + " " + mFilePath, Toast.LENGTH_LONG).show();

        // Create audio item
        audioItem record = new audioItem(recordsCount, mFileName, mFilePath);

        // Create audio file inside smartphone
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFile(mFilePath);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setAudioChannels(1);

        try
        {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        }
        catch (IOException e)
        {
            Log.e("audioRecordService","An error occurred.");
        }

    }

    // handling stoping recording and saving file
    public void stopRecording()
    {
        Log.e("audioRecordService","Destroy process");
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;

        Toast.makeText(this, "Saved to file " + " " + mFilePath, Toast.LENGTH_LONG).show();
    }
}
