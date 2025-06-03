package com.api.model.file;

import java.util.ArrayList;

public class FileRefreshRequest {
    private ArrayList<String> fileList;

    public FileRefreshRequest(ArrayList<String> fileList) {
        this.fileList = fileList;
    }

    public ArrayList<String> getFileList() {
        return fileList;
    }

    @Override
    public String toString() {
        return "FileRefreshRequest{" +
                "fileList=" + fileList +
                '}';
    }
}
