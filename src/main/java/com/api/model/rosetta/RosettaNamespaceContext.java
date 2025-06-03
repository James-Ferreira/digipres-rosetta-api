package com.api.model.rosetta;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

public class RosettaNamespaceContext implements NamespaceContext {
    @Override
    public Iterator getPrefixes(String arg0) {
        throw new UnsupportedOperationException();
    }
    @Override
    public String getPrefix(String arg0) {
        throw new UnsupportedOperationException();
    }
    @Override
    public String getNamespaceURI(String prefix) {
        if (prefix == null) throw new NullPointerException("Null prefix");
        else if ("srw".equals(prefix)) {
            return "http://www.loc.gov/zing/srw/";
        } else if ("dc".equals(prefix)) {
            return "http://purl.org/dc/elements/1.1/";
        } else if ("xsi".equals(prefix)) {
            return "http://www.w3.org/2001/XMLSchema-instance";
        } else if ("mets".equals(prefix)) {
            return "http://www.loc.gov/METS/";
        } else if ("dnx".equals(prefix)) {
            return "http://www.exlibrisgroup.com/dps/dnx";
        } else if ("dcterms".equals(prefix)) {
            return "http://purl.org/dc/terms/";
        }
        return null;
    }
}
