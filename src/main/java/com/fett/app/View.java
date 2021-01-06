package com.fett.app;

import com.fett.app.models.Observer;
import com.fett.app.models.StatusModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class View implements Observer {

    private final StatusModel model;
    private final DataPane pane;
    private final JButton stopBtn;
    private DefaultTableModel tableModel;

    public View(StatusModel model, DefaultTableModel tableModel) {

        this.model = model;
        this.tableModel = tableModel;

        tableModel.addRow(model.getData());

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pane = new DataPane();
        frame.add(pane, BorderLayout.CENTER);

        stopBtn = new JButton("Stop");
        frame.add(stopBtn, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    JButton getStopBtn()  { return stopBtn; }

    @Override
    public void onObservableChanged() { //update text in response to change in model
        //pane.setText(String.format("%.2f",model.getValue()));
    }

    class DataPane extends JPanel {

        private final JLabel label;

        DataPane() {
            setPreferredSize(new Dimension(200, 100));
            setLayout(new GridBagLayout());
            label = new JLabel(" ");
            add(label);
        }

        void setText(String text){  label.setText(text); }
    }
}