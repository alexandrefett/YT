package com.fett.app.models;

//Interface implemented by View and used by Model
public interface Observer {
    void onObservableChanged();
}
