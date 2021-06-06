package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Default {
    @SerializedName("url")
    @Expose
    String url;
    @SerializedName("width")
    @Expose
    int width;
    @SerializedName("height")
    @Expose
    int height;
}
