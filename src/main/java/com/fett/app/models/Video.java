package com.fett.app.models;

import java.util.List;

public class Video {
    String id;
    String url;
    String title;
    int viewsadded;
    int timeadded;
    int duration;
    int viewcount;
    int timecount;

    public int getViewcount() {
        return viewcount;
    }

    public void setViewcount(int viewcount) {
        this.viewcount = viewcount;
    }

    public int getTimecount() {
        return timecount;
    }

    public void setTimecount(int timecount) {
        this.timecount = timecount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewsadded() {
        return viewsadded;
    }

    public void setViewsadded(int viewsadded) {
        this.viewsadded = viewsadded;
    }

    public int getTimeadded() {
        return timeadded;
    }

    public void setTimeadded(int timeadded) {
        this.timeadded = timeadded;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

