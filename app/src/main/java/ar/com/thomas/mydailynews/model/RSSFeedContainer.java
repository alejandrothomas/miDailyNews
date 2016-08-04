package ar.com.thomas.mydailynews.model;

import java.util.List;

/**
 * Created by alejandrothomas on 6/26/16.
 */
public class RSSFeedContainer {


    private List<RSSFeed> resultsRss;

    public void setResultsRss(List<RSSFeed> resultsRss) {
        this.resultsRss = resultsRss;
    }

    public List<RSSFeed> getResultsRss() {

        return resultsRss;
    }

    public RSSFeedContainer() {
    }

    public RSSFeedContainer(List<RSSFeed> resultsRss) {

        this.resultsRss = resultsRss;
    }
}