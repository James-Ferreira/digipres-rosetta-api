package com.api.model.rosetta;

public class Fixity extends com.exlibris.dps.Fixity {
    public Fixity(Builder builder) {
        super();
        this.setValue(builder.value);
        this.setAlgorithmType(builder.algorithmType);
    }

    public Fixity(com.exlibris.dps.Fixity fixity) {
        this(new Builder()
                .value(fixity.getValue())
                .algorithmType(fixity.getAlgorithmType()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Fixity f = (Fixity) obj;
        boolean value = this.getValue().equals(f.getValue());
        boolean algorithmType = this.getAlgorithmType().equals(f.getAlgorithmType());

        return value && algorithmType;
    }

    public static class Builder {
        private String value;
        private String algorithmType;


        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder algorithmType(String algorithmType) {
            this.algorithmType = algorithmType;
            return this;
        }

        public Fixity build() {
            return new Fixity(this);
        }
    }

}
