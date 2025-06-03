package com.api.model.file;

public class FileSpecificRefreshRequest {

    private String iePID;
    private String repPID;
    private String filePID;
    private String filepath;


    public String getFilePID() {
        return filePID;
    }

    public String getIePID() {
        return iePID;
    }

    public String getRepPID() {
        return repPID;
    }

    public String getFilepath() {
        return filepath;
    }

    @Override
    public String toString() {
        return "FileSpecificRefreshRequest{" +
                "iePID='" + iePID + '\'' +
                ", repPID='" + repPID + '\'' +
                ", filePID='" + filePID + '\'' +
                ", filepath='" + filepath + '\'' +
                '}';
    }
}
