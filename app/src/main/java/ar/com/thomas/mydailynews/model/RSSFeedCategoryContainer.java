package ar.com.thomas.mydailynews.model;

import java.util.List;

/**
 * Created by alejandrothomas on 6/26/16.
 */
public class RSSFeedCategoryContainer {

    private List<RSSFeedCategory> resultsCat;

    public void setResultsCat(List<RSSFeedCategory> resultsCat) {
        this.resultsCat = resultsCat;
    }

    public RSSFeedCategoryContainer(List<RSSFeedCategory> resultsCat) {
        this.resultsCat = resultsCat;
    }

    public RSSFeedCategoryContainer() {
    }

    public List<RSSFeedCategory> getResultsCat() {

        return resultsCat;
    }
}