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

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.io.FileInputStream;
import java.io.IOException;

public class App {

    public static void main(String[] args) {
        boolean isRunning = true;
        YoutubeBot ytb;
        try {
            FileInputStream refreshToken = new FileInputStream("<json firebase>");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .setDatabaseUrl("<url firebase>")
                    .build();

            FirebaseApp app = FirebaseApp.initializeApp(options);
            System.out.println("Initilized...");
            Database db = new Database(app);
            ytb = new YoutubeBot(db);

            UserRecord userRecord = FirebaseAuth.getInstance().getUser("XFTGaFvOF9Q2dVH8sHespNlfD8C3");
            System.out.println("Successfully fetched user data: " + userRecord.getUid());
            System.out.println(userRecord.getEmail());

            System.in.read();
            ytb.finish();
        } catch (IOException | FirebaseAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
