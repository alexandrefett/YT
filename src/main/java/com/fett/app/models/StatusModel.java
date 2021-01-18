package com.fett.app.models;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StatusModel {
    private int views = 0;
    private String status = "-";
    private String id = "-";
    private String title = "-";

    public StatusModel(){}

    private final Set<Observer> mObservers = Collections.newSetFromMap(
            new ConcurrentHashMap<Observer, Boolean>(0));

    public void changeValue(String id, int value, String status, String title){
        this.views = value;
        this.status = status;
        this.id = id;
        this.title = title;
        notifyObservers();
    }
    synchronized int getViews() { return views; }
    synchronized void setViews(int views) {  this.views = views; }
    synchronized String getStatus() { return status; }
    synchronized void setStatus(String status) {  this.status = status; }
    synchronized String getId() { return id; }
    synchronized void setId(String id) {  this.id = id; }
    public synchronized String getTitle() { return title; }
    synchronized void setTitle(String title) {  this.title = title; }
    public synchronized String getData(){
        return new String (
    StringUtils.rightPad(id, 10) +
            " - " +
            StringUtils.rightPad(String.valueOf(views), 5) +
            " - " +
            StringUtils.rightPad(status, 25)
        );
    }

    //-- handle observers

    // add new Observer - it will be notified when Observable changes
    public void registerObserver(Observer observer) {
        if (observer != null) {
            mObservers.add((Observer) observer);
        }
    }

    //remove an Observer
    public void unregisterObserver(Observer observer) {
        if (observer != null) {
            mObservers.remove(observer);
        }
    }

    //notifies registered observers
    private void notifyObservers() {
        for (Observer observer : mObservers) {
            observer.onObservableChanged();
        }
    }
}

