package com.fett.app.models;

public class Task {
    String id;
    String userid;
    String channelid;
    String channelname;
    String playlistname;
    String type;
    String playlistid;
    String status;
    int views;
    int speed;
    int watchtime;
    long endtime;
    long starttime;
    int timeadded;
    int viewsadded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getChannelname() {
        return channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname;
    }

    public String getPlaylistname() {
        return playlistname;
    }

    public void setPlaylistname(String playlistname) {
        this.playlistname = playlistname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlaylistid() {
        return playlistid;
    }

    public void setPlaylistid(String playlistid) {
        this.playlistid = playlistid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getWatchtime() {
        return watchtime;
    }

    public void setWatchtime(int watchtime) {
        this.watchtime = watchtime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public int getTimeadded() {
        return timeadded;
    }

    public void setTimeadded(int timeadded) {
        this.timeadded = timeadded;
    }

    public int getViewsadded() {
        return viewsadded;
    }

    public void setViewsadded(int viewsadded) {
        this.viewsadded = viewsadded;
    }

    public String getChannelurl(){
        return "https://www.youtube.com/channel/" + channelid;
    }
}
