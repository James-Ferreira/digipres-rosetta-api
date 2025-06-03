package com.api.service;

import com.exlibris.dps.*;
import com.exlibris.dps.sdk.pds.HeaderHandlerResolver;
import com.api.util.Parser;
import jakarta.xml.ws.BindingProvider;
import com.api.model.file.FileDetails;
import com.api.model.rosetta.Submission;

import java.util.List;

public class IEBroker {
    private final IEWebServices ieService;
    private final SRUService sruService;

    public IEBroker(String username, String userpass, String institutionCode, String ieServiceEndpoint, String sruEndpoint) {
        this.sruService = new SRUService(username, userpass, institutionCode, sruEndpoint);

        IEWebServices_Service iews = new IEWebServices_Service();
        HeaderHandlerResolver hhr = new HeaderHandlerResolver(username, userpass, institutionCode);
        iews.setHandlerResolver(hhr);
        this.ieService = iews.getIEWebServicesPort();

        // connect over https endpoint, fails on http
        BindingProvider bp = (BindingProvider) ieService;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ieServiceEndpoint);
    }

    public String getMETS(String iePID) throws UserAuthorizeException_Exception, IEWSException_Exception {
        try {
            return this.ieService.getIE(1L, iePID, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public void submitRepresentation(Submission s) throws InvalidMIDException_Exception, LockedIeException_Exception, UserAuthorizeException_Exception, InvalidXmlException_Exception, FixityEventException_Exception, InvalidTypeException_Exception, IEWSException_Exception, NotInPermanentException_Exception {
        try {
            if (s.getOperation() == Operation.ADD) {
                this.ieService.addRepresentation(
                        s.getArPolicyID(),
                        s.getCommit(),
                        s.getIePid(),
                        s.getLabel(),
                        (List<com.exlibris.dps.MetaData>)(List<?>) s.getMetadata(),
                        s.getPdsHandle(),
                        s.getPreservationType(),
                        s.getRepresentationCode(),
                        (List<com.exlibris.dps.RepresentationContent>)(List<?>) s.getRepresentationContent(),
                        s.getRepresentationEntityType(),
                        s.getSubmissionReason()
                );
            } else {
                this.ieService.updateRepresentation(
                        s.getCommit(),
                        s.getIePid(),
                        (List<com.exlibris.dps.MetaData>)(List<?>) s.getMetadata(),
                        s.getPdsHandle(),
                        s.getRepPID(),
                        (List<com.exlibris.dps.RepresentationContent>)(List<?>) s.getRepresentationContent(),
                        s.getSubmissionReason()
                );
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public FileDetails getDetailsFromFilename(String filename) throws UserAuthorizeException_Exception, IEWSException_Exception {
        String iePID = sruService.getIEPid(filename);
        if (iePID.isEmpty()) {
            throw new IEWSException_Exception("Failed to recover an IEPid from " + filename, null);
        }
        String mets = getMETS(iePID);

        String filePID = Parser.parseFilePIDFromMETS(mets, filename);
        String repPID = Parser.parseRepPIDFromMets(mets, filePID);
        FileDetails fd = new FileDetails
                .Builder()
                .filename(filename)
                .iePID(iePID)
                .repPID(repPID)
                .filePID(filePID)
                .build();
        return fd;
    }

    public IeStatusInfo manageIE(String iePID, Action action) throws LockedIeException_Exception, UserAuthorizeException_Exception, IEWSException_Exception, NotInPermanentException_Exception {
        return this.ieService.manageIE(action, iePID, "");
    }
}
