package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelItems {
    @SerializedName("kind")
    @Expose
    String kind;
    @SerializedName("etag")
    @Expose
    String etag;
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("snippet")
    @Expose
    Snippet snippet;
    @SerializedName("contentDetails")
    @Expose
    ContentDetails contentDetails;
    @SerializedName("statistics")
    @Expose
    Statistics statistics;
    @SerializedName("status")
    @Expose
    Status status;

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public String getId() {
        return id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public ContentDetails getContentDetails() {
        return contentDetails;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public Status getStatus() {
        return status;
    }
}
