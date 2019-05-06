package com.dawidrazny.audiohandler;

import android.widget.Toast;

public class audioItem
{
    private int itemId;
    private String itemFileName;
    private String itemFilePath;
    private String itemLength;

    public audioItem(int recordId, String fileName, String filePath)
    {
        itemId = recordId;
        itemFileName = fileName;
        itemFilePath = filePath;
    }

    public int getItemId()
    {
        return itemId;
    }

    public String getItemfilePath()
    {
        return itemFilePath;
    }

    public String getItemLength()
    {
        return itemLength;
    }

}
