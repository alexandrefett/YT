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

package com.fett.app.services;

import com.fett.app.Database;
import com.fett.app.ViewCounter;
import com.fett.app.models.Task;
import com.fett.app.models.Video;
import com.fett.app.utils.ProxyList;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class DirectWorker extends BotWorker{

    public DirectWorker(
            String workerid,
            Task task,
            String apiKey,
            Database db,
            ViewCounter viewCounter,
            ProxyList proxyList,
            List<Video> videos)  throws IOException, URISyntaxException {
        super(workerid, task, apiKey, db, viewCounter, proxyList, videos);
    }

    @Override
    public void run() {
        int viewCount = 0;
        try {
            this.initializeBot();
            if(gotoYoutube()) {
                for (int i = 0; i < videos.size(); i++) {
                    try {
                        Video v = videos.get(i);
                        if (searchVideo(v, task)) {
                            int watchFor = setVideo(v);
                            db.addMonitoring(v, workerid, task.getId(), watchFor);
                            Thread.sleep(watchFor);
                            db.count(task, v, (watchFor / 1000));
                            viewCount = viewCounter.addViewCount();
                            db.clearMonitoring(workerid, task.getId());
                            if (viewCounter.getCount() >= task.getViews()) {
                                i = videos.size();
                            }
                        } else {
                            System.out.print(workerid+" - ");
                            System.out.println("Video not found: " + v.getTitle());
                        }
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (viewCounter.getCount() < task.getViews()) {
                    System.out.print(workerid+" - ");
                    System.out.println("Worker ended with " + viewCount + " views");
                }
            }
            System.out.print(workerid+" - ");
            System.out.println("Worker not started: no response from youtube");
        } catch (URISyntaxException | IOException | InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
        } finally {
            if (this.webDriver != null) {
                this.webDriver.quit();
            }
        }
    }

    @Override
    boolean searchVideo(Video video, Task task) {
        try {
            this.webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            this.webDriver.get(video.getUrl());

            new WebDriverWait(webDriver, 15).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));

            if (this.webDriver.getTitle().contains(video.getTitle())) {
                return true;
            }
/*
            new WebDriverWait(webDriver, 30).until(
                    wb -> ExpectedConditions.elementToBeClickable(wb.findElement(By.id("dismiss-button"))));
            this.webDriver
                    .findElement(By.id("dismiss-button"))
                    .findElement(By.xpath("//a[@class=\"yt-simple-endpoint style-scope yt-button-renderer\"]"))
                    .click();

            new WebDriverWait(webDriver, 30).until(
                    wb -> ExpectedConditions.frameToBeAvailableAndSwitchToIt(wb.findElement(By.id("iframe"))));
            this.webDriver.switchTo().frame(
                    this.webDriver.findElement(By.id("iframe")));
            new WebDriverWait(this.webDriver, 30).until(wd ->
                    ExpectedConditions.elementToBeClickable(By.id("introAgreeButton")));
            this.webDriver.findElement(By.id("introAgreeButton")).click();
            this.webDriver.switchTo().defaultContent();

 */
        }  catch (TimeoutException ex) {
            return false;
        } catch (WebDriverException ex) {
            return false;
        }
        return false;
    }
}
