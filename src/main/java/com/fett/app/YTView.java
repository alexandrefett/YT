package com.fett.app;

import com.fett.app.models.Driver;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class YTView {
    private int numWorkers;
    private String videoUrl;
    private String apiKey;
    private int videoLength;
    private Driver webDriver;
    private static ExecutorService executor;

    public YTView(int numWorkers, String videoUrl, String apiKey, int videoLength, Driver webDriver) {
        this.numWorkers = numWorkers;
        this.videoUrl = videoUrl;
        this.apiKey = apiKey;
        this.videoLength = videoLength;
        this.webDriver = webDriver;
    }

    public void start() throws IOException {
    }
}
