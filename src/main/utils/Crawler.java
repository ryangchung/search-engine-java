package main.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Crawler {
    private final ArrayList<String> URL_QUEUE;
    private final ArrayList<String> UNIQUE_PAGES;
    private final ArrayList<Page> PAGES;
    private final String SEED;

    public Crawler(String seed) {
        this.UNIQUE_PAGES = new ArrayList<>();
        this.URL_QUEUE = new ArrayList<>();
        this.URL_QUEUE.add(seed);
        this.SEED = seed;

        this.PAGES = new ArrayList<>();
    }

    // Mutates the words hashmap to add the words in the wordsArray
    private void addToWords(String[] wordsArray, HashMap<String, Integer> words) {
        for (String word : wordsArray) {
            if (word.length() > 0) {
                words.put(word, words.containsKey(word) ? words.get(word) + 1 : 1);
            }
        }
    }

    // Remove html tags from the line
    public String[] extractWords(String line) {
        return line
                .split("<p")[1]
                .split(">")[1]
                .split("<")[0]
                .split("\n");
    }

    public int crawl() {
        this.UNIQUE_PAGES.add(this.SEED);
        HashMap<String, Integer> numOfWordOccurrences = new HashMap<>();

        // Uses a queue to index every page in a network given a SEED
        while (this.URL_QUEUE.size() > 0) {
            Page page = new Page();
            String content = Link.getPageContent(this.URL_QUEUE.get(0));
            if (content.equals("")) {
                return 0;
            }

            // Regex to split the content by p tags and a tags
            String[] splitContent = content.split("(?i)(?s)(?m)(?<=</p>)|(?<=</a>)|(?<=<body)");

            // Page-related information
            HashMap<String, Integer> words = new HashMap<>();
            ArrayList<String> outgoingLinks = new ArrayList<>();

            for (String line : splitContent) {
                if (line.contains("<p")) {
                    // Adding words into the words hashmap
                    addToWords(extractWords(line), words);
                } else if (line.contains("<a")) {
                    // get link between the parenthesis of the href
                    String link = Link.anchorToLink(line, this.SEED);
                    outgoingLinks.add(link);
                    if (!UNIQUE_PAGES.contains(link)) {
                        this.UNIQUE_PAGES.add(link);
                        this.URL_QUEUE.add(link);
                    }
                }
            }

            // Adding to tally in idf
            for (String word : words.keySet()) {
                if (numOfWordOccurrences.containsKey(word)) {
                    numOfWordOccurrences.put(word, numOfWordOccurrences.get(word) + 1);
                } else {
                    numOfWordOccurrences.put(word, 1);
                }
            }

            page.setTitle(splitContent[0].split("(?i)(?s)(?m)(?<=<title>)|(?<=</title>)")[1].split("<")[0].trim());
            page.setUrl(this.URL_QUEUE.get(0));
            page.setOutgoingLinks(outgoingLinks.toArray(new String[0]));
            page.setTf(Statistics.computeTf(words));
            this.PAGES.add(page);
            this.URL_QUEUE.remove(0);
        }

        // Stores information that relies on non-page-specific information
        HashMap<String, String[]> outgoingLinks = new HashMap<>();
        for (Page page : this.PAGES) {
            outgoingLinks.put(page.getUrl(), page.getOutgoingLinks());
        }

        HashMap<String, Double> idf = Statistics.computeIdf(this.UNIQUE_PAGES.size(), numOfWordOccurrences);
        HashMap<String, String[]> incomingLinks = Statistics.computeIncomingLinks(this.UNIQUE_PAGES, outgoingLinks);
        HashMap<String, Double> allPageRank = Statistics.computePageRank(this.UNIQUE_PAGES, incomingLinks);

        for (Page page : this.PAGES) {
            page.setTfIdf(Statistics.computeTfIdf(page.getTf(), idf));
            page.setIncomingLinks(incomingLinks.get(page.getUrl()));
            page.setPageRank(allPageRank.get(page.getTitle()));
        }

        FileOperations.deleteFiles(new File("data"));
        FileOperations.save(idf, this.UNIQUE_PAGES.toArray(new String[0]), this.PAGES);
        return UNIQUE_PAGES.size();
    }

    public static void main(String[] args) {
        Crawler crawler = new Crawler("https://people.scs.carleton.ca/~davidmckenney/tinyfruits/N-0.html");
        System.out.println(crawler.crawl());
    }
}
