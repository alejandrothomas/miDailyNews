package ar.com.thomas.mydailynews.model;

import com.google.gson.annotations.SerializedName;

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