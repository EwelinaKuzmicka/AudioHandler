package com.ewelinakuzmicka.audiohandler;

public class fileItem
{
    private String filePath, fileName, fileID;

    public fileItem()
    {

    }

    public fileItem(String fileID, String fileName, String filePath)
    {
        this.fileID = fileID;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileID()
    {
        return this.fileID;
    }

}
