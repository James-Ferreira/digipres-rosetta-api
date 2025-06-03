package com.api.model.rosetta;

public class MetaData extends com.exlibris.dps.MetaData {
    public MetaData(Builder builder) {
        super();
        this.setContent(builder.content);
        this.setMid(builder.mid);
        this.setType(builder.type);
        this.setSubType(builder.subType);
    }

    public MetaData(com.exlibris.dps.MetaData metadata) {
        this(new Builder()
                .content(metadata.getContent())
                .mid(metadata.getMid())
                .type(metadata.getType())
                .subType(metadata.getSubType()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MetaData md = (MetaData) obj;
        boolean content = this.getContent().equals(md.getContent());
        boolean mid = this.getMid().equals(md.getMid());
        boolean type = this.getType().equals(md.getType());
        boolean subType = this.getSubType().equals(md.getSubType());

        return content && mid && type && subType;
    }

    public MetaData[] toArray() {
        return new MetaData[]{this};
    }

    public static class Builder {
        private String content;
        private String mid;
        private String type;
        private String subType;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder mid(String mid) {
            this.mid = mid;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder subType(String subType) {
            this.subType = subType;
            return this;
        }

        public MetaData build() {
            return new MetaData(this);
        }
    }
}
