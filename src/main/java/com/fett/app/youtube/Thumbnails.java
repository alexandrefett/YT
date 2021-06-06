package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnails {
    @SerializedName("thumbnailsDefault")
    @Expose
    Default thumbnailsDefault;
    @SerializedName("medium")
    @Expose
    Default medium;
    @SerializedName("high")
    @Expose
    Default high;
    @SerializedName("standard")
    @Expose
    Default standard;
    @SerializedName("maxres")
    @Expose
    Default maxres;

}
