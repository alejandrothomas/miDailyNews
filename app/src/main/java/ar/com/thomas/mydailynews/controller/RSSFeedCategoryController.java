package ar.com.thomas.mydailynews.controller;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.dao.RSSFeedCategoryDAO;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.util.ResultListener;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class RSSFeedCategoryController {

    public void getRSSFeedCategoryList(final ResultListener<List<RSSFeedCategory>>listener) {

        RSSFeedCategoryDAO rssFeedCategoryDAO = new RSSFeedCategoryDAO();
        rssFeedCategoryDAO.getRSSFeedCategoryList(new ResultListener<List<RSSFeedCategory>>() {
            @Override
            public void finish(List<RSSFeedCategory> result) {
                listener.finish(result);
            }
        });
    }
}
