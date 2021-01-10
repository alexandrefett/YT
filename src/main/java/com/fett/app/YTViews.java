package com.fett.app;

import com.fett.app.models.StatusModel;
import com.fett.app.models.ViewLengthModel;
import com.fett.app.models.WorkersModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class YTViews extends JFrame{
    private JTextField textField1;
    private JTextField textField2;
    private JButton startButton;
    private JComboBox mWorkers;
    private JComboBox mLength;
    private JPanel rootPanel;
    private JPanel workerPanel;


    public YTViews() {
        add(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        initPanel();
    }

    public JPanel getWorkerPanel(){
        return workerPanel;
    }

    private void initPanel(){
        workerPanel.setLayout(new BoxLayout(workerPanel, BoxLayout.Y_AXIS));
        mLength.addItem(new ViewLengthModel("30 secs", 30));
        mLength.addItem(new ViewLengthModel("60 secs", 60));
        mLength.addItem(new ViewLengthModel("120 secs", 120));
        mLength.addItem(new ViewLengthModel("240 secs", 240));
        mLength.setSelectedIndex(1);

        mWorkers.addItem(new WorkersModel("1 worker", 1));
        mWorkers.addItem(new WorkersModel("2 workers", 2));
        mWorkers.addItem(new WorkersModel("3 workers", 3));
        mWorkers.addItem(new WorkersModel("5 workers", 5));
        mWorkers.setSelectedIndex(0);

        String[] columns ={"Worker", "Status", "Count"};

        mWorkers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        revalidate();
    }

    public void addStatusModel(StatusModel[] statusModels){}
}
