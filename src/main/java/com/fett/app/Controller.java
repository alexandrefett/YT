package com.fett.app;

import com.fett.app.models.StatusModel;

public class Controller {
    private int numOfWorkers;

    public Controller(int numOfWorkers) {
        this.numOfWorkers = numOfWorkers;
        StatusModel[] statusModels = new StatusModel[this.numOfWorkers];
        View[] views = new View[this.numOfWorkers];
        for (int i=0;i<this.numOfWorkers;i++){
            StatusModel statusModel = new StatusModel();
            statusModels[i] = statusModel;
//            View view = new View(statusModel);
        }
    }
}
