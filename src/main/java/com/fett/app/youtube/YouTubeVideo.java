package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YouTubeVideo {
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("url")
    @Expose
    String url;
}
