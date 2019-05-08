package com.ewelinakuzmicka.audiohandler;

import android.app.Activity;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class playDialog extends Dialog implements android.view.View.OnClickListener
{
    public Activity mActivity;
    private ImageView vPlayButton;
    private ImageView vStopButton;
    private TextView vAudioTitleTextView;
    private TextView vCurrentTimeTextView;
    private TextView vDurationTextView;

    private ProgressBar vAudioProgressBar;

    private fileItem mFileItem;

    private Handler handler = new Handler();

    private int mProgressStatus;

    //play / pause
    public MediaPlayer mMediaPlayer;
    private Boolean isPlaying;

    public playDialog(Activity activity, fileItem fileitem)
    {
        super(activity);
        this.mActivity = activity;
        this.mFileItem = fileitem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.play_dialog);

        if (mMediaPlayer != null)
        {
            stopPlaying();
        }

        vAudioTitleTextView = (TextView) findViewById(R.id.audioTitleTextView);
        vCurrentTimeTextView = (TextView) findViewById(R.id.currentTimeTextView);
        vDurationTextView = (TextView) findViewById(R.id.durationTextView);

        vPlayButton = (ImageView) findViewById(R.id.playButton);
        vPlayButton.setOnClickListener(this);

        vStopButton = (ImageView) findViewById(R.id.stopButton);
        vStopButton.setOnClickListener(this);

        vAudioProgressBar = (ProgressBar) findViewById(R.id.audioProgressBar);

        isPlaying = false;

        vAudioTitleTextView.setText(mFileItem.getFileName());
        vDurationTextView.setText(mFileItem.getFileDuration());

        vAudioProgressBar.setProgress(0);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.playButton:
                Log.d("listAdapter", "Otwieramy dialog");
                startPlaying();
                break;
            case R.id.stopButton:
                stopPlaying();
                break;
            default:
                break;
        }
    }

    private void startPlaying()
    {
        if (mMediaPlayer != null)
        {
            stopPlaying();
        }

        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(mFileItem.getFilePath());
            mMediaPlayer.prepare();

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e)
        {
            Log.e("audioListAdapter", "Process failed");
        }

        isPlaying = true;

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });

        vAudioProgressBar.setMax(mMediaPlayer.getDuration());

        updateProgressBar();
    }

    private void stopPlaying()
    {
        if(isPlaying)
        {
            vAudioProgressBar.setProgress(mMediaPlayer.getDuration());
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            isPlaying = false;
            handler.removeCallbacks(mRunnable);
        }
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mMediaPlayer != null){

                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                vAudioProgressBar.setProgress(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition) - TimeUnit.MINUTES.toSeconds(minutes);
                vCurrentTimeTextView.setText(String.format("%02d:%02d", minutes, seconds));

                updateProgressBar();
            }
        }
    };

   private void updateProgressBar()
   {
       handler.postDelayed(mRunnable, 1000);
   }

}
