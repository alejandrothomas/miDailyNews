package ar.com.thomas.mydailynews.dao;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.model.RSSFeedCategoryContainer;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class RSSFeedCategoryDAO extends GenericDAO {
    public List<RSSFeedCategory> getRSSFeedCategoryList(Context context){
        RSSFeedCategoryContainer rssFeedCategoryContainer = (RSSFeedCategoryContainer) getObjectJSON(context,RSSFeedCategoryContainer.class,"RSSFeedCategory.json");

        return rssFeedCategoryContainer.getRssFeedCategoryList();
    }
}
