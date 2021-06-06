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
import com.fett.app.utils.UrlUtil;
import org.openqa.selenium.Proxy;

import java.io.IOException;
import java.util.*;

public class Proxies {
    private final String protoType = "https";

    private PubProxies proxies;
    private Set<Datum> usedProxies;
    private String workerName;
    private String workerColor;
    private String apiKey;
    private Datum currentProxyModel;
    private Proxy currentProxy;

    private RequestSender sender;
    private Gson gson;

    public Proxies(String workerName, String apiKey, String workerColor) {
        this.workerName = workerName;
        this.apiKey = apiKey;
        this.workerColor = workerColor;
        this.usedProxies = new HashSet<>();

        this.sender = new HttpRequestSender();
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        this.generateProxies();
    }

    @SuppressWarnings("Duplicates")
    public void generateProxies()
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("api", apiKey);
        params.put("limit", "20");
        params.put("https", "true");
        params.put("format", "json");

        Request request = new Request(UrlUtil.buildUrlQuery(Constants.PubEndpoint, params));
        try {
            Response response = sender.sendRequest(request);
            String body = response.getBody();
            proxies = gson.fromJson(body, PubProxies.class);
        } catch (JsonSyntaxException | IOException e) {
            generateProxies();
        }

        // Set the first
        this.loadNewProxy();
    }

    public void rotateProxies()
    {
        // Add the current proxy to the used Set
        this.usedProxies.add(this.currentProxyModel);
        // Load a new one
        this.loadNewProxy();
    }

    private void refreshProxies()
    {
        this.proxies = null;
        this.generateProxies();
    }

    // fix randomproxy problem
    private void loadNewProxy() {
        this.usedProxies.add(this.getCurrentProxyModel());
        Datum proxy = randomProxy();
            if ( isUsed(proxy) ) {
                try {
                    Thread.sleep(3000);
                    this.refreshProxies();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                this.setCurrentProxyModel(proxy);
                this.setCurrentProxy(proxy);
            }

    }



    private Boolean isUsed(Datum proxy)
    {
        return usedProxies.contains(proxy);
    }

    private Datum randomProxy() {
        return proxies.getData().get(new Random().nextInt(proxies.getData().size()));
    }

    public List<Datum> getProxies() {
        return proxies.getData();
    }

    public Set<Datum> getUsedProxies() {
        return usedProxies;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Datum getCurrentProxyModel() {
        return currentProxyModel;
    }

    public void setCurrentProxyModel(Datum currentProxyModel) {
        this.currentProxyModel = currentProxyModel;
    }

    public Proxy getCurrentProxy() {
        return currentProxy;
    }

    public void setCurrentProxy(Datum currentProxy) {
        Proxy proxy = new Proxy();
        System.out.println("Http::"+currentProxy.getIpPort());
        proxy.setHttpProxy(currentProxy.getIpPort());
        proxy.setSslProxy(currentProxy.getIpPort());
        this.currentProxy = proxy;
    }
}
