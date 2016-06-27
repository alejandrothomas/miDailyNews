package ar.com.thomas.mydailynews.controller;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.dao.NewsDAO;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class NewsController {

    private NewsDAO newsDAO;

    public List<News> getNewsList(Context context, String rssFeed){

        newsDAO = new NewsDAO();

        return newsDAO.getNewsList(context, rssFeed);
    }

    public List<RSSFeedCategory> getRSSFeedCategoryList(Context context){
        newsDAO = new NewsDAO();
        return newsDAO.getRSSFeedCategoryList(context);
    }

    public List<RSSFeed> getRSSFeedList(Context context){
        newsDAO = new NewsDAO();
        return newsDAO.getRSSFeedList(context);
    }
}
