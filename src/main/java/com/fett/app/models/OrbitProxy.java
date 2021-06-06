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


import com.google.gson.JsonSyntaxException;
import com.fett.app.http.Request;
import com.fett.app.http.Response;
import com.fett.app.utils.UrlUtil;
import org.openqa.selenium.Proxy;

import java.io.IOException;
import java.util.*;

public class OrbitProxy extends ProxyModel {
    private OrbitProxyModel proxy;
    public OrbitProxy(String apiKey) {
        super(apiKey);
        init();
    }


    @Override
    public void generateProxies()
    {
        HashMap<String, String> params = new HashMap<>();
        Random r = new Random();
        Request request;
        int local = r.nextInt(100);
        if(local <= 70){
            request = new Request(UrlUtil.buildUrlQuery(Constants.OrbitEndpoint+this.apikey+"&location=BR&youtube=true", params));
        } else {
            request = new Request(UrlUtil.buildUrlQuery(Constants.OrbitEndpoint+this.apikey+"&youtube=true", params));
        }
        try {
            Response response = sender.sendRequest(request);
            String body = response.getBody();
            proxy = gson.fromJson(body, OrbitProxyModel.class);
            if(proxy.getWebsites().isYoutube()) {
                this.currentProxy = new Proxy();
                this.currentProxy.setHttpProxy(proxy.getCurl());
            }
        } catch (JsonSyntaxException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
