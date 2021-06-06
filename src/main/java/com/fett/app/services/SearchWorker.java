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
import com.fett.app.utils.UserAgent;
import org.openqa.selenium.*;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Stream;

public class SearchWorker extends BotWorker{

    public SearchWorker(
            String workerid,
            Task task,
            String apiKey,
            Database db,
            ViewCounter viewCounter,
            ProxyList  proxyList,
            List<Video> videos)  throws IOException, URISyntaxException {
        super(workerid, task, apiKey, db, viewCounter, proxyList, videos);

    }

    @Override
    boolean searchVideo(Video video, Task task){
        String channelName = task.getChannelname();
        try{
            WebElement element = this.webDriver.findElement(By.id("search"));
            element.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
            element.sendKeys(video.getTitle() + " CANAL "+channelName);
            element.sendKeys(Keys.ENTER);
            new WebDriverWait(webDriver, 10).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));
            Thread.sleep(3000);
            List<WebElement> elements = this.webDriver.findElements(By.xpath("//a[@id=\"video-title\"]"));

            for(int i = 0;i<elements.size();i++){
                if(video.getUrl().equals(elements.get(i).getAttribute("href"))){
                    elements.get(i).click();
                    return true;
                }
            }
        } catch (WebDriverException | InterruptedException ex){
            //System.out.println("WebDriverException | InterruptedException");
        }
        return false;
    }
}
