package ar.com.thomas.mydailynews.controller;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.dao.RSSFeedDAO;
import ar.com.thomas.mydailynews.model.RSSFeed;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class RSSFeedController {
    public List<RSSFeed> getRSSFeedList(Context context){
        RSSFeedDAO rssFeedDAO = new RSSFeedDAO();
        return rssFeedDAO.getRSSFeedList(context);
    }
}
