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

import com.fett.app.dao.HttpRequestSender;
import com.fett.app.dao.RequestSender;
import com.fett.app.http.Request;
import com.fett.app.http.Response;
import com.fett.app.models.Constants;
import com.fett.app.models.Driver;
import com.fett.app.models.StatusModel;
import com.fett.app.services.BotWorker;
import com.fett.app.services.DriverConfiguration;
import com.fett.app.utils.AnsiColors;
import com.fett.app.utils.UrlUtil;
import com.github.lalyos.jfiglet.FigletFont;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class App {
    private static String version = "0.0.1";
    private static String apiKey = "RgSnuSLsXCNwwkxAUBrKFhBSRuX8JMbXdkn6tRGOP08";
    private static Driver driverType = Driver.CHROME;
    private static Integer watchLength = 60;
    private static Integer numberOfWorkers = 5;

    private static String[] schemes = { "http","https" };
    private static UrlValidator urlValidator = new UrlValidator(schemes);
    private static StatusModel[] statusModels;
    private static ExecutorService executor;
    private static String[] videos = {
            "https://youtu.be/8sil0DpZE-c",
            "https://youtu.be/SVdSXJfPBAQ",
            "https://youtu.be/sCPIdfuomqs",
            "https://youtu.be/Zrjmn0PAFSU",
            "https://youtu.be/J2jtAmzp7es",
            "https://youtu.be/cBIi0GLPgJY",
            "https://youtu.be/kAw4Djrnufk",
            "https://youtu.be/PZCJ8mQMz6Y"
    };
    public static void main(String[] args) throws IOException, URISyntaxException {


        YTViews frame = new YTViews();
        frame.setVisible(true);

        showWelcome();
        //setVideoUrl();
        //setApiKey();
        //setDriverType();
        //setWatchLength();
        //setNumberOfWorkers();

        clearScreen();

        StatusModel[] statusModels = new StatusModel[numberOfWorkers];
        StatusViewLabel[] views = new StatusViewLabel[numberOfWorkers];

        executor = Executors.newFixedThreadPool(numberOfWorkers);
        for ( int i = 0; i < numberOfWorkers; i++)
        {
            StatusModel statusModel = new StatusModel();
            statusModels[i] = statusModel;
            views[i] = new StatusViewLabel(statusModel);
            statusModel.registerObserver(views[i]);
            frame.getWorkerPanel().add(views[i]);
            frame.getWorkerPanel().revalidate();
            frame.revalidate();

            Runnable worker = new BotWorker(
                    "Worker " + (i + 1),
                    apiKey,
                    videos,
                    driverType,
                    watchLength,
                    AnsiColors.randomForeground(),
                    DriverConfiguration.getDriver(),
                    statusModels[i]);
            executor.execute(worker);
        }

        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) { }
        System.out.println("\nFinished all threads");
    }

    public static void showWelcome() throws IOException {
        String display = FigletFont.convertOneLine("YTBot");

        System.out.print(AnsiColors.ANSI_GREEN + display + AnsiColors.ANSI_RESET);
        System.out.println();
        System.out.println(AnsiColors.ANSI_YELLOW + "Author: Alexandre Fett" + AnsiColors.ANSI_RESET);
        System.out.println(AnsiColors.ANSI_YELLOW + "Version: " + version + AnsiColors.ANSI_RESET);
        System.out.println(AnsiColors.ANSI_YELLOW + "License: GNU GPL v3" + AnsiColors.ANSI_RESET);
        System.out.println(AnsiColors.ANSI_YELLOW + "Repo: https://github.com/alexandrefett/YT.git" + AnsiColors.ANSI_RESET);
        System.out.println();
        System.out.println(AnsiColors.ANSI_BRIGHT_RED + "This application is meant for educational purposes only. " +
                "What you do with bot, is on you, I am not liable for anything you do with this."
                + AnsiColors.ANSI_RESET);
    }

    private static void setVideoUrl()
    {
        Scanner scanner = new Scanner(System.in);
        boolean validated = false;

        while (!validated)
        {
            System.out.print("What is the URL of the YouTube video?  -> ");
            String url = scanner.next();

            if (urlValidator.isValid(url)) {
                //videoUrl = url;
                validated = true;
            } else {
                System.out.println(AnsiColors.ANSI_BRIGHT_RED + "Invalid URL" + AnsiColors.ANSI_RESET);
            }
        }
    }

    private static void setApiKey()
    {
        Scanner scanner = new Scanner(System.in);
        boolean validated = false;

        while (!validated) {
            System.out.print("What is your API Key?  -> ");
            String key = scanner.next();

            if ( apiKeyValid(key) ) {
                apiKey = key;
                validated = true;
            } else {
                System.out.println(AnsiColors.ANSI_BRIGHT_RED + "Invalid API Key" + AnsiColors.ANSI_RESET);
                validated = false;
            }
        }
    }
    private static void setDriverType()
    {
        boolean validated = false;

        while (!validated) {
            System.out.println("Which driver would you like to use? (Enter a number 1-2)");
            System.out.println("1. Firefox");
            System.out.println("2. Chrome");
            System.out.print("->  ");

            try {
                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        driverType = Driver.FIREFOX;
                        validated = true;
                        break;
                    case 2:
                        driverType = Driver.CHROME;
                        validated = true;
                        break;
                    default:
                        System.out.println(AnsiColors.ANSI_BRIGHT_RED + "Invalid Selection" + AnsiColors.ANSI_RESET);
                        validated = false;
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(AnsiColors.ANSI_BRIGHT_RED + "Invalid Selection" + AnsiColors.ANSI_RESET);
                validated = false;
            }
        }
    }

    private static void setWatchLength() {
        boolean validated = false;

        while (!validated) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Would you like the bot to watch the FULL video or a set number of seconds? Select an item:");
            System.out.println("1. Full Length");
            System.out.println("2. Specific Time");
            System.out.print("->  ");
            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        watchLength = -1;
                        validated = true;
                        break;
                    case 2:
                        setWatchSeconds();
                        validated = true;
                        break;
                    default:
                        System.out.println(AnsiColors.ANSI_BRIGHT_RED + "Invalid Selection" + AnsiColors.ANSI_RESET);
                        validated = false;
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(AnsiColors.ANSI_BRIGHT_RED + "Invalid Selection" + AnsiColors.ANSI_RESET);
                validated = false;
            }
        }
    }

    private static void setWatchSeconds() {
        boolean validated = false;

        while (!validated) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("How long would you like to watch the video for, IN SECONDS? (Must be greater than 30 seconds)");
            System.out.print("->  ");
            try {
                int seconds = scanner.nextInt();

                if (seconds < 30) {
                    System.out.println(AnsiColors.ANSI_BRIGHT_RED + "Invalid Timeframe" + AnsiColors.ANSI_RESET);
                    validated = false;
                }
                else {
                    watchLength = seconds;
                    validated = true;
                }
            } catch (InputMismatchException e) {
                System.out.println(AnsiColors.ANSI_BRIGHT_RED + "Invalid Timeframe" + AnsiColors.ANSI_RESET);
                validated = false;
            }
        }
    }

    private static void setNumberOfWorkers()
    {
        boolean validated = false;

        while (!validated) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("How many workers would you like to use? (default = 3)");
            System.out.print("->  ");
            try {
                int workers = scanner.nextInt();
                if (workers < 1) {
                    System.out.println(AnsiColors.ANSI_BRIGHT_RED + "Must have 1 more workers" + AnsiColors.ANSI_RESET);
                    validated = false;
                }
                else if (workers > 20) {
                    System.out.println(AnsiColors.ANSI_BRIGHT_YELLOW + "I hope you know what you're doing" + AnsiColors.ANSI_RESET);
                    validated = true;
                    numberOfWorkers = workers;
                } else {
                    validated = true;
                    numberOfWorkers = workers;
                }
            } catch (InputMismatchException e) {
                System.out.println(AnsiColors.ANSI_BRIGHT_RED + "Must have 1 more workers" + AnsiColors.ANSI_RESET);
                validated = false;
            }
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @SuppressWarnings("Duplicates")
    private static Boolean apiKeyValid(String apiKey)
    {
        RequestSender sender= new HttpRequestSender();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HashMap<String, String> params = new HashMap<>();

//        params.put("access_key", apiKey);
        params.put("location","BR");
        params.put("youtube","true");

        Request request = new Request(UrlUtil.buildUrlQuery(Constants.SearchEndpoint, params));
        try {
            Response response = sender.sendRequest(request);
            return !response.getBody().contains("Invalid API");
        } catch (JsonSyntaxException | IOException e) {
            return false;
        }
    }
}
