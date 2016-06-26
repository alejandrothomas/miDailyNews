package ar.com.thomas.mydailynews.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.model.News;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class NewsDAO {
    public List<News> getNews(Context context,String rssSource){
        List<News> newsList = new ArrayList<>();

        //harcoded news
        for (int i=0; i<10;i++){
            News news = new News("News Title "+i, "News Subtitle " + i, rssSource);
            newsList.add(news);
        }
     return newsList;
    }
}
