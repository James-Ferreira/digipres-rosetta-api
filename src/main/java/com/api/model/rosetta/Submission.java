package com.api.model.rosetta;

import com.exlibris.dps.Operation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Submission {
    private final String pdsHandle;
    private final String iePid;
    private final String submissionReason;
    private final List<RepresentationContent> representationContent;
    private final List<MetaData> metadata;
    private final boolean commit;

    private final Operation operation;

    // UPDATE
    private final String repPID;

    // ADD
    private final String preservationType;
    private final String label;
    private final String representationEntityType;
    private final String representationCode;
    private final String arPolicyID;

    public Submission(
            Builder builder
    ) {
        this.pdsHandle = builder.pdsHandle;
        this.iePid = builder.iePID;
        this.submissionReason = builder.submissionReason;
        this.representationContent = builder.representationContent;
        this.metadata = builder.metadata;
        this.commit = builder.commit;
        this.repPID = builder.repPID;

        this.preservationType = builder.preservationType;
        this.label = builder.label;
        this.representationEntityType = builder.representationEntityType;
        this.representationCode = builder.representationCode;
        this.arPolicyID = builder.arPolicyID;

        this.operation = builder.operation;
    }
    public Submission(List<String> values) {
        this(parseSubmissionCSV(values));
    }

    public static Builder parseSubmissionCSV(List<String> values) {
        String validation = validateValues(values);

        if(!validation.isEmpty()) {
            throw new IllegalArgumentException(validation);
        }

        String operation = values.get(0);
        String iePid = values.get(1);
        String repID = values.get(2); //only used in REPLACE
        String reason = values.get(3);
        String newFile = values.get(4);
        String fileOrigPath = values.get(5); //maybe optional??
        String oldFilePID = values.get(6);

        // Fixity
        String fixityValue = values.get(7);
        String fixityAlgorithmType = values.get(8);

        // ADD
        String label = values.get(9);
        String preservationType = values.get(10);
        String repEntityType = values.get(11);
        String repCode = values.get(12);
        String arPolicyID = values.get(13);


        Fixity fixity = new Fixity.Builder()
                .value(fixityValue)
                .algorithmType(fixityAlgorithmType)
                .build();

        RepresentationContent rc = new RepresentationContent.Builder()
                .label(label)
                .operation(Operation.valueOf(operation))
                .newFile(newFile)
                .fileOriginalPath(fileOrigPath)
                .oldFilePid(oldFilePID)
                .fixity(fixity)
                .build();

        Builder sb = new Submission.Builder()
                .pdsHandle("")
                .iePID(iePid)
                .repPID(repID)
                .operation(Operation.valueOf(operation))
                .submissionReason(reason)
                .representationContent(rc.toArray())
                .label(label)
                .preservationType(preservationType)
                .representationEntityType(repEntityType)
                .representationCode(repCode)
                .arPolicyID(arPolicyID)
                .commit(false); // flip when ready to test

        return sb;
    }

    public static String validateValues(List<String> values) {
        String[] validOperations = {"ADD", "REPLACE"};
        String operation = values.get(0);

        if(!Arrays.asList(validOperations).contains(operation)) {
            return "Expected operation as one of of :" + Arrays.toString(validOperations) +", Received: " + operation;
        }

        if(Objects.equals(operation, "ADD")) {
            int[] requiredIndices = {
                    0, 1, 3, 4, 5, 6, 7, 8, 9, 10,
                    11, 12, 13
            };

            for (int ri : requiredIndices) {
                if (values.get(ri).isEmpty()) {
                    return "Expected Index Empty: " + ri;
                }
            }
        }

        if(Objects.equals(operation, "REPLACE")) {
            int[] requiredIndices = {
                    0, 1, 2, 3, 4, 5, 6, 7, 8,
            };

            for (int ri : requiredIndices) {
                if (values.get(ri).isEmpty()) {
                    return "Expected Index Empty: " + ri;
                }
            }
        }

        // todo: check enums, character lengths

        return "";
    }

    public String getPdsHandle() {
        return this.pdsHandle;
    }
    public Operation getOperation() {
        return this.operation;
    }

    public String getIePid() {
        return this.iePid;
    }

    public String getSubmissionReason() {
        return this.submissionReason;
    }

    public List<com.exlibris.dps.RepresentationContent> getRepresentationContent() {
        return this.representationContent.stream()
                .map(rc -> (com.exlibris.dps.RepresentationContent) rc)
                .collect(Collectors.toList());
    }

    public List<MetaData> getMetadata() {
        return this.metadata;
    }

    public boolean getCommit() {
        return this.commit;
    }

    public String getRepPID() {
        return this.repPID;
    }

    public String getPreservationType() {
        return this.preservationType;
    }

    public String getLabel() {
        return this.label;
    }

    public String getRepresentationEntityType() {
        return this.representationEntityType;
    }

    public String getRepresentationCode() {
        return this.representationCode;
    }

    public String getArPolicyID() {
        return this.arPolicyID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Submission b = (Submission) obj;

        boolean iePID = this.getIePid().equals(b.getIePid());
        boolean reason = this.getSubmissionReason().equals(b.getSubmissionReason());
        boolean rc = this.getRepresentationContent().equals(b.getRepresentationContent());
        boolean commit = this.getCommit() == b.getCommit();
        boolean repPID = this.getRepPID().equals(b.getRepPID());
        boolean preservationType = this.getPreservationType().equals(b.getPreservationType());
        boolean label = this.getLabel().equals(b.getLabel());
        boolean representationEntityType = this.getRepresentationEntityType().equals(b.getRepresentationEntityType());
        boolean representationCode = this.getRepresentationCode().equals(b.getRepresentationCode());
        boolean arPolicyID = this.getArPolicyID().equals(b.getArPolicyID());
        boolean operation = this.getOperation().equals(b.getOperation());

        return  iePID &&
                reason &&
                rc &&
                commit &&
                repPID &&
                preservationType &&
                label &&
                representationEntityType &&
                representationCode &&
                arPolicyID &&
                operation;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "pdsHandle='" + pdsHandle + '\'' +
                ", iePid='" + iePid + '\'' +
                ", submissionReason='" + submissionReason + '\'' +
                ", representationContent=" + representationContent.toString() +
                ", metadata=" + metadata +
                ", commit=" + commit +
                ", operation=" + operation +
                ", repPID='" + repPID + '\'' +
                ", preservationType='" + preservationType + '\'' +
                ", label='" + label + '\'' +
                ", representationEntityType='" + representationEntityType + '\'' +
                ", representationCode='" + representationCode + '\'' +
                ", arPolicyID='" + arPolicyID + '\'' +
                '}';
    }

    public static class Builder {
        private String pdsHandle;
        private String iePID;
        private String repPID;
        private String submissionReason;
        private List<RepresentationContent> representationContent;
        private List<MetaData> metadata;
        private boolean commit;

        private String preservationType;
        private String label;
        private String representationEntityType;
        private String representationCode;
        private String arPolicyID;

        private Operation operation;

        public Builder operation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public Builder pdsHandle(String pdsHandle) {
            this.pdsHandle = pdsHandle;
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

        public Builder submissionReason(String submissionReason) {
            this.submissionReason = submissionReason;
            return this;
        }

        public Builder representationContent(RepresentationContent[] representationContent) {
            this.representationContent = List.of(representationContent);
            return this;
        }

        public Builder metadata(MetaData[] metadata) {
            this.metadata = List.of(metadata);
            return this;
        }

        public Builder commit(boolean commit) {
            this.commit = commit;
            return this;
        }

        public Builder preservationType(String preservationType) {
            this.preservationType = preservationType;
            return this;
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder representationEntityType(String representationEntityType) {
            this.representationEntityType = representationEntityType;
            return this;
        }

        public Builder representationCode(String representationCode) {
            this.representationCode = representationCode;
            return this;
        }

        public Builder arPolicyID(String arPolicyID) {
            this.arPolicyID = arPolicyID;
            return this;
        }

        public Submission build() {
            return new Submission(this);
        }
    }
}
