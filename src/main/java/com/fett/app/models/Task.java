package com.fett.app.models;

public class Task {
    private String id;
    private int workers;
    private int views;
    private int watchlength;
    private String status;
    private String userid;


    public Task(String userid, String id, int workers, int views, int watchlength, String status) {
        this.workers = workers;
        this.views = views;
        this.watchlength = watchlength;
        this.status = status;
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Task() {
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getWatchlength() {
        return watchlength;
    }

    public void setWatchlength(int watchlength) {
        this.watchlength = watchlength;
    }
}
