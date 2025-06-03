package com.api.model.rosetta;

import com.exlibris.dps.Operation;

public class RepresentationContent extends com.exlibris.dps.RepresentationContent {
    public RepresentationContent(Builder builder) {
        super();
        this.setLabel(builder.label);
        this.setOperation(builder.operation);
        this.setNewFile(builder.newFile);
        this.setFileOriginalPath(builder.fileOriginalPath);
        this.setOldFilePid(builder.oldFilePid);
        this.setFixity(builder.fixity);
//        this.setMetadata(builder.metadata.toArray());
    }

    public RepresentationContent[] toArray() {
        return new RepresentationContent[]{this};
    }

    @Override
    public Fixity getFixity() {
        return new Fixity(super.getFixity());
    }

    //todo: account for metadata of len > 1
//    @Override
//    public MetaData[] getMetadata() {
//        return new MetaData(super.getMetadata()[0]).toArray();
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        RepresentationContent rc = (RepresentationContent) obj;
        boolean label = this.getLabel().equals(rc.getLabel());
        boolean operation = this.getOperation().equals(rc.getOperation());
        boolean newFile = this.getNewFile().equals(rc.getNewFile());
        boolean fileOriginalPath = this.getFileOriginalPath().equals(rc.getFileOriginalPath());
        boolean oldFilePid = this.getOldFilePid().equals(rc.getOldFilePid());
        boolean fixity = this.getFixity().equals(rc.getFixity());

        return label && operation && newFile && fileOriginalPath && oldFilePid && fixity;
    }

    public static class Builder {
        private String label;
        private Operation operation;
        private String newFile;
        private String fileOriginalPath;
        private String oldFilePid;
        private Fixity fixity;
        private MetaData metadata;

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder operation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public Builder newFile(String newFile) {
            this.newFile = newFile;
            return this;
        }

        public Builder fileOriginalPath(String fileOriginalPath) {
            this.fileOriginalPath = fileOriginalPath;
            return this;
        }
        public Builder oldFilePid(String oldFilePid) {
            this.oldFilePid = oldFilePid;
            return this;
        }
        public Builder fixity(Fixity fixity) {
            this.fixity = fixity;
            return this;
        }

        public Builder metadata(MetaData metadata) {
            this.metadata = metadata;
            return this;
        }


        @Override
        public String toString() {
            return "Builder{" +
                    "label='" + label + '\'' +
                    ", operation=" + operation +
                    ", newFile='" + newFile + '\'' +
                    ", fileOriginalPath='" + fileOriginalPath + '\'' +
                    ", oldFilePid='" + oldFilePid + '\'' +
                    ", fixity=" + fixity +
                    '}';
        }

        public RepresentationContent build() {
            return new RepresentationContent(this);
        }
    }
}
