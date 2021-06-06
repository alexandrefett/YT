package com.fett.app.youtube;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class YouTubeAPI {
    private String apikey;
    private static OkHttpClient client = new OkHttpClient();

    public YouTubeAPI(String key) {
        this.apikey = key;
    }


    public YouTubeChannel getChannel(String channelId) throws Exception {
        String json = null;

        try {
            json = getJSONGET("https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics,contentDetails&id=" + channelId + "&key=" + this.apikey);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        Gson gson = new Gson();
        YouTubeChannel profile = (YouTubeChannel)gson.fromJson(json, YouTubeChannel.class);
        if (profile.kind == null) {
            throw new Exception();
        } else {
            return profile;
        }
    }

    public YouTubeChannel getPlaylist(String playlistId) throws Exception {
        String json = null;

        try {
            json = getJSONGET("https://www.googleapis.com/youtube/v3/playlistItems?maxResults=50&part=id,snippet,status,contentDetails&playlistId=" + playlistId + "&key=" + this.apikey);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        Gson gson = new Gson();
        YouTubeChannel profile = (YouTubeChannel)gson.fromJson(json, YouTubeChannel.class);
        if (profile.kind == null) {
            throw new Exception();
        } else {
            return profile;
        }
    }

    public YouTubeChannel getStats(String videosIds)  throws Exception {
        String json = null;

        try {
            json = getJSONGET("https://www.googleapis.com/youtube/v3/videos?part=id,snippet,statistics&id=" + videosIds + "&key=" + this.apikey);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        Gson gson = new Gson();
        YouTubeChannel profile = (YouTubeChannel)gson.fromJson(json, YouTubeChannel.class);
        if (profile.kind == null) {
            throw new Exception();
        } else {
            return profile;
        }
    }

    private static String getJSONGET(String url) throws IOException {
        Request request = (new Request.Builder()).url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}