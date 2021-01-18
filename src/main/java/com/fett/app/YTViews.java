package com.fett.app;

import com.fett.app.models.StatusModel;
import com.fett.app.models.WorkersModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class YTViews extends JFrame{
    private JTextField mChannelURL;
    private JTextField mApiKey;
    private JButton startButton;
    private JComboBox mWorkers;
    private JComboBox mLength;
    private JPanel rootPanel;
    private JPanel workerPanel;
    private JLabel lChannel;
    private JLabel lworkers;
    private JLabel lWatchLength;
    private JLabel lVideosAdded;
    private JLabel lApikey;
    private String apikey;
    private String channelURL;

    public YTViews(String apikey, String channelURL, String workers, int watchLentgh, int videos) {
        add(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lChannel.setText(channelURL);
        lApikey.setText(apikey);
        lworkers.setText(workers);
        if(watchLentgh == -1){
            lWatchLength.setText("All length video");
        } else {
            lWatchLength.setText(watchLentgh +" secs (average)");
        }
        lVideosAdded.setText(String.valueOf(videos));

        pack();
        initPanel();
    }

    public JPanel getWorkerPanel(){
        return workerPanel;
    }
    public void setAction(ActionListener startListener){
        startButton.addActionListener(startListener);
    }

    private void initPanel(){
        workerPanel.setLayout(new BoxLayout(workerPanel, BoxLayout.Y_AXIS));
        revalidate();
    }

}
