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
import com.fett.app.utils.FileUtil;
import com.fett.app.utils.UrlUtil;
import com.google.gson.JsonSyntaxException;
import org.openqa.selenium.Proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ListProxy extends ProxyModel {
    private OrbitProxyModel proxy;
    private List<String> listproxy;
    private int index;
    public ListProxy(String apiKey) {
        super(apiKey);
        this.listproxy = FileUtil.readFile("proxy.txt");
        this.index = 0;
        init();
    }

    @Override
    public void generateProxies()
    {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(listproxy.get(index));
        proxy.setSslProxy(listproxy.get(index));
        this.currentProxy = proxy;
        index++;
    }
}
