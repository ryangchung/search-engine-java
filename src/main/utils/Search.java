package main.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search {
    private static int count(String phrase, String targetWord) {
        String[] splitPhrase = phrase.split(" ");
        int count = 0;
        for (String word : splitPhrase) {
            if (word.equals(targetWord)) {
                count++;
            }
        }
        return count;
    }

    public static ArrayList<SearchResult> search(String phrase, boolean boost) {
        String[] listOfPages = FileOperations.read(new File("data/uniquePages.txt")).split(" ");
        List<String> uniqueWords = Arrays.stream(phrase.split(" "))
                .distinct().toList();
        
        // Get document vectors
        ArrayList<ArrayList<Double>> documentVectors = new ArrayList<>();
        for (String page : listOfPages) {
            ArrayList<Double> documentVector = new ArrayList<>();
            for (String word : uniqueWords) {
                File file = new File("data/" + Link.linkToTitle(page) + "/tfIdf/" + word + ".txt");
                if (file.exists()) {
                    documentVector.add(Double.parseDouble(FileOperations.read(file)));
                } else {
                    documentVector.add(0.0);
                }
            }
            documentVectors.add(documentVector);
        }

        // Calculate query vector
        ArrayList<Double> queryVector = new ArrayList<>();
        for (String word : uniqueWords) {
            File file = new File("data/idf/" + word + ".txt");
            if (file.exists()) {
                double fileValue = Double.parseDouble(FileOperations.read(file));
                queryVector.add(Statistics.log2(1.0 + (double) count(phrase, word) / phrase.split(" ").length) * fileValue);
            } else {
                queryVector.add(0.0);
            }
        }

        // Get left denominator
        ArrayList<SearchResult> contentScores = new ArrayList<>();
        double sum = 0;
        for (double element : queryVector) {
            sum += Math.pow(element, 2);
        }
        double leftDenominator = Math.sqrt(sum);
        int index = 0;
        for (ArrayList<Double> documentVector : documentVectors) {
            // Get numerator
            double numerator = 0;
            for (int i = 0; i < queryVector.size(); i++) {
                numerator += queryVector.get(i) * documentVector.get(i);
            }

            // Get right denominator
            double rightDenominator = 0.0;
            for (double element : documentVector) {
                rightDenominator += Math.pow(element, 2);
            }
            rightDenominator = Math.sqrt(rightDenominator);

            // Get cosine similarity
            final double cosineSimilarity;
            if (leftDenominator == 0 || rightDenominator == 0) {
                cosineSimilarity = 0.0;
            } else {
                cosineSimilarity = numerator / (leftDenominator * rightDenominator);
            }
            
            String currentDocument = Link.linkToTitle(listOfPages[index]);
            if (boost) {
                double score = Double.parseDouble(FileOperations.read(new File("data/" + currentDocument + "/pageRank.txt")));
                contentScores.add(new SearchResultObject(currentDocument, cosineSimilarity * score));
            } else {
                contentScores.add(new SearchResultObject(currentDocument, cosineSimilarity));
            }

            index++;
        }

        // Sort by score, then by title
        contentScores.sort((one, two) -> {
            if (one.getScore() == two.getScore()) {
                return one.getTitle().compareTo(two.getTitle());
            } else {
                return (one.getScore() > two.getScore()) ? -1 : 1;
            }
        });
        return contentScores;
    }

    public static void main(String[] args) {
        System.out.println(search("coconut coconut orange blueberry lime lime lime tomato", false));
    }
}
