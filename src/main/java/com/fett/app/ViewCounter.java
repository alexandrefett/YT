package com.fett.app;

public class ViewCounter {
    private volatile int count;
    public ViewCounter(int count){
        this.count=count;
    }
    public int addViewCount(){
        count++;
        return count;
    }
    public int getCount(){
        return count;
    }
}