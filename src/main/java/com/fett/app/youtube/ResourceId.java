package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResourceId {
    @SerializedName("kind")
    @Expose
    String kind;
    @SerializedName("videoId")
    @Expose
    String videoId;

    public String getKind() {
        return kind;
    }

    public String getVideoId() {
        return videoId;
    }
}
