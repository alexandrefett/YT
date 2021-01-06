package com.fett.app.models;

public class WorkersModel {
    private String key;
    private int value;

    public WorkersModel(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return key;
    }
}
