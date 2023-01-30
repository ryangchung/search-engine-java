package main.utils;

import java.util.HashMap;

public class Page {
    private String url;
    private String title;
    private double pageRank;
    private String[] outgoingLinks;
    private String[] incomingLinks;
    private HashMap<String, Double> tf;
    private HashMap<String, Double> tfIdf;

    public Page() {
        this.outgoingLinks = new String[0];
        this.incomingLinks = new String[0];
        this.tf = new HashMap<>();
        this.tfIdf = new HashMap<>();
        this.url = "";
        this.title = "";
        this.pageRank = 0;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPageRank() {
        return pageRank;
    }

    public void setPageRank(double pageRank) {
        this.pageRank = pageRank;
    }

    public String[] getOutgoingLinks() {
        return outgoingLinks;
    }

    public void setOutgoingLinks(String[] outgoingLinks) {
        this.outgoingLinks = outgoingLinks;
    }

    public String[] getIncomingLinks() {
        return incomingLinks;
    }

    public void setIncomingLinks(String[] incomingLinks) {
        this.incomingLinks = incomingLinks;
    }

    public HashMap<String, Double> getTf() {
        return tf;
    }

    public void setTf(HashMap<String, Double> tf) {
        this.tf = tf;
    }

    public HashMap<String, Double> getTfIdf() {
        return tfIdf;
    }

    public void setTfIdf(HashMap<String, Double> tfIdf) {
        this.tfIdf = tfIdf;
    }
}
