/*
 *     JYTBot, YouTube viewer bot for educational purposes
 *     Copyright (C) 2019  Mark Tripoli (triippztech.com)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.fett.app.models;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.fett.app.dao.HttpRequestSender;
import com.fett.app.dao.RequestSender;
import com.fett.app.http.Request;
import com.fett.app.http.Response;
import com.fett.app.utils.Log;
import com.fett.app.utils.UrlUtil;
import org.openqa.selenium.Proxy;

import java.io.IOException;
import java.util.*;

public class OrbitProxie {
    private final String protoType = "https";

    private OrbitProxies proxie;
    private String workerName;
    private String workerColor;
    private String apiKey;
    private Proxy currentProxy;

    private RequestSender sender;
    private Gson gson;

    public OrbitProxie(String workerName, String apiKey, String workerColor) {
        this.workerName = workerName;
        this.apiKey = apiKey;
        this.workerColor = workerColor;

        this.sender = new HttpRequestSender();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.generateProxies();
    }

    public String getProtoType() {
        return protoType;
    }

    public OrbitProxies getProxie() {
        return proxie;
    }

    public void setProxie(OrbitProxies proxie) {
        this.proxie = proxie;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerColor() {
        return workerColor;
    }

    public void setWorkerColor(String workerColor) {
        this.workerColor = workerColor;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Proxy getCurrentProxy() {
        return currentProxy;
    }

    public void setCurrentProxy(Proxy currentProxy) {
        this.currentProxy = currentProxy;
    }

    public RequestSender getSender() {
        return sender;
    }

    public void setSender(RequestSender sender) {
        this.sender = sender;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @SuppressWarnings("Duplicates")
    public void generateProxies()
    {
        HashMap<String, String> params = new HashMap<>();

        Request request = new Request(UrlUtil.buildUrlQuery(Constants.SearchEndpoint+apiKey, params));
        try {
            Response response = sender.sendRequest(request);
            String body = response.getBody();
            proxie = gson.fromJson(body, OrbitProxies.class);
            if(proxie.getWebsites().isYoutube()) {
                currentProxy = new Proxy();
                currentProxy.setHttpProxy(proxie.getCurl());
                Log.WINFO(workerName, workerColor, "Proxy::" + currentProxy.getHttpProxy());
            }
        } catch (JsonSyntaxException | IOException e) {
            Log.WERROR(workerName, workerColor, e.getMessage());
            generateProxies();
        }
    }
}
