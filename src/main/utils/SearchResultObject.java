package main.utils;

public class SearchResultObject implements SearchResult {
    String title;
    double score;

    public SearchResultObject(String title, double score) {
        this.title = title;
        this.score = Double.parseDouble(String.format("%.3f", score));
    }

    public String getTitle() {
        return title;
    }

    public double getScore() {
        return score;
    }

    public String toString() {
        return title + ", " + score;
    }
}
