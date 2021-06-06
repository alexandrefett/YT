package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelatedPlaylists {
    @SerializedName("likes")
    @Expose
    String likes;
    @SerializedName("favorites")
    @Expose
    String favorites;
    @SerializedName("uploads")
    @Expose
    String uploads;
    @SerializedName("watchHistory")
    @Expose
    String watchHistory;
    @SerializedName("watchLater")
    @Expose
    String watchLater;

    public String getLikes() {
        return likes;
    }

    public String getFavorites() {
        return favorites;
    }

    public String getUploads() {
        return uploads;
    }

    public String getWatchHistory() {
        return watchHistory;
    }

    public String getWatchLater() {
        return watchLater;
    }
}
