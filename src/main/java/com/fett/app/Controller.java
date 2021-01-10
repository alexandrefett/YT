package com.fett.app;

import com.fett.app.models.StatusModel;

public class Controller {
    private int numOfWorkers;

    public Controller(int numOfWorkers) {
        this.numOfWorkers = numOfWorkers;
        StatusModel[] statusModels = new StatusModel[this.numOfWorkers];
        StatusViewLabel[] views = new StatusViewLabel[this.numOfWorkers];
        for (int i=0;i<this.numOfWorkers;i++){
            StatusModel statusModel = new StatusModel();
            statusModels[i] = statusModel;
            views[i] = new StatusViewLabel(statusModel);
            statusModel.registerObserver(views[i]);
        }
    }
}
