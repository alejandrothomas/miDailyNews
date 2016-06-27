package ar.com.thomas.mydailynews.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.model.RSSFeedCategoryContainer;
import ar.com.thomas.mydailynews.model.RSSFeedContainer;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class ObjectDAO extends GenericDAO{

    public List<News> getNewsList(Context context,String rssFeed){
        List<News> newsList = new ArrayList<>();

        //harcoded news
        for (int i=0; i<10;i++){
            News news = new News("News Title "+i, "News Subtitle " + i, rssFeed);
            newsList.add(news);
        }
     return newsList;
    }

    public List<RSSFeedCategory> getRSSFeedCategoryList(Context context){
        RSSFeedCategoryContainer rssFeedCategoryContainer = (RSSFeedCategoryContainer) getObjectJSON(context,RSSFeedCategoryContainer.class,"RSSFeedCategory.json");

        return rssFeedCategoryContainer.getRssFeedCategoryList();
    }

    public List<RSSFeed> getRSSFeedList(Context context){
        RSSFeedContainer rssFeedContainer = (RSSFeedContainer)getObjectJSON(context,RSSFeedContainer.class,"RSSFeed.json");

        return rssFeedContainer.getRssFeedList();
    }
}
