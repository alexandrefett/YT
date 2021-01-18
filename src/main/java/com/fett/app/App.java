/*
 *     JYTBot, YouTube viewer bot for educational purposes
 *     Copyright (C) 2019  Mark Tripoli (triippztech.com)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.fett.app;

import com.fett.app.models.Driver;
import com.fett.app.models.StatusModel;
import com.fett.app.services.BotWorker;
import com.fett.app.services.DriverConfiguration;
import com.fett.app.utils.FileUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    private static String apiKey;
    private static Driver driverType = Driver.CHROME;
    private static Integer watchLength;
    private static Integer numberOfWorkers;
    private static Integer viewsTarget;
    private static String channelURL;
    private static String[] schemes = {"http", "https"};
    private static StatusModel[] statusModels;
    private static ExecutorService executor;
    private static ArrayList<String> videos;
    private static ArrayList<String> ids;

    public static void main(String[] args) {

        initApp();
        YTViews frame = new YTViews(
                apiKey,
                channelURL,
                String.valueOf(numberOfWorkers),
                watchLength,
                videos.size());
        frame.setVisible(true);

        try {
            StatusModel[] statusModels = new StatusModel[viewsTarget];
            StatusViewLabel[] views = new StatusViewLabel[viewsTarget];

            boolean throttle = true;
            for (; ; ) {
                if (numberOfWorkers == 12) {
                    throttle = false;
                }
                if (numberOfWorkers == 3) {
                    throttle = true;
                }

                executor = Executors.newFixedThreadPool(numberOfWorkers);
                for (int i = 0; i < viewsTarget; i++) {
                    StatusModel statusModel = new StatusModel();
                    statusModels[i] = statusModel;
                    views[i] = new StatusViewLabel(statusModel);
                    statusModel.registerObserver(views[i]);
                    frame.getWorkerPanel().add(views[i]);
                    frame.getWorkerPanel().revalidate();

                    Runnable worker = new BotWorker(
                            "Worker " + (i + 1),
                            videos,
                            driverType,
                            watchLength,
                            DriverConfiguration.getDriver(),
                            statusModels[i],
                            apiKey,
                            ids);
                    executor.execute(worker);
                }
                frame.pack();
                executor.shutdown();
                // Wait until all threads are finish
                while (!executor.isTerminated()) {
                }
                System.out.println("\nFinishing threads");
                if (throttle) {
                    numberOfWorkers++;
                } else {
                    numberOfWorkers--;
                }
            }
        } catch (URISyntaxException | IOException ex) {
        }

    }

    private static void initApp() {
        Random r = new Random();
        videos = FileUtil.readFile("video_title.txt");
        ids = FileUtil.readFile("video_id.txt");
        ids.forEach(e -> ids.set(ids.indexOf(e), "https://www.youtube.com/watch?v=" + e));
        System.out.println("Videos loaded: " + videos.size());
        ArrayList<String> config = FileUtil.readFile("config.txt");
        channelURL = config.get(0);
        apiKey = config.get(1);
        numberOfWorkers = Integer.valueOf(config.get(2));
        viewsTarget = Integer.valueOf(config.get(3));
        watchLength = Integer.valueOf(config.get(4));
        viewsTarget = r.nextInt(viewsTarget) + 20;
    }
}
