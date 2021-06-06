
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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FalconModel {
    @SerializedName("proxy")
    @Expose
    private String proxy;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("port")
    @Expose
    private int port;
    @SerializedName("connectionType")
    @Expose
    private String connectionType;
    @SerializedName("asn")
    @Expose
    private String asn;
    @SerializedName("isp")
    @Expose
    private String isp;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("lastChecked")
    @Expose
    private long lastChecked;
    @SerializedName("get")
    @Expose
    private boolean get;
    @SerializedName("post")
    @Expose
    private boolean post;
    @SerializedName("cookies")
    @Expose
    private boolean cookies;
    @SerializedName("referer")
    @Expose
    private boolean referer;
    @SerializedName("userAgent")
    @Expose
    private boolean userAgent;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("randomUserAgent")
    @Expose
    private String randomUserAgent;
    @SerializedName("requestsRemaining")
    @Expose
    private int requestsRemaining;

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getAsn() {
        return asn;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(long lastChecked) {
        this.lastChecked = lastChecked;
    }

    public boolean isGet() {
        return get;
    }

    public void setGet(boolean get) {
        this.get = get;
    }

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }

    public boolean isCookies() {
        return cookies;
    }

    public void setCookies(boolean cookies) {
        this.cookies = cookies;
    }

    public boolean isReferer() {
        return referer;
    }

    public void setReferer(boolean referer) {
        this.referer = referer;
    }

    public boolean isUserAgent() {
        return userAgent;
    }

    public void setUserAgent(boolean userAgent) {
        this.userAgent = userAgent;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRandomUserAgent() {
        return randomUserAgent;
    }

    public void setRandomUserAgent(String randomUserAgent) {
        this.randomUserAgent = randomUserAgent;
    }

    public int getRequestsRemaining() {
        return requestsRemaining;
    }

    public void setRequestsRemaining(int requestsRemaining) {
        this.requestsRemaining = requestsRemaining;
    }
}
