package com.fett.app;

import javax.swing.*;

public class VideosForm extends  JFrame{
    private JTextArea videosArea;
    private JButton bSave;
    private JButton bCancel;
    private JPanel videosForm;
    private String[] videos;

    public VideosForm(String[] videos) {
        add(videosForm);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.videos = videos;
        init();
        pack();
    }

    private void init(){
    }
}
