package ar.com.thomas.mydailynews.model;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class News {
    private String title;
    private String subtitle;
    private String rssSource;

    public News(String titulo, String subtitulo, String rssSource) {
        this.title = titulo;
        this.subtitle = subtitulo;
        this.rssSource = rssSource;
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

    public String getRssSource() {
        return rssSource;
    }

    public void setRssSource(String RSSSource) {
        this.rssSource = RSSSource;
    }
}
