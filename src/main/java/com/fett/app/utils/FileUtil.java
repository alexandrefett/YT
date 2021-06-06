package com.fett.app.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FileUtil {

    public static ArrayList<String> readFile(String filePath){
        Path currentRelativePath = Paths.get("");
        String p = currentRelativePath.toAbsolutePath().toString();
        ArrayList<String> r = new ArrayList<String>();
        try (Stream<String> stream = Files.lines( Paths.get(p + "/"+filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> r.add(s));
        } catch (NoSuchFileException e) {
            try{

                Path fileToCreatePath = Paths.get(p + "/"+filePath);
                Path newFilePath = Files.createFile(fileToCreatePath);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    public static void addToFile(String filePath, String data){
        Path currentRelativePath = Paths.get("");
        String p = currentRelativePath.toAbsolutePath().toString();
        String s = System.lineSeparator() + data;
        try {
            Files.write(Paths.get(p + "/"+filePath), s.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
