package ar.com.thomas.mydailynews.model;

import java.util.List;

import javax.xml.transform.Source;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class News {
    private String title;
    private String link;
    private String description;
    private String author;
    private String pubDate;
    private String imageUrl;

    private String rssFeed;
    private String rssFeedIdToString;
    private Integer newsID;



    public void setNewsID(Integer newsID) {
        this.newsID = newsID;
    }

    public Integer getNewsID() {

        return newsID;
    }

    public void setRssFeedIdToString(String rssFeedIdToString) {
        this.rssFeedIdToString = rssFeedIdToString;
    }

    public String getRssFeedIdToString() {

        return rssFeedIdToString;
    }

    public void setRssFeed(String rssFeed) {
        this.rssFeed = rssFeed;
    }

    public String getRssFeed() {

        return rssFeed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}