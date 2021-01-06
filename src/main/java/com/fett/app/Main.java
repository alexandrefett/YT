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

import com.fett.app.models.StatusModel;
import com.fett.app.models.Driver;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;
import java.util.concurrent.ExecutorService;


public class Main {
    private static String version = "0.0.1";
    private static String videoUrl = "https://tuneca2011.wixsite.com/video1";
    private static String apiKey = "RgSnuSLsXCNwwkxAUBrKFhBSRuX8JMbXdkn6tRGOP08";
    private static Driver driverType = Driver.CHROME;
    private static Integer watchLength = 120;
    private static Integer numberOfWorkers = 5;

    private static String[] schemes = { "http","https" };
    private static UrlValidator urlValidator = new UrlValidator(schemes);
    private static StatusModel[] statusModels;
    private static ExecutorService executor;

    public static void main(String[] args) throws IOException {
        new Controller(0);
    }

}
