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


import com.fett.app.http.Request;
import com.fett.app.http.Response;
import com.fett.app.utils.UrlUtil;
import com.google.gson.JsonSyntaxException;
import org.openqa.selenium.Proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class FalconProxy extends ProxyModel {
    private FalconModel proxy;
    public FalconProxy(String apiKey) {
        super(apiKey);
        init();
    }


    @Override
    public void generateProxies()
    {
        String[] country = {"CA","US","ES","GB","DE","PT","PT","PT","FR","IT"};
        Random r = new Random();
        HashMap<String, String> params = new HashMap<>();
        Request request;
        int local = r.nextInt(100);
   //     if(local <= 60){
   //         request = new Request(UrlUtil.buildUrlQuery(Constants.FalconEndpoint+this.apikey+"&country=BR&connectionType=Residential&userAgent=true&get=true", params));
   //     } else {
            request = new Request(UrlUtil.buildUrlQuery(Constants.FalconEndpoint+this.apikey+"&connectionType=Residential&userAgent=true&get=true", params));
  //      }
        try {
            Response response = sender.sendRequest(request);
            String body = response.getBody();
            proxy = gson.fromJson(body, FalconModel.class);
            this.currentProxy = new Proxy();
            this.currentProxy.setAutodetect(false);
            this.currentProxy.setProxyType(Proxy.ProxyType.MANUAL);
            this.currentProxy.setHttpProxy(proxy.getProxy());
            this.currentProxy.setSslProxy(proxy.getProxy());
        } catch (JsonSyntaxException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
