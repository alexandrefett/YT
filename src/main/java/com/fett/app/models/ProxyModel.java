package com.fett.app.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fett.app.dao.HttpRequestSender;
import com.fett.app.dao.RequestSender;
import org.openqa.selenium.Proxy;

public abstract class ProxyModel {
    Proxy currentProxy;
    String apikey;
    RequestSender sender;
    Gson gson;
    final String protoType = "https";

    public ProxyModel(String apikey) {
        this.apikey = apikey;
        this.sender = new HttpRequestSender();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    void init(){
        this.generateProxies();
    }

    public Proxy getCurrentProxy() {
        return currentProxy;
    }

    public void setCurrentProxy(Proxy cuurentProxy) {
        this.currentProxy = currentProxy;
    }

    public abstract void generateProxies();
}
