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

import com.fett.app.models.*;
import com.fett.app.exceptions.NoSuchTitleException;
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
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Stream;

public class BotWorker implements Runnable {

    private String workerName;
    private String apiKey;
    private Driver driverType;
    private Integer watchLength;
    private OrbitProxie proxies;
    private WebDriver webDriver;
    private File driverLocation;
    private UserAgent userAgent;
    private Integer numberOfWatches = 0;
    private final ArrayList<String> videos;
    private final ArrayList<String> ids;
    private final StatusModel model;

    public BotWorker(
            String workerName,
            ArrayList<String> videos,
            Driver driverType,
            Integer watchLength,
            File driverLocation,
            StatusModel model,
            String apiKey,
            ArrayList<String> ids
            ) throws IOException, URISyntaxException {
        this.workerName = workerName;
        this.apiKey = apiKey;
        this.driverType = driverType;
        this.watchLength = watchLength;
        this.driverLocation = driverLocation;
        this.model = model;
        this.videos = new ArrayList<String>(videos);
        this.ids=ids;
        model.changeValue(workerName, numberOfWatches, "Queued...", "-");
    }

    @SuppressWarnings("Duplicates")
    private void initializeBot() throws IOException, URISyntaxException {
        this.proxies = new OrbitProxie(this.workerName, this.apiKey);
        this.userAgent = new UserAgent(this.driverType);
        Collections.shuffle(videos);
        this.setChromeDriver();
    }

    private void setChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "./chromedriver/chromedriver");
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.silentOutput", "true");
        ChromeOptions options = new ChromeOptions();
        List<String> chromeOptions = new ArrayList<>();
        LoggingPreferences logPrefs = new LoggingPreferences();

        chromeOptions.add(String.format("--proxy-server=%s", proxies.getCurrentProxy().getHttpProxy()));
        String a = userAgent.randomUA();
        chromeOptions.add(String.format("--user-agent=%s", a));
        chromeOptions.add("--mute-audio");
        chromeOptions.add("--incognito");

        logPrefs.enable(LogType.BROWSER, Level.ALL);
        logPrefs.enable(LogType.PERFORMANCE, Level.OFF);
        logPrefs.enable(LogType.DRIVER, Level.ALL);
        logPrefs.enable(LogType.CLIENT, Level.OFF);
        logPrefs.enable(LogType.PROFILER, Level.OFF);
        logPrefs.enable(LogType.SERVER, Level.OFF);
        options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        options.addArguments(chromeOptions);
        options.setHeadless(true);
        options.setProxy(this.proxies.getCurrentProxy());

        options.setCapability("proxy", this.proxies.getCurrentProxy());
        this.webDriver = new ChromeDriver(options);
        model.changeValue(workerName, numberOfWatches, "Chrome Driver Set.", "-");
    }

    private boolean connectYoutube(){
        try {
            this.webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            this.webDriver.get("https://www.youtube.com");
            new WebDriverWait(webDriver, 15).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));
        } catch (WebDriverException ex){
            model.changeValue(workerName, numberOfWatches, "Connection failed", "-");
            return false;
        }
        try {
            Thread.sleep(2000);
            WebElement wl = this.webDriver.findElement(By.id("dismiss-button"));
            wl.findElement(By.xpath("//a[@class=\"yt-simple-endpoint style-scope yt-button-renderer\"]")).click();
            model.changeValue(workerName, numberOfWatches, "Pressing NO,THANKS", "-");
        } catch (NoSuchElementException | InterruptedException ex) {
            model.changeValue(workerName, numberOfWatches, "NO,THANKS not found. Continue...", "-");
        }
        try {
            Thread.sleep(3000);
            this.webDriver.switchTo().frame(
                    this.webDriver.findElement(By.id("iframe")));
            new WebDriverWait(this.webDriver, 5).until(
                    ExpectedConditions.elementToBeClickable(By.id("introAgreeButton"))).click();
            this.webDriver.switchTo().defaultContent();
            model.changeValue(workerName, numberOfWatches, "Accepting COOKIES...", "-");
            new WebDriverWait(webDriver, 15).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));
        } catch (NoSuchElementException | TimeoutException | InterruptedException ex){
            this.webDriver.switchTo().defaultContent();
            model.changeValue(workerName, numberOfWatches, "No COOKIES consent. Continue...", "-");
        }
        return true;
    }

    private boolean searchVideo(String video){
        try{
            WebElement element = this.webDriver.findElement(By.id("search"));

            element.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
            element.sendKeys(video + " CANAL ATAQUE TRIPLO");
            element.sendKeys(Keys.ENTER);
            new WebDriverWait(webDriver, 5).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));
            Thread.sleep(3000);
            List<WebElement> elements = this.webDriver.findElements(By.xpath("//a[@id=\"video-title\"]"));

            for(int i = 0;i<elements.size();i++){
                if(ids.contains(elements.get(i).getAttribute("href"))){
                    elements.get(i).click();
                    return true;
                }
            }
        } catch (WebDriverException | InterruptedException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private int setVideo(String s) throws InterruptedException {
        new WebDriverWait(webDriver, 15).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));

        try{
            if ( this.webDriver.getTitle().endsWith("YouTube") )
            {
                //WebElement playButton = this.webDriver.findElement(By.className("ytp-play-button"));
                try {
                    WebElement playButton = this.webDriver.findElement(By.id("player-container"));
                    //if (Objects.equals(playButton.getAttribute("title"), "Play (k)"))
                        //playButton.click();
                } catch (ElementClickInterceptedException e) {
                    model.changeValue(workerName, numberOfWatches, e.getMessage(), s);
                    throw new ElementClickInterceptedException(e.getMessage());
                }

                String currentVideoTime = this.webDriver.findElement(By.className("ytp-time-current")).getAttribute("innerHTML");
                String totalVideoTime = this.webDriver.findElement(By.className("ytp-time-duration")).getAttribute("innerHTML");
                if (this.watchLength == -1) {
                    return calculateWatchTime( currentVideoTime, totalVideoTime );
                } else {
                    // Randomize watch duration every visits,
                    int w = this.watchLength * 1000;
                    int max = w + 5000;
                    int min = w;
                    int range = max - min + min;
                    int rand = (int) (Math.random()* range) + (min / 2);
                    String pattern = "#,##,###.#";
                    DecimalFormat decimalFormat = new DecimalFormat(pattern);
                    String format = decimalFormat.format(rand / 1000);

                    model.changeValue(workerName, numberOfWatches, "Bot Watching now for "+format+" Seconds", s);
                    return rand;

                }
            } else  {
                model.changeValue(workerName, numberOfWatches, "reCaptcha Showing!!",s);
                throw new NoSuchTitleException(String.format("Title does not end with YouTube, please ensure you have " +
                        "provided the correct URL to the video. Actual title: %s", this.webDriver.getTitle()));
            }

        } catch(NoSuchElementException ex){
            model.changeValue(workerName, numberOfWatches, "Fail...",s);
        }
        return 0;
    }


    private Integer calculateWatchTime ( String currentTime, String videoLength )
    {
        int[] curHMS =  Stream.of(currentTime.split(":")).mapToInt(Integer::parseInt).toArray();
        int[] totalHMS =  Stream.of(videoLength.split(":")).mapToInt(Integer::parseInt).toArray();
        int currentTimeSeconds;
        int totalTimeSeconds;
        if ( curHMS.length < 3 )
            currentTimeSeconds = (60 * curHMS[0]) + curHMS[1];
        else
            currentTimeSeconds = (3600 * curHMS[0]) + (60 * curHMS[1]) + curHMS[2];
        if ( totalHMS.length < 3 )
            totalTimeSeconds = (60 * totalHMS[0]) + totalHMS[1];
        else
            totalTimeSeconds = (3600 * totalHMS[0]) + (60 * totalHMS[1]) + totalHMS[2];

        int watchLength = totalTimeSeconds - currentTimeSeconds;
        return watchLength * 1000;
    }

    @Override
    public void run() {
        Random r = new Random();
        try {
            this.initializeBot();
            Thread.sleep(r.nextInt(15000));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            model.changeValue(workerName, numberOfWatches, "Fail to initilize...",e.getMessage());
            e.printStackTrace();
            if (this.webDriver != null) {
                this.webDriver.quit();
            }
            return;
        }
        if(connectYoutube()) {
            for (int i = 0; i < videos.size(); i++) {
                try {
                    if(searchVideo(videos.get(i))) {
                        Thread.sleep(this.setVideo(videos.get(i)));
                        numberOfWatches++;
                        model.changeValue(workerName, numberOfWatches, "Finished video",videos.get(i));
                    } else {
                        model.changeValue(workerName, numberOfWatches, "Video not found...",videos.get(i));
                    }
                    //this.resetBot();
                } catch (InterruptedException e) {
                    model.changeValue(workerName, numberOfWatches, e.getMessage(),videos.get(i));
                }
            }
            model.changeValue(workerName, numberOfWatches, "Finishing worker...","-");
            if (this.webDriver != null) {
                this.webDriver.quit();
            }
        }
    }
}
