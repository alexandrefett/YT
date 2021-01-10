package com.fett.app;

import com.fett.app.models.Observer;
import com.fett.app.models.StatusModel;

import javax.swing.*;


class StatusViewLabel extends JLabel implements Observer {

    private final StatusModel model;

    public StatusViewLabel(StatusModel model) {

        this.model = model;
        this.setText(model.getData());
    }

    @Override
    public void onObservableChanged() { //update text in response to change in model
        this.setText(model.getData());
    }
}