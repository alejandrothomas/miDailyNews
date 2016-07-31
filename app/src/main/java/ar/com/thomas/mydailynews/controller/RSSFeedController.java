package ar.com.thomas.mydailynews.controller;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.dao.RSSFeedCategoryDAO;
import ar.com.thomas.mydailynews.dao.RSSFeedDAO;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.model.RSSFeedCategory;
import ar.com.thomas.mydailynews.util.ResultListener;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class RSSFeedController {
    public void getRSSFeedList(final ResultListener<List<RSSFeed>> listener) {

        RSSFeedDAO rssFeedDAO = new RSSFeedDAO();
        rssFeedDAO.getRSSFeedList(new ResultListener<List<RSSFeed>>() {
            @Override
            public void finish(List<RSSFeed> result) {
                listener.finish(result);
            }
        });
    }
}
