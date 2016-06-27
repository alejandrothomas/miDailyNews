package ar.com.thomas.mydailynews.controller;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.dao.RSSFeedCategoryDAO;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class RSSFeedCategoryController {

    public List<RSSFeedCategory> getRSSFeedCategoryList(Context context){
        RSSFeedCategoryDAO rssFeedCategoryDAO = new RSSFeedCategoryDAO();
        return rssFeedCategoryDAO.getRSSFeedCategoryList(context);
    }
}
