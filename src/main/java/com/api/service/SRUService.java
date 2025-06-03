package com.api.service;

import com.api.util.Parser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class SRUService
{
    private final String base64Auth;
    private final String sruEndpoint;

    public SRUService(String username, String userpass, String institutionCode, String sruEndpoint) {
        String basic = String.format("%s-institutionCode-%s:%s", username, institutionCode, userpass);
        this.base64Auth = Base64.getEncoder().encodeToString(basic.getBytes());
        this.sruEndpoint = sruEndpoint;
    }

    public HttpResponse<String> sendRequest(String req) throws URISyntaxException, IOException, InterruptedException {
        String url = String.format(
                "%s%s",
                sruEndpoint,
                req
        );

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Basic " + this.base64Auth)
                .header("Content-Type", "text/xml;charset=UTF-8")
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String getIEPid(String filename) {
        String filenameWithoutExt = filename.substring(0, filename.lastIndexOf('.'));
        String req = String.format("%s=%s", "/delivery/sru?version=1.2&operation=searchRetrieve&query=FILE.generalFileCharacteristics.label", filenameWithoutExt);

        try {
            HttpResponse<String> response = sendRequest(req);
            return Parser.parseIEPIDFromSRU(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
