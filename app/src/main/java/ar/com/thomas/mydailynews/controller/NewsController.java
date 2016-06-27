package ar.com.thomas.mydailynews.controller;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.dao.NewsDAO;
import ar.com.thomas.mydailynews.model.News;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class NewsController {

    public List<News> getNewsList(Context context, String rssFeed){

        NewsDAO newsDAO = new NewsDAO();

        return newsDAO.getNewsList(context, rssFeed);
    }
}
