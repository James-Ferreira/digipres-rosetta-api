package com.api.service;

import com.exlibris.dps.*;
import com.api.model.rosetta.RepresentationContent;
import com.api.model.file.FileRefreshRequest;
import com.api.model.file.FileRefreshResponse;
import com.api.model.file.FileSpecificRefreshRequest;
import com.api.model.file.FileUpdateRequest;
import com.api.model.file.FileDetails;
import com.api.model.rosetta.Submission;

import java.io.File;
import java.util.ArrayList;

public class FileService {
    private IEBroker ieBroker;

    public FileService() {
        String user = System.getenv("ROSETTA_USER");
        String pass = System.getenv("ROSETTA_PWD");
        String inst = System.getenv("ROSETTA_INSTITUTION");
        String endpoint = System.getenv("IE_SERVICE_ENDPOINT");
        String sru = System.getenv("SRU_ENDPOINT");

        if(user.isEmpty() || pass.isEmpty() || inst.isEmpty() || endpoint.isEmpty() || sru.isEmpty()) {
            System.out.println("Please ensure all environment variables are set");
            System.exit(1);
        }

        ieBroker = new IEBroker(user, pass, inst, endpoint, sru);
    }

    public void replaceFile(FileUpdateRequest fur) {
        try {
            Submission s = formatUpdateSubmission(fur);
            ieBroker.submitRepresentation(s);
        } catch (InvalidMIDException_Exception | LockedIeException_Exception | UserAuthorizeException_Exception |
                 InvalidXmlException_Exception | FixityEventException_Exception | InvalidTypeException_Exception |
                 IEWSException_Exception | NotInPermanentException_Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FileDetails getFileDetails(String filepath) throws UserAuthorizeException_Exception, IEWSException_Exception {
        String filename = new File(filepath).getName();
        try {
            return ieBroker.getDetailsFromFilename(filename);
        } catch (UserAuthorizeException_Exception | IEWSException_Exception e) {
            throw e;
        }
    }

    public Submission formatUpdateSubmission(FileUpdateRequest fur) throws UserAuthorizeException_Exception, IEWSException_Exception {

        FileDetails fd = getFileDetails(fur.getCurrentFilePath());

        RepresentationContent rc = new RepresentationContent.Builder()
                .operation(Operation.REPLACE)
                .newFile(fur.getNewFilePath())
                .fileOriginalPath(fur.getCurrentFilePath())
                .oldFilePid(fd.getFilePID())
                .build();

        return new Submission.Builder()
                .iePID(fd.getIePID())
                .repPID(fd.getRepPID())
                .submissionReason("Representation Update via Rosetta Java API")
                .representationContent(rc.toArray())
                .operation(Operation.REPLACE)
                .commit(true)
                .build();
    }

    public FileRefreshResponse refreshFileList(FileRefreshRequest frr) {
        FileRefreshResponse response = new FileRefreshResponse();
        ArrayList<String> fileList = frr.getFileList();

        for (int i = 0; i < fileList.size(); i++) {
            String f = fileList.get(i);

            FileUpdateRequest fur = new FileUpdateRequest(f, f);

            try {
                Submission s = formatUpdateSubmission(fur);
                ieBroker.submitRepresentation(s);
                response.addSuccess(f);
                if(i < fileList.size() - 1) {
                    Thread.sleep(20000);
                }
            } catch (Exception e) {
                response.addFailure(f, e);
            }
        }

        return response;
    }

    public void refreshFileSpecific(FileSpecificRefreshRequest req) {

        try{
            RepresentationContent rc = new RepresentationContent.Builder()
                    .operation(Operation.REPLACE)
                    .newFile(req.getFilepath())
                    .fileOriginalPath(req.getFilepath())
                    .oldFilePid(req.getFilePID())
                    .build();

            Submission s = new Submission.Builder()
                    .iePID(req.getIePID())
                    .repPID(req.getRepPID())
                    .submissionReason("Representation Update via Rosetta Java API")
                    .representationContent(rc.toArray())
                    .operation(Operation.REPLACE)
                    .commit(true)
                    .build();

            ieBroker.submitRepresentation(s);
        } catch (InvalidMIDException_Exception | LockedIeException_Exception | UserAuthorizeException_Exception |
        InvalidXmlException_Exception | FixityEventException_Exception | InvalidTypeException_Exception |
        IEWSException_Exception | NotInPermanentException_Exception e) {
            throw new RuntimeException(e);
        }
    }
}
