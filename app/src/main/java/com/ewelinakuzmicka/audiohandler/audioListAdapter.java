package com.ewelinakuzmicka.audiohandler;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class audioListAdapter extends RecyclerView.Adapter<audioListAdapter.audioElementView>
{
    Context mContext;
    LinearLayoutManager llm;
    private List<fileItem> mFileList;

    //play / pause
    private MediaPlayer mMediaPlayer;


    public static class audioElementView extends RecyclerView.ViewHolder
    {
        protected TextView vName;
        private TextView vID;
        private TextView vDuration;
        private Button vPlayButton;

        protected int vPosition;



        public audioElementView(View v, int position)
        {
            super(v);
            vPosition = position;
            vName = (TextView) v.findViewById(R.id.audioNameTextView);
            vID = (TextView) v.findViewById(R.id.audioIdTextView);
            vDuration = (TextView) v.findViewById(R.id.audioDurationTextView);
            vPlayButton = (Button) v.findViewById(R.id.playButton);

        }
    }


    public audioListAdapter(Context context, LinearLayoutManager linearLayoutManager,List<fileItem> fileList)
    {
        mContext = context;
        llm = linearLayoutManager;
        this.mFileList = fileList;
    }


    @Override
    public audioElementView onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.audio_list_element, viewGroup, false);

        mContext = viewGroup.getContext();

        return new audioElementView(itemView, i);
    }

    @Override
    public void onBindViewHolder(audioElementView audioElementView, int i)
    {
        final fileItem fileItem = mFileList.get(i);

        audioElementView.vID.setText(fileItem.getFileID());
        audioElementView.vName.setText(fileItem.getFileName());
        audioElementView.vDuration.setText(fileItem.getFileDuration());

        audioElementView.vPlayButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startPlaying(fileItem.getFilePath());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mFileList.size();
    }

    private void startPlaying(String filePath)
    {
        if (mMediaPlayer != null)
        {
            stopPlaying();
        }

        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(filePath);
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

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });
    }

    private void stopPlaying()
    {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

}
