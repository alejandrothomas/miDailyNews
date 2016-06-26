package ar.com.thomas.mydailynews.controller;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.dao.NewsDAO;
import ar.com.thomas.mydailynews.model.News;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class NewsController {

    public List<News> getNews(Context context, String rssSource){

        NewsDAO newsDAO = new NewsDAO();
        List<News> result = newsDAO.getNews(context, rssSource);

        return result;
    }
}
