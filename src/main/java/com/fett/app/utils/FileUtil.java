package com.fett.app.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtil {
    public static void appendToFile(String file, String line)
            throws IOException {

        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }

    public static ArrayList<String> readFile(String filePath)
    {
        Path currentRelativePath = Paths.get("");
        String p = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + p);
        ArrayList<String> r = new ArrayList<String>();
        try (Stream<String> stream = Files.lines( Paths.get(p + "/"+filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> r.add(s));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return r;
    }
}
