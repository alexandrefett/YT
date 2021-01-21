package com.fett.app.models;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Profile {
    private String id;
    private String name;
    private String channel;
    private ArrayList<Video> videos = new ArrayList<Video>();

    public Profile(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public ArrayList<Video> getVideos() {
        Collections.shuffle(videos);
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }
}
