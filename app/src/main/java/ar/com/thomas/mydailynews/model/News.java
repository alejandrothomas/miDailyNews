package ar.com.thomas.mydailynews.model;

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
    private String newsID;
    private String content;
    private String encoded;

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    public String getEncoded() {

        return encoded;
    }




    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {

        return content;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public String getNewsID() {

        return newsID;
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

    @Override
    public boolean equals(Object o) {
        News otherNews = (News) o;
        if(otherNews!=null && this.newsID!=null){
            return (this.newsID.equals(otherNews.getNewsID()));
        }else
            return false;

    }
}