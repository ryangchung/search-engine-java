package main.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class FileOperations {
    // This class shouldn't be instantiated
    private FileOperations() {
        throw new AssertionError();
    }

    public static String arrayToString(String[] array) {
        StringBuilder string = new StringBuilder();
        for (String element : array) {
            string.append(element).append(" ");
        }
        return string.toString();
    }

    // Recursively deletes all files given a file tree
    public static void deleteFiles(File element) {
        if (element.isDirectory()) {
            for (File file : Objects.requireNonNull(element.listFiles())) {
                deleteFiles(file);
            }
        }
        element.delete();
    }

    public static String read(File element) {
        // Read file contents of a file given the file name
        String content = "";
        try {
            content = Files.readString(Paths.get(element.getPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void save(
        HashMap<String, Double> idf,
        String[] uniquePages,
        ArrayList<Page> pages
    ) {
        try {
            // Creates root folder
            new File("data").mkdir();
            
            // Saves IDFs
            new File("data/idf").mkdir();
            for (String word : idf.keySet()) {
                Files.writeString(Paths.get("data/idf/" + word + ".txt"),
                idf.get(word).toString(),
                StandardCharsets.UTF_8);
            }

            // Saves uniquePages
            Files.writeString(Paths.get("data/uniquePages.txt"), arrayToString(uniquePages), StandardCharsets.UTF_8);

            // Create new folder names based on each page and inject data
            for (Page page: pages) {
                String rootDirectory = "data" + File.separator + page.getTitle();

                new File(rootDirectory).mkdir();
                Files.writeString(Paths.get(rootDirectory + File.separator + "incomingLinks.txt"), arrayToString(page.getIncomingLinks()), StandardCharsets.UTF_8);
                Files.writeString(Paths.get(rootDirectory + File.separator + "outgoingLinks.txt"), arrayToString(page.getOutgoingLinks()), StandardCharsets.UTF_8);
                Files.writeString(Paths.get(rootDirectory + File.separator + "pageRank.txt"), Double.toString(page.getPageRank()), StandardCharsets.UTF_8);
                Files.writeString(Paths.get(rootDirectory + File.separator + "url.txt"), page.getUrl(), StandardCharsets.UTF_8);
                
                new File(rootDirectory + File.separator + "tf").mkdir();
                for (String word : page.getTf().keySet()) {
                    Files.writeString(Paths.get(rootDirectory + File.separator + "tf" + File.separator + word + ".txt"),
                    page.getTf().get(word).toString(),
                    StandardCharsets.UTF_8);
                }

                new File(rootDirectory + File.separator + "tfIdf").mkdir();
                for (String word : page.getTfIdf().keySet()) {
                    Files.writeString(Paths.get(rootDirectory + File.separator + "tfIdf" + File.separator + word + ".txt"),
                    page.getTfIdf().get(word).toString(),
                    StandardCharsets.UTF_8);
                }
            }

            // Saves incoming links

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
