package com.ewelinakuzmicka.audiohandler;

public class fileItem
{
    private String filePath, fileName, fileID, fileDuration;
    private int itemLengthMili;

    public fileItem(){};

    public fileItem(String fileID, String fileName, String filePath,String fileDuration, int fileLengthMili)
    {
        this.fileID = fileID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileDuration = fileDuration;
        this.itemLengthMili = fileLengthMili;
    }

    public String getFileID()
    {
        return this.fileID;
    }

    public String getFileName()
    {
        return this.fileName;
    }

    public String getFilePath()
    {
        return this.filePath;
    }

    public String getFileDuration()
    {
        return this.fileDuration;
    }

    public int getItemLength(){ return this.itemLengthMili; }
}
