package com.fett.app.models;

public class ViewLengthModel {
    private String key;
    private int length;

    public ViewLengthModel(String key, int length) {
        this.key = key;
        this.length = length;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString()
    {
        return key;
    }
}
