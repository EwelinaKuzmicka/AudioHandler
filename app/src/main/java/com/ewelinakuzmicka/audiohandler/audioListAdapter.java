package com.ewelinakuzmicka.audiohandler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class audioListAdapter extends RecyclerView.Adapter<audioListAdapter.audioElementView>
{
    Context mContext;
    LinearLayoutManager llm;
    private List<fileItem> mFileList;

    public static class audioElementView extends RecyclerView.ViewHolder
    {
        protected TextView vName;
        protected int vPosition;
        protected TextView vLength;
        protected TextView vDateAdded;
        protected View cardView;

        public audioElementView(View v, int position)
        {
            super(v);
            vPosition = position;
            Log.d("audioList", "audio Element View");
            vName = (TextView) v.findViewById(R.id.audioNameTextView);
        }
    }


    public audioListAdapter(Context context, LinearLayoutManager linearLayoutManager,List<fileItem> fileList)
    {
        Log.d("audioList", "DECLARED");
        mContext = context;
        llm = linearLayoutManager;
        this.mFileList = fileList;
    }


    @Override
    public audioElementView onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.audio_list_element, viewGroup, false);

        mContext = viewGroup.getContext();

        Log.d("audioList", "BINDED");
        return new audioElementView(itemView, i);
    }

    @Override
    public void onBindViewHolder(audioElementView audioElementView, int i)
    {
        fileItem fileItem = mFileList.get(i);

        Log.d("audioList", "FILE ID " + fileItem.getFileID());

        audioElementView.vName.setText("Number : " + i);
    }

    @Override
    public int getItemCount()
    {
        return mFileList.size();
    }

}
