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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ChannelWorker extends BotWorker{

    public ChannelWorker(
            String workerid,
            Task task,
            String apiKey,
            Database db,
            ViewCounter viewCounter,
            ProxyList proxyList, List<Video> videos)  throws IOException, URISyntaxException {
        super(workerid, task, apiKey, db, viewCounter, proxyList, videos);
    }

    @Override
    boolean searchVideo(Video video, Task task) {
        return false;
    }

    boolean searchVideoOnChannel(Video video, Task task) {
        try {
            return openVideo(video);
        } catch (StaleElementReferenceException ex){
            //System.out.println(ex.getMessage());
        } catch (WebDriverException ex){
            //System.out.println(ex.getMessage());
        }
        return false;
    }

    private boolean openVideo(Video video){
        try{
            new WebDriverWait(webDriver, 30).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));


            long lastHeight = (long) ((JavascriptExecutor) webDriver).executeScript("return document.body.scrollHeight");

            while (true) {
                ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(5000);

                long newHeight = (long) ((JavascriptExecutor) webDriver).executeScript("return document.body.scrollHeight");
                if (newHeight == lastHeight) {
                    break;
                }
                lastHeight = newHeight;
            }
            List<WebElement> elements = webDriver.findElements(By.xpath("//a[@id=\"video-title\"]"));
            for (WebElement element : elements) {
                if (video.getUrl().equals(element.getAttribute("href"))) {
                    element.click();
                    return true;
                }
            }
            return false;
        }  catch (WebDriverException ex){
            // System.out.println(ex.getMessage());
        } catch (InterruptedException e) {
            // System.out.println(e.getMessage());
        }
        return false;
    }

    boolean gotoYoutube(){
        try {
            webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            webDriver.get("https://www.youtube.com");
            new WebDriverWait(webDriver, 15).until(
                    wb -> ExpectedConditions.elementToBeClickable(wb.findElement(By.id("dismiss-button"))));
            webDriver
                    .findElement(By.id("dismiss-button"))
                    .findElement(By.xpath("//a[@class=\"yt-simple-endpoint style-scope yt-button-renderer\"]"))
                    .click();
        } catch (NoSuchElementException ex){
        } catch (WebDriverException ex) {
            return false;
        }

        try {
            new WebDriverWait(webDriver, 15).until(
                    wb -> ExpectedConditions.frameToBeAvailableAndSwitchToIt(wb.findElement(By.id("iframe"))));
            webDriver.switchTo().frame(
                    webDriver.findElement(By.id("iframe")));
            new WebDriverWait(webDriver, 15).until(wd ->
                    ExpectedConditions.elementToBeClickable(By.id("introAgreeButton")));
            webDriver.findElement(By.id("introAgreeButton")).click();
            webDriver.switchTo().defaultContent();
        } catch (NoSuchElementException ex){
            webDriver.switchTo().defaultContent();
        } catch (TimeoutException ignored) {
        }
        return true;
    }

    boolean gotoChannel(Task t, Video v){
        try {
            webDriver.get("https://www.youtube.com/channel/"+task.getChannelid()+"/videos");
            new WebDriverWait(webDriver, 30).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));
            Thread.sleep(2000);
            String pageTitle = webDriver.getTitle();
            if(pageTitle.contains(t.getChannelname())){
                return true;
            }
        } catch (WebDriverException | InterruptedException ex) {
            return false;
        }
        return false;
    }

    @Override
    public void run() {
        while(proxyList.proxyExist(proxy.getCurrentProxy().getHttpProxy())) {
            System.out.println("Proxy already used...");
            proxy.generateProxies();
        }
        Collections.shuffle(videos);

        Random r = new Random();
        int viewCount = 0;
        try {
            Thread.sleep(r.nextInt(5000));
            initializeBot();
            if (gotoYoutube()) {
                for (int i = 0; i < videos.size(); i++) {
                    try {
                        Video v = videos.get(i);
                        if(gotoChannel(task, v)) {
                            if (openVideo(v)) {
                                int watchFor = setVideo(v);
                                db.addMonitoring(v, workerid, task.getId(), watchFor);
                                Thread.sleep(watchFor);
                                db.count(task, v, (watchFor / 1000));
                                viewCount = viewCounter.addViewCount();
                                if (viewCounter.getCount() >= task.getViews()) {
                                    i = videos.size();
                                }
                                db.clearMonitoring(workerid, task.getId());
                                webDriver.navigate().back();
                            } else {
                                System.out.print(workerid + " - ");
                                System.out.println("Video not found: " + v.getTitle());
                            }
                        }
                    } catch (InterruptedException e) {
                        //System.out.println("InterruptedException");
                    }
                }
                if (viewCounter.getCount() < task.getViews()) {
                    System.out.print(workerid+" - ");
                    System.out.println("Worker ended with "+viewCount+" views");
                }
            }
            System.out.print(workerid+" - ");
            System.out.println("Worker finished by counter with "+viewCount+" views");
        } catch (URISyntaxException | IOException | InterruptedException | ExecutionException e) {
            //System.out.println("URISyntaxException | IOException | InterruptedException | ExecutionException");
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }
}
