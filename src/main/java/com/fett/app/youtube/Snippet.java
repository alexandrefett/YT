package com.fett.app.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snippet {
    @SerializedName("publishedAt")
    @Expose
    String publishedAt;
    @SerializedName("channelId")
    @Expose
    String channelId;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("thumbnails")
    @Expose
    Thumbnails thumbnails;
    @SerializedName("customUrl")
    @Expose
    String customUrl;
    @SerializedName("localized")
    @Expose
    Localized localized;
    @SerializedName("country")
    @Expose
    String country;
    @SerializedName("resourceId")
    @Expose
    ResourceId resourceId;

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public Localized getLocalized() {
        return localized;
    }

    public String getCountry() {
        return country;
    }

    public ResourceId getResourceId() {
        return resourceId;
    }
}
