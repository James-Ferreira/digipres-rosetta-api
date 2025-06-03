package com.api.model.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileRefreshResponse {
    public ArrayList<String> success;
    public Map<String, String> failures;

    public FileRefreshResponse() {
        this.success = new ArrayList<>();
        this.failures = new HashMap<String, String>();
    }

    public void addSuccess(String s) {
        this.success.add(s);
    }

    public void addFailure(String s, Exception e) {
        this.failures.put(s, e.getMessage());
    }

    public ArrayList<String> getSuccess() {
        return success;
    }

    public Map<String, String> getFailures() {
        return failures;
    }

    public void addSuccesses(ArrayList<String> s) {
        this.success.addAll(s);
    }

    public void addFailures(Map<String, String> f) {
        this.failures.putAll(f);
    }


    @Override
    public String toString() {
        return "FileRefreshResponse{" +
                "success=" + success +
                ", failures=" + failures +
                "}\n";
    }
}
