package ar.com.thomas.mydailynews.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alejandrothomas on 6/26/16.
 */
public class RSSFeedCategoryContainer {

    @SerializedName("results")
    private List<RSSFeedCategory> rssFeedCategoryList;

    public void setRssFeedCategoryList(List<RSSFeedCategory> rssFeedCategoryList) {
        this.rssFeedCategoryList = rssFeedCategoryList;
    }

    public List<RSSFeedCategory> getRssFeedCategoryList() {

        return this.rssFeedCategoryList;
    }
}
