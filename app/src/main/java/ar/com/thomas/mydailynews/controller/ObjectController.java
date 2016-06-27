package ar.com.thomas.mydailynews.controller;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.dao.ObjectDAO;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class ObjectController {

    private ObjectDAO objectDAO;

    public List<News> getNewsList(Context context, String rssFeed){

        objectDAO = new ObjectDAO();

        return objectDAO.getNewsList(context, rssFeed);
    }

    public List<RSSFeedCategory> getRSSFeedCategoryList(Context context){
        objectDAO = new ObjectDAO();
        return objectDAO.getRSSFeedCategoryList(context);
    }

    public List<RSSFeed> getRSSFeedList(Context context){
        objectDAO = new ObjectDAO();
        return objectDAO.getRSSFeedList(context);
    }
}
