package ar.com.thomas.mydailynews.model;

import java.util.List;

/**
 * Created by alejandrothomas on 6/26/16.
 */
public class RSSFeed {

    private RSSFeedCategory category;
    private String title;
    private String createdAt;
    private String feedLink;
    private String iconLink;
    private String objectId;
    private List<News> newsList;


    public void setCategory(RSSFeedCategory category) {
        this.category = category;
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

    public RSSFeedCategory getCategory() {
        return category;
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

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {

        return objectId;
    }
}
