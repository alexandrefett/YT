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
import com.fett.app.models.*;
import com.fett.app.exceptions.NoSuchTitleException;
import com.fett.app.utils.ProxyList;
import com.fett.app.utils.UserAgent;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Stream;

public abstract class BotWorker implements Runnable {

    final String apiKey;
    final Driver driverType;
    WebDriver webDriver;
    final Task task;
    final Database db;
    ViewCounter viewCounter;
    ProxyList  proxyList;
    ProxyModel proxy;
    final List<Video> videos;
    final String workerid;

    public BotWorker(
            String workerid,
            Task task,
            String apiKey,
            Database db,
            ViewCounter viewCounter,
            ProxyList proxyList,
            List<Video> videos){
        this.apiKey = apiKey;
        this.driverType = Driver.CHROME;
        this.task = task;
        this.db = db;
        this.viewCounter = viewCounter;
        this.videos = videos;
        this.workerid =workerid;
        this.proxyList = proxyList;

        proxy = new FalconProxy(apiKey);
    }

    void initializeBot() throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        this.setChromeDriver();
    }

    void setChromeDriver() throws IOException, URISyntaxException {
        UserAgent userAgent = new UserAgent(this.driverType);
        System.setProperty("webdriver.chrome.driver", "./chromedriver/chromedriver");
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.silentOutput", "true");
        ChromeOptions options = new ChromeOptions();
        List<String> chromeOptions = new ArrayList<>();
        LoggingPreferences logPrefs = new LoggingPreferences();

        options.addArguments("--proxy-server=\"http="+
                proxy.getCurrentProxy().getHttpProxy()+
                ";https="+proxy.getCurrentProxy().getHttpProxy()+"\"");
            chromeOptions.add(
                    String.format("--proxy-server=\"http="+
                            proxy.getCurrentProxy().getHttpProxy()+
                            ";https="+proxy.getCurrentProxy().getHttpProxy()+"\""));
            options.setCapability("proxy", proxy.getCurrentProxy());
            options.setProxy(proxy.getCurrentProxy());

        String a = userAgent.randomUA();
        chromeOptions.add(String.format("--user-agent=%s", a));
        chromeOptions.add("--mute-audio");
        chromeOptions.add("--incognito");
        options.addArguments("start-maximized");

        logPrefs.enable(LogType.BROWSER, Level.OFF);
        logPrefs.enable(LogType.PERFORMANCE, Level.OFF);
        logPrefs.enable(LogType.DRIVER, Level.OFF);
        logPrefs.enable(LogType.CLIENT, Level.OFF);
        logPrefs.enable(LogType.PROFILER, Level.OFF);
        logPrefs.enable(LogType.SERVER, Level.OFF);
        options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        options.addArguments(chromeOptions);
        if(task.getSpeed()==1){
            options.setHeadless(false);
        } else {
            options.setHeadless(true);
        }

        this.webDriver = new ChromeDriver(options);
    }

    boolean gotoYoutube(){
        try {
            this.webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            this.webDriver.get("https://www.youtube.com");
            new WebDriverWait(webDriver, 15).until(
                    wb -> ExpectedConditions.elementToBeClickable(wb.findElement(By.id("dismiss-button"))));
            this.webDriver
                    .findElement(By.id("dismiss-button"))
                    .findElement(By.xpath("//a[@class=\"yt-simple-endpoint style-scope yt-button-renderer\"]"))
                    .click();
        } catch (NoSuchElementException ex){
        } catch (TimeoutException ex) {
            return false;
        } catch (WebDriverException ex){
            return false;
        }

        try {
            new WebDriverWait(webDriver, 15).until(
                    wb -> ExpectedConditions.frameToBeAvailableAndSwitchToIt(wb.findElement(By.id("iframe"))));
            this.webDriver.switchTo().frame(
                    this.webDriver.findElement(By.id("iframe")));
            new WebDriverWait(this.webDriver, 15).until(wd ->
                    ExpectedConditions.elementToBeClickable(By.id("introAgreeButton")));
            this.webDriver.findElement(By.id("introAgreeButton")).click();
            this.webDriver.switchTo().defaultContent();
        } catch (NoSuchElementException ex){
            this.webDriver.switchTo().defaultContent();
        } catch (TimeoutException ex) {
        }
        return true;
    }

    abstract boolean searchVideo(Video video, Task task);

    int setVideo(Video video) throws InterruptedException {
        int whatchlength = task.getWatchtime();
        new WebDriverWait(webDriver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
        try{
            WebDriverWait wait = new WebDriverWait(webDriver, 30);
                wait.until(ExpectedConditions.titleIs(video.getTitle()+" - YouTube"));
            if (this.webDriver.getTitle().contains(video.getTitle())) {
                //WebElement playButton = this.webDriver.findElement(By.className("ytp-play-button"));
                try {
                    WebElement playButton = this.webDriver.findElement(By.id("player-container"));
                    //if (Objects.equals(playButton.getAttribute("title"), "Play (k)"))
                        //playButton.click();
                } catch (ElementClickInterceptedException e) {
                    //model.changeValue(workerName, numberOfWatches, e.getMessage(), s);
                    throw new ElementClickInterceptedException(e.getMessage());
                }

                if (whatchlength == 0) {
                    System.out.print(workerid+" - ");
                    System.out.println("Watching: "+video.getDuration()+" - "+video.getTitle());
                    return video.getDuration() *1000;
                } else {
                    Random r = new Random();
                    // Randomize watch duration every visits,
                    int w = (video.getDuration()/100) * whatchlength;
                    int rand = r.nextInt(w);
                    if(rand<60){
                        rand = 30 + r.nextInt(60);
                    }
                    System.out.print(workerid+" - ");
                    System.out.println("Watching: "+(rand)+" - "+video.getTitle());
                    return rand * 1000;
                }
            } else  {
                throw new NoSuchTitleException(String.format("Title does not end with YouTube, please ensure you have " +
                        "provided the correct URL to the video. Actual title: %s", this.webDriver.getTitle()));
            }
        } catch(NoSuchElementException ex){
//            System.out.println("NoSuchElementException");
        }
        return 0;
    }

    @Override
    public void run() {
        while(proxyList.proxyExist(proxy.getCurrentProxy().getHttpProxy())) {
            System.out.println("Proxy already used...");
            proxy.generateProxies();
        }
        Collections.shuffle(this.videos);

        Random r = new Random();
        int viewCount = 0;
        try {
            Thread.sleep(r.nextInt(5000));
            this.initializeBot();
            if (gotoYoutube()) {
                for (int i = 0; i < videos.size(); i++) {
                    try {
                        Video v = videos.get(i);
                        if (searchVideo(v, task)) {
                            int watchFor = setVideo(v);
                            db.addMonitoring(v, workerid, task.getId(), watchFor);
                            Thread.sleep(watchFor);
                            db.count(task, v, (watchFor / 1000));
                            viewCount = viewCounter.addViewCount();
                            if (viewCounter.getCount() >= task.getViews()) {
                                i = videos.size();
                            }
                            db.clearMonitoring(workerid, task.getId());
                        } else {
                            System.out.print(workerid+" - ");
                            System.out.println("Video not found: "+v.getTitle());
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
            if (this.webDriver != null) {
                this.webDriver.quit();
            }
        }
    }
}
