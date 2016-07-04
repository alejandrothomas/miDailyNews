package ar.com.thomas.mydailynews.dao;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.model.RSSFeedContainer;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class RSSFeedDAO extends GenericDAO {


    public List<RSSFeed> getRSSFeedList(Context context){
        RSSFeedContainer rssFeedContainer = (RSSFeedContainer)getObjectJSON(context,RSSFeedContainer.class,"RSSFeed.json");

        return rssFeedContainer.getRssFeedList();
    }


}
