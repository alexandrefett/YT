package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentDetails {
    @SerializedName("relatedPlaylists")
    @Expose
    RelatedPlaylists relatedPlaylists;
    @SerializedName("upload")
    @Expose
    Upload upload;
    @SerializedName("duration")
    @Expose
    String duration;

    public RelatedPlaylists getRelatedPlaylists() {
        return relatedPlaylists;
    }

    public Upload getUpload() {
        return upload;
    }

    public String getDuration() {
        return duration;
    }
}
