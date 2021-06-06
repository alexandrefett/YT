package com.fett.app.utils;

import java.util.List;

public class ProxyList {
    private volatile List<String> list;
    public ProxyList(){
        this.list = FileUtil.readFile("proxylist.txt");
    }
    public void addProxy(String proxy){
        list.add(proxy);
        FileUtil.addToFile("proxylist.txt", proxy);
    }
    public List<String> getProxyList(){
        return list;
    }
    public boolean proxyExist(String proxy){
        if(list.contains(proxy)){
            return true;
        } else {
            addProxy(proxy);
        }
        return false;
    }
}
