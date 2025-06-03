# Digital Preservation Rosetta API

The Rosetta Java API codebase provides a Dockerised API wrapper around the official RosettaSDK provided by ExLibris.

The following environment variables are required
- IE_SERVICE_ENDPOINT 
- SRU_ENDPOINT 
- ROSETTA_INSTITUTION 
- ROSETTA_USER 
- ROSETTA_PWD 

Please search the codebase for 'REPLACE_ME', and update with your own information.


Please note Maven generates the WDSL classes and may require running the build and 'Generate Sources and Update Folder` on the host machine to avoid import syntax issues

The API currently supports

### GET /file 

For a provided Filepath, retrieves the PIDS for the Intellectual Entity, Representation, and File.  

This method: 
1. Queries Rosetta via the SRU protocol to search and retrieve an XML document containing the IntellectualEntityPID based on a related file label   
2. Parse the SRU XML to extract the IE PID 
3. Use the extracted IEPID to execute IEWebServices getIE(iePID)  and return the METS file 
4. Parse the METS XML to extract the FilePID and RepPID 
5. Return the result as JSON 

 
```
# request 

curl -X GET -H "Content-Type: application/json" -d \ 

'{ 

    "filepath": "/permanent_storage/archive/foo/bar.tif" 

}' api:8080/file 

 

# response (success) 

{"filename":"bar.tif","iePID":"IE1200048","repPID":"REP1200009","filePID":"FL1200050"} 

 
# response (failure) 

com.exlibris.dps.IEWSException_Exception: There is no IE with PID 
```

### POST /file/refresh 

Use this method to re-add files to existing IEs / to update IEs 

For a provided file of newline separated filepaths, perform an in-place refresh of the file within its existing IE 

 
This method: 
1. Create a text file with the filepath of files to be re-added to existing IEs 
2. Reads each line from the input file and retrieve the IEPID, RepPID, and FilePID for each Filepath. 
3. Submits an UpdateSubmission adhering to IEWebservices updateRepresentation() method, setting both the currentFilepath and newFilepath to the same value (as this is an in-place refresh) 
4. If an error occurs the filepath, along with the exception message, are appended to response[“failures”]. If there are no issues, the filepath is appended to response[“success”] 
5. Between submissions, a naive delay of 20 seconds occurs to reduce IE locking issues preventing successful updates 
6. Return the result as JSON 

 
```
# example.txt 
/operational_storage/foo/bar/baz/file1.txt 

/operational_storage/foo/bar/baz/file2.zip 

/operational_storage/foo/bar/baz/something_wrong.xml 

 
# request 

curl -X POST -F "file=@./example.txt" api:8080/file/refresh >> output.json 

 

# response 

{ 

    "success": [ 

        “/operational_storage/foo/bar/baz/file1.txt”, 

       “/operational_storage/foo/bar/baz/file1.zip” 

    ], 

    "failures": { 

        “/operational_storage/foo/bar/baz/something_wrong.xml": "Failed to recover an IEPID " 

    } 
}
```
 