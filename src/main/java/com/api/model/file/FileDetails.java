package com.api.model.file;

public class FileDetails {
    private String filename;
    private String iePID;
    private String repPID;
    private String filePID;

    public FileDetails(Builder builder) {
        this.setFilename(builder.filename);
        this.setIePID(builder.iePID);
        this.setRepPID(builder.repPID);
        this.setFilePID(builder.filePID);
    }
    public String getFilename() {
        return filename;
    }

    public String getIePID() {
        return iePID;
    }

    public String getFilePID() {
        return filePID;
    }

    public String getRepPID() {
        return repPID;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFilePID(String filePID) {
        this.filePID = filePID;
    }

    public void setIePID(String iePID) {
        this.iePID = iePID;
    }

    public void setRepPID(String repPID) {
        this.repPID = repPID;
    }

    @Override
    public String toString() {
        return "FileDetails{" +
                "filename='" + filename + '\'' +
                ", iePID='" + iePID + '\'' +
                ", repPID='" + repPID + '\'' +
                ", filePID='" + filePID + '\'' +
                "}\n";
    }

    public static class Builder {
        private String filename;
        private String iePID;
        private String repPID;
        private String filePID;

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder iePID(String iePID) {
            this.iePID = iePID;
            return this;
        }

        public Builder repPID(String repPID) {
            this.repPID = repPID;
            return this;
        }

        public Builder filePID(String filePID) {
            this.filePID = filePID;
            return this;
        }

        public FileDetails build() {
            return new FileDetails(this);
        }

    }
}
