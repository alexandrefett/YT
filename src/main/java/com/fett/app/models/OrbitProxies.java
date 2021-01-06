
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

public class OrbitProxies {

    @SerializedName("anonymous")
    @Expose
    private boolean anonymous;
    @SerializedName("cookies")
    @Expose
    private boolean cookies;
    @SerializedName("curl")
    @Expose
    private String curl;
    @SerializedName("get")
    @Expose
    private boolean get;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("isp")
    @Expose
    private String iso;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("lastChecked")
    @Expose
    private double lastChecked;
    @SerializedName("port")
    @Expose
    private int port;
    @SerializedName("post")
    @Expose
    private boolean post;
    @SerializedName("protocol")
    @Expose
    private String protocol;
    @SerializedName("rtt")
    @Expose
    private double rtt;
    @SerializedName("ssl")
    @Expose
    private boolean ssl;
    private Websites websites;

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean isCookies() {
        return cookies;
    }

    public void setCookies(boolean cookies) {
        this.cookies = cookies;
    }

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl;
    }

    public boolean isGet() {
        return get;
    }

    public void setGet(boolean get) {
        this.get = get;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(double lastChecked) {
        this.lastChecked = lastChecked;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public double getRtt() {
        return rtt;
    }

    public void setRtt(double rtt) {
        this.rtt = rtt;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public Websites getWebsites() {
        return websites;
    }

    public void setWebsites(Websites websites) {
        this.websites = websites;
    }
}
