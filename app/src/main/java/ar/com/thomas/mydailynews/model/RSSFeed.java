package ar.com.thomas.mydailynews.model;

import java.util.List;

/**
 * Created by alejandrothomas on 6/26/16.
 */
public class RSSFeed {
    private RSSFeedCategory rssFeedCategory;
    private String title;
    private String createdAt;
    private String feedLink;
    private String iconLink;
    private List<News> newsList;


    public void setRssFeedCategory(RSSFeedCategory rssFeedCategory) {
        this.rssFeedCategory = rssFeedCategory;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setFeedLink(String feedLink) {
        this.feedLink = feedLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public RSSFeedCategory getRssFeedCategory() {
        return rssFeedCategory;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getFeedLink() {
        return feedLink;
    }

    public String getIconLink() {
        return iconLink;
    }

    public List<News> getNewsList() {
        return newsList;
    }
}
