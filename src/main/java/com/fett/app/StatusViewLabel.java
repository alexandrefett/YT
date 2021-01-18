package com.fett.app;

import com.fett.app.models.Observer;
import com.fett.app.models.StatusModel;

import javax.swing.*;
import java.awt.*;


class StatusViewLabel extends JPanel implements Observer {

    private final StatusModel model;
    private JLabel status;
    private JLabel title;
    public StatusViewLabel(StatusModel model) {
        BoxLayout boxlayout = new BoxLayout(this, BoxLayout.Y_AXIS);// from left to rigth
        this.setLayout(boxlayout);
        this.setMaximumSize(new Dimension(400,35));
        this.setMinimumSize(new Dimension(400,35));
        this.model = model;
        this.status = new JLabel(model.getData());
        this.title = new JLabel(model.getTitle());
        this.add(status);
        this.add(title);
    }

    @Override
    public void onObservableChanged() { //update text in response to change in model
            this.status.setText(model.getData());
            this.status.revalidate();
            this.title.setText(model.getTitle());
            this.title.revalidate();
            this.revalidate();
    }
}