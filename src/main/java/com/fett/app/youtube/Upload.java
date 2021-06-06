package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Upload {
    @SerializedName("videoId")
    @Expose
    String videoId;
}
