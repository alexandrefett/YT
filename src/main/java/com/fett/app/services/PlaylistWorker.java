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
import com.fett.app.exceptions.NoSuchTitleException;
import com.fett.app.models.*;
import com.fett.app.utils.ProxyList;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class PlaylistWorker extends BotWorker{

    public PlaylistWorker(
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
    boolean searchVideo(Video video, Task task) {
        return false;
    }

    boolean gotoChannel(Task task){
        try{
            WebElement element = this.webDriver.findElement(By.id("search"));
            element.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
            element.sendKeys("CANAL "+task.getChannelname());
            element.sendKeys(Keys.ENTER);
            new WebDriverWait(webDriver, 30).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));
            Thread.sleep(3000);
            List<WebElement> elements = this.webDriver.findElements(By.xpath("//a[@id=\"main-link\"]"));

            for(int i = 0;i<elements.size();i++){
                if(task.getChannelurl().equals(elements.get(i).getAttribute("href"))){
                    elements.get(i).click();
                    return true;
                }
            }
        } catch (WebDriverException | InterruptedException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private int watchVideo(){
        int whatchlength = (int)this.task.getWatchtime();
        try{
            new WebDriverWait(webDriver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));

            if ( this.webDriver.getTitle().endsWith("YouTube") )
            {
                try {
                    WebElement playButton = this.webDriver.findElement(By.id("player-container"));
                } catch (ElementClickInterceptedException e) {
                    throw new ElementClickInterceptedException(e.getMessage());
                }
                String currentVideoTime = this.webDriver.findElement(By.className("ytp-time-current")).getAttribute("innerHTML");
                String totalVideoTime = this.webDriver.findElement(By.className("ytp-time-duration")).getAttribute("innerHTML");

                    // Randomize watch duration every visits,
                    int w = whatchlength * 1000;
                    int max = w + 5000;
                    int min = w;
                    int range = max - min + min;
                    int rand = (int) (Math.random()* range) + (min / 2);

                    if(rand<=30000){
                        rand = 45000;
                    }
                    return rand;

            } else  {
                throw new NoSuchTitleException(String.format("Title does not end with YouTube, please ensure you have " +
                        "provided the correct URL to the video. Actual title: %s", this.webDriver.getTitle()));
            }
        } catch(NoSuchElementException ex){
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    boolean nextVideo(){
        try{
            new WebDriverWait(webDriver, 30).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));
            this.webDriver.findElement(By.xpath("//a[@class=\"ytp-next-button ytp-button\"]")).click();
            return true;
        } catch(WebDriverException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    boolean gotoPlaylist(Task task){
        try {
            new WebDriverWait(webDriver, 30).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));
            List<WebElement> elements =
                    this.webDriver.findElements(By.xpath("//div[@class=\"tab-content style-scope paper-tab\"]"));

            for (WebElement e : elements){
                if(e.getText().equals("PLAYLISTS")){
                    e.click();
                    return true;
                }
            }
        } catch (WebDriverException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    @Override
    public void run(){
        Random r = new Random();
        int viewCount = 0;
        int videoCount = videos.size();
        try {
            System.out.println("init");
            this.initializeBot();
            System.out.println("youtube");
            if (gotoYoutube()) {
                Thread.sleep(r.nextInt(20000));
                if(gotoChannel(task)){
                    Thread.sleep(1000);
                    if(gotoPlaylist(task)) {
                        Thread.sleep(1000);
                        for (int i = 0; i < videoCount; i++) {
                            int watchFor = watchVideo();
                            System.out.println("Whatching: "+(watchFor/1000)+" - "+this.webDriver.getTitle());
                            Thread.sleep(watchFor);
                            if (watchFor >= 30000) {
                                //db.count(task, (watchFor / 1000));
                                viewCount = viewCounter.addViewCount();
                                if (viewCounter.getCount() >= task.getViews()) {
                                    i = videoCount;
                                }
                            }
                            nextVideo();
                        }
                        System.out.println("Worker finished by counter with " + viewCount + " views");
                    }else {
                        System.out.println("Playlist not fount");
                    }
                }else {
                    System.out.println("Channel not found");
                }
            } else {
                System.out.println("Can't connect to Youtube");
            }
        } catch (URISyntaxException | IOException | InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
        } finally {
            if (this.webDriver != null) {
                this.webDriver.quit();
            }
        }
    }
}
