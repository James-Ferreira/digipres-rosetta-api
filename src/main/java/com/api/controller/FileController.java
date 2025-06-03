package com.api.controller;

import com.api.model.file.FileGetRequest;
import com.api.model.file.FileRefreshRequest;
import com.api.model.file.FileRefreshResponse;
import com.api.service.FileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.api.model.file.FileDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService service = new FileService();

    @GetMapping
    public ResponseEntity<String> info(@RequestBody FileGetRequest request) {
        ObjectMapper om = new ObjectMapper();
        FileDetails fd = null;
        String output = "";
        try {
            fd = service.getFileDetails(request.getFilepath());
            output = om.writeValueAsString(fd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(output+"\n", HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshByFile(@RequestParam("file") MultipartFile file) {
        ArrayList<String> fileList = new ArrayList<>();
        FileRefreshResponse consolidatedOutput = new FileRefreshResponse();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                fileList.add(line);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (fileList.isEmpty()) {
            return new ResponseEntity<>("Uploaded file is empty", HttpStatus.BAD_REQUEST);
        }

        if (fileList.size() >= 3) {
            int maxThreads = 3;
            int segmentSize = (int) Math.ceil((double) fileList.size() / maxThreads);
            ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
            List<Future<FileRefreshResponse>> futures = new ArrayList<>();

            for (int i = 0; i < maxThreads; i++) {
                int start = i * segmentSize;
                int end = Math.min(start + segmentSize, fileList.size());

                ArrayList<String> segment = new ArrayList<>(fileList.subList(start, end));

                FileRefreshRequest request = new FileRefreshRequest(segment);
                int threadIndex = 1 + i;
                futures.add(executor.submit(() -> {
                    Thread.currentThread().setName("Thread " + threadIndex);
                    return service.refreshFileList(request);
                }));
            }

            for (Future<FileRefreshResponse> future : futures) {
                try {
                    consolidatedOutput.addSuccesses(future.get().getSuccess());
                    consolidatedOutput.addFailures(future.get().getFailures());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            executor.shutdown();
        } else {
            FileRefreshRequest request = new FileRefreshRequest(fileList);
            consolidatedOutput = service.refreshFileList(request);
        }

        ObjectMapper om = new ObjectMapper();
        String output = consolidatedOutput.toString();
        try {
            output = om.writeValueAsString(consolidatedOutput);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
