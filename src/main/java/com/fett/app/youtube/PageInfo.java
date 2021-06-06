package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageInfo {
    @SerializedName("totalResults")
    @Expose
    int totalResults;
    @SerializedName("resultsPerPage")
    @Expose
    int resultsPerPage;
}
