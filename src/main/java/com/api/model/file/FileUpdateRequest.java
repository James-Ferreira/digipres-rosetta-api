package com.api.model.file;

public class FileUpdateRequest {
    private final String currentFilePath;
    private final String newFilePath;

    public FileUpdateRequest(String currentFilePath, String newFilePath) {
        this.currentFilePath = currentFilePath;
        this.newFilePath = newFilePath;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public String getNewFilePath() {
        return newFilePath;
    }

    @Override
    public String toString() {
        return "FileUpdateRequest{" +
                "currentFilePath='" + currentFilePath + '\'' +
                ", newFilePath='" + newFilePath + '\'' +
                "}\n";
    }
}
