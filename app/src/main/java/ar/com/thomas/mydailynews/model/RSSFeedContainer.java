package ar.com.thomas.mydailynews.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alejandrothomas on 6/26/16.
 */
public class RSSFeedContainer {

    @SerializedName("results")
    private List<RSSFeed> rssFeedList;

    public List<RSSFeed> getRssFeedList() {
        return rssFeedList;
    }

    public void setRssFeedList(List<RSSFeed> rssFeedList) {
        this.rssFeedList = rssFeedList;
    }
}