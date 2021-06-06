package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistics {
    @SerializedName("viewCount")
    @Expose
    String viewCount;
    @SerializedName("commentCount")
    @Expose
    String commentCount;
    @SerializedName("subscriberCount")
    @Expose
    String subscriberCount;
    @SerializedName("hiddenSubscriberCount")
    @Expose
    boolean hiddenSubscriberCount;
    @SerializedName("videoCount")
    @Expose
    String videoCount;

    public String getViewCount() {
        return viewCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getSubscriberCount() {
        return subscriberCount;
    }

    public boolean isHiddenSubscriberCount() {
        return hiddenSubscriberCount;
    }

    public String getVideoCount() {
        return videoCount;
    }
}
