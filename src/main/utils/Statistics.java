package main.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Statistics {
    // This class shouldn't be instantiated
    private Statistics() {
        throw new AssertionError();
    }
    public static Double log2(double value) {
        return Math.log(value) / Math.log(2);
    }

    public static HashMap<String, String[]> computeIncomingLinks(ArrayList<String> uniquePages, HashMap<String, String[]> allOutgoingLinks) {
        HashMap<String, String[]> allIncomingLinks = new HashMap<>();
        for (String link : uniquePages) {
            ArrayList<String> incomingLinks = new ArrayList<>();
            for (String outgoingLink : allOutgoingLinks.keySet()) {
                if (Arrays.asList(allOutgoingLinks.get(outgoingLink)).contains(link)) {
                    incomingLinks.add(outgoingLink);
                }
            }
            allIncomingLinks.put(link, incomingLinks.toArray(new String[0]));
        }
        return allIncomingLinks;
    }

    public static HashMap<String, Double> computeTf(HashMap<String, Integer> words) {
        HashMap<String, Double> tf = new HashMap<>();
        // Get the word count from words
        int wordCount = 0;
        for (int count : words.values()) {
            wordCount += count;
        }

        for (String word : words.keySet()) {
            tf.put(word, (double) words.get(word) / wordCount);
        }
        return tf;
    }

    public static HashMap<String, Double> computeIdf(int numOfPages, HashMap<String, Integer> numOfWordOccurrences) {
        HashMap<String, Double> idf = new HashMap<>();
        for (String word : numOfWordOccurrences.keySet()) {
            idf.put(word, log2((double) numOfPages / (numOfWordOccurrences.get(word) + 1)));
        }
        return idf;
    }

    public static HashMap<String, Double> computeTfIdf(HashMap<String, Double> tf, HashMap<String, Double> idf) {
        HashMap<String, Double> tfIdf = new HashMap<>();
        for (String word : tf.keySet()) {
            tfIdf.put(word, idf.get(word) * log2(tf.get(word) + 1));
        }
        return tfIdf;
    }

    public static HashMap<String, Double> computePageRank(
        ArrayList<String> uniquePages,
        HashMap<String, String[]> incomingLinks
    ) {
        final double ALPHA = 0.1;
        final int NETWORK_SIZE = uniquePages.size();
        double[][] adjacencyMatrix = new double[NETWORK_SIZE][NETWORK_SIZE];
        HashMap<String, Integer> titleId = new HashMap<>();

        // Create mapping from url to id
        int index = 0;
        for (String url : uniquePages) {
            titleId.put(Link.linkToTitle(url), index);
            index++;
        }

        // Fill in adjacency matrix
        for (String url : uniquePages) {
            for (String incomingLink : incomingLinks.get(url)) {
                adjacencyMatrix[titleId.get(Link.linkToTitle(url))][titleId.get(Link.linkToTitle(incomingLink))] = 1;
            }
        }

        // Matrix calculations
        for (double[] row : adjacencyMatrix) {
            int countOfOne = 0;
            for (int i = 0; i < NETWORK_SIZE; i++) {
                if (row[i] == 1) {
                    countOfOne++;
                }
            }

            for (int i = 0; i < NETWORK_SIZE; i++) {
                if (countOfOne == 0) {
                    row[i] = 1.0 / NETWORK_SIZE;
                } else {
                    if (row[i] == 1) {
                        row[i] /= countOfOne;
                    }
                }
            }

            for (int i = 0; i < NETWORK_SIZE; i++) {
                row[i] = ALPHA / NETWORK_SIZE + (1.0 - ALPHA) * row[i];
            }
        }
        
        // Multiplying until values converge < 0.0001 via euclidean distance
        double[] currentVector = new double[NETWORK_SIZE];
        Arrays.fill(currentVector, 1.0 / NETWORK_SIZE);
        while (true) {
            double[] newVector = new double[NETWORK_SIZE];
            for (int i = 0; i < NETWORK_SIZE; i++) {
                for (int j = 0; j < NETWORK_SIZE; j++) {
                    newVector[i] += currentVector[j] * adjacencyMatrix[j][i];
                }
            }

            double euclideanDistance = 0;
            for (int i = 0; i < NETWORK_SIZE; i++) {
                euclideanDistance += Math.pow(newVector[i] - currentVector[i], 2);
            }
            euclideanDistance = Math.sqrt(euclideanDistance);

            if (euclideanDistance < 0.0001) {
                break;
            }

            currentVector = newVector;
        }

        HashMap<String, Double> pageRank = new HashMap<>();
        for (String url : uniquePages) {
            String title = Link.linkToTitle(url);
            pageRank.put(title, currentVector[titleId.get(title)]);
        }

        return pageRank;
    }
}
