package com.api.util;

import com.api.model.rosetta.RosettaNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.api.model.rosetta.Submission;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    public List<List<String>> parseCSV(String filepath) {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    public List<Submission> parseSubmissionFile(String filepath) {
        List<List<String>> lines = parseCSV(filepath);
        List<Submission> result = new ArrayList<>();

        lines.remove(0); //pop header

        for (List<String> line : lines) {
            Submission r = new Submission(line);
            result.add(r);
        }

        return result;
    }

    public static List<String> parseXML(Document doc, XPath xpath, String expression) {
        List<String> list = new ArrayList<>();
        try {
            XPathExpression expr = xpath.compile(expression);
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String parseIDFromXML(String xml, String query) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xml)));

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();

            xpath.setNamespaceContext(new RosettaNamespaceContext());

            List<String> result = Parser.parseXML(doc, xpath, query);
            return result.get(0); //todo: handle better
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ""; //todo: handle better
    }

    public static String parseRepPIDFromMets(String xml, String filePID) {
        String query = String.format("/mets:mets/mets:fileSec/mets:fileGrp[mets:file[@ID='%s']]/@ID", filePID);
        return parseIDFromXML(xml, query);
    }

    public static String parseFilePIDFromMETS(String xml, String filename) {
        String base = "/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/dnx:dnx[dnx:section/dnx:record/dnx:key[@id='fileOriginalName' and text() ='%s']]/dnx:section/dnx:record[dnx:key[@id='internalIdentifierType' and text() ='PID']]/dnx:key[@id='internalIdentifierValue']/text()";
        String query = String.format(base, filename);
        return parseIDFromXML(xml, query);
    }

    public static String parseIEPIDFromSRU(String xml) {
        String query = "/srw:searchRetrieveResponse/srw:records/srw:record/srw:recordData/dc:record/dc:identifier[@xsi:type='PID']/text()";
        return parseIDFromXML(xml, query);
    }
}
