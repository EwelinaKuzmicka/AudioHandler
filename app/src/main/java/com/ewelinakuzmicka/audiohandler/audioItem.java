package com.ewelinakuzmicka.audiohandler;

public class audioItem
{
    private int itemId;
    private String itemFileName;
    private String itemFilePath;
    private int itemLengthMili;

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

}
