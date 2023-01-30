package main.model;

import main.utils.Crawler;
import main.utils.Search;
import main.utils.FileOperations;
import main.utils.Link;
import main.utils.SearchResult;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectTesterImp implements ProjectTester {
    public void initialize() {
        FileOperations.deleteFiles(new File("data"));
    }

    public void crawl(String seedURL) {
        Crawler crawler = new Crawler(seedURL);
        crawler.crawl();
    }

    private List<String> getStrings(String url) {
        File file = new File("data/" + Link.linkToTitle(url) + "/outgoingLinks.txt");
        if (file.exists() && !file.isDirectory()) {
            String fileContent = FileOperations.read(file);
            return Arrays.asList(fileContent.split(" "));
        } else {
            return null;
        }
    }

    public List<String> getOutgoingLinks(String url) {
        return getStrings(url);
    }

    public List<String> getIncomingLinks(String url) {
        return getStrings(url);
    }

    public double getPageRank(String url) {
        File file = new File("data/" + Link.linkToTitle(url) + "/pageRank.txt");
        if (file.exists() && !file.isDirectory()) {
            return Double.parseDouble(FileOperations.read(file));
        } else {
            return -1.0;
        }
    }

    public double getIDF(String word) {
        File file = new File("data/idf/" + word + ".txt");
        if (file.exists() && !file.isDirectory()) {
            return Double.parseDouble(FileOperations.read(file));
        } else {
            return 0.0;
        }
    }

    public double getTF(String url, String word) {
        File file = new File("data/" + Link.linkToTitle(url) + "/tf/" + word + ".txt");
        if (file.exists() && !file.isDirectory()) {
            return Double.parseDouble(FileOperations.read(file));
        } else {
            return 0.0;
        }
    }

    public double getTFIDF(String url, String word) {
        File file = new File("data/" + Link.linkToTitle(url) + "/tfIdf/" + word + ".txt");
        if (file.exists() && !file.isDirectory()) {
            return Double.parseDouble(FileOperations.read(file));
        } else {
            return 0.0;
        }
    }

    public List<SearchResult> search(String query, boolean boost, int X) {
        return Search.search(query, boost).stream().limit(X).collect(Collectors.toList());
    }
}
