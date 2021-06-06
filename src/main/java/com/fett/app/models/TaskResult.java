package com.fett.app.models;


public class TaskResult {
    private String id;
    private int viewcount;
    private int timecount;
    private String status;
    private long starttime;
    private long endtime;

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}