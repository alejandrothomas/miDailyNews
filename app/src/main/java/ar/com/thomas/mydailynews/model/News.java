package ar.com.thomas.mydailynews.model;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class News {
    private String title;
    private String subtitle;
    private String rssFeed;

    public News(String titulo, String subtitulo, String rssFeed) {
        this.title = titulo;
        this.subtitle = subtitulo;
        this.rssFeed = rssFeed;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getRSSFeed() {
        return rssFeed;
    }

    public void setRSSFeed(String rssFeed) {
        this.rssFeed = rssFeed;
    }
}
