package com.ewelinakuzmicka.audiohandler;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class audioListAdapter extends RecyclerView.Adapter<audioListAdapter.audioElementView>
{
    Context mContext;
    LinearLayoutManager llm;
    private List<fileItem> mFileList;
    private Activity mActivity;


    public static class audioElementView extends RecyclerView.ViewHolder
    {
        protected TextView vName;
        private TextView vID;
        private TextView vDuration;
        private LinearLayout vListElement;

        protected int vPosition;



        public audioElementView(View v, int position)
        {
            super(v);
            vPosition = position;
            vName = (TextView) v.findViewById(R.id.audioNameTextView);
            vID = (TextView) v.findViewById(R.id.audioIdTextView);
            vDuration = (TextView) v.findViewById(R.id.audioDurationTextView);
            vListElement = (LinearLayout) v.findViewById(R.id.list_element);
        }
    }


    public audioListAdapter(Context context, LinearLayoutManager linearLayoutManager, List<fileItem> fileList, Activity activity)
    {
        mContext = context;
        llm = linearLayoutManager;
        this.mFileList = fileList;
        this.mActivity = activity;
    }


    @Override
    public audioElementView onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.audio_list_element, viewGroup, false);

        mContext = viewGroup.getContext();

        return new audioElementView(itemView, i);
    }

    @Override
    public void onBindViewHolder(final audioElementView audioElementView, int i)
    {
        final fileItem fileItem = mFileList.get(i);

        audioElementView.vID.setText(fileItem.getFileID());
        audioElementView.vName.setText(fileItem.getFileName());
        audioElementView.vDuration.setText(fileItem.getFileDuration());

        audioElementView.vListElement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playDialog playDialog = new playDialog(mActivity, fileItem);
                playDialog.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mFileList.size();
    }

}
