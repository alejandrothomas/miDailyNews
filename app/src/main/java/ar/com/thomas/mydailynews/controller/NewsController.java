package ar.com.thomas.mydailynews.controller;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.dao.NewsDAO;
import ar.com.thomas.mydailynews.dao.RSSFeedDAO;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.util.ResultListener;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class NewsController {

    private NewsDAO newsDAO;

    public void getNews(final ResultListener<List<News>> listener,String feedLink,Context context, String rssFeed){
        newsDAO = NewsDAO.getNewsDAO(context);

        newsDAO.getNewsList(new ResultListener<List<News>>() {
            @Override
            public void finish(List<News> result) {
                listener.finish(result);
            }
        }, feedLink, rssFeed);
    }

    public List<News> getNewsListFromDB(String rssFeed,Context context){
        newsDAO = NewsDAO.getNewsDAO(context);
        return newsDAO.getNewsListFromDatabase(rssFeed);
    }

    public void updateFavourites (List<String> rssFeedFavouriteList, Context context){

        newsDAO = NewsDAO.getNewsDAO(context);
        newsDAO.updateFavouriteListDB(rssFeedFavouriteList);
    }

    public List<RSSFeed> getFavouritesFromDB(Context context){
        newsDAO = NewsDAO.getNewsDAO(context);
        return newsDAO.getFavouritesFromDatabase();
    }

    public void clearNewsDB(Context context){
        newsDAO = NewsDAO.getNewsDAO(context);
        newsDAO.clearNewsDB();
    }
}
