package main.utils;

import java.io.IOException;

public class Link {
    // This class shouldn't be instantiated
    private Link() {
        throw new AssertionError();
    }

    // Converts relative links to absolute links
    public static String relativeToAbsolute(String url, String seed) {
        if (url.startsWith("http")) {
            return url;
        } else {
            String rootLink = seed.substring(0, seed.lastIndexOf("/"));
            return rootLink + "/" + url.substring(2);
        }
    }

    // Returns the link between the parenthesis of the href
    public static String anchorToLink(String anchor, String seed) {
        String link = anchor.split("href=\"")[1].split("\"")[0];
        return relativeToAbsolute(link, seed);
    }

    public static String linkToTitle(String link) {
        return link.substring(link.lastIndexOf("/") + 1, link.lastIndexOf("."));
    }

    // Returns the content of the page at the given url
    public static String getPageContent(String url) {
        try {
            return WebRequester.readURL(url);
        } catch (IOException e) {
            // IOException also catches MalformedURLException
            e.printStackTrace();
            return "-1";
        }
    }
}
