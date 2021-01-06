package com.fett.app.models;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StatusModel {
    private int views =0;
    private String status = "";
    private String id = "";

    public StatusModel(){}

    private final Set<Observer> mObservers = Collections.newSetFromMap(
            new ConcurrentHashMap<Observer, Boolean>(0));

    public void changeValue(String id, int value, String status){
        this.views = value;
        this.status = status;
        this.id = id;
        notifyObservers();
    }
    synchronized int getViews() { return views; }
    synchronized void setViews(int views) {  this.views = views; }
    synchronized String getStatus() { return status; }
    synchronized void setStatus(String status) {  this.status = status; }
    synchronized String getId() { return id; }
    synchronized void setId(String id) {  this.id = id; }
    public synchronized Object[] getData(){
        return new Object[]{id,views, status};
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

