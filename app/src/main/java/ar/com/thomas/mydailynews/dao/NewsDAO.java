package ar.com.thomas.mydailynews.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.model.News;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class NewsDAO extends GenericDAO {

    public List<News> getNewsList(Context context,String rssFeed){
        List<News> newsList = new ArrayList<>();

        //harcoded news
        for (int i=0; i<10;i++){
            News news = new News("News Title "+i, "News Subtitle " + i, rssFeed);
            newsList.add(news);
        }
        return newsList;
    }
}
