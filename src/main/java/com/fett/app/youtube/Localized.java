package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Localized {
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("description")
    @Expose
    String description;
}
