package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YouTubeChannel {
    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("etag")
    @Expose
    public String etag;
    @SerializedName("items")
    @Expose
    private List<ChannelItems> items;
    @SerializedName("pageInfo")
    @Expose
    private PageInfo pageInfo;
    @SerializedName("nextPageToken")
    @Expose
    String nextPageToken;
    public YouTubeChannel() {
    }

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public List<ChannelItems> getItems() {
        return items;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }
}

