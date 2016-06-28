package ar.com.thomas.mydailynews.controller;

import android.content.Context;

import java.util.List;

import ar.com.thomas.mydailynews.dao.NewsDAO;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.util.ResultListener;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class NewsController {

    public void getNews(final ResultListener<List<News>> listener,String feedLink){
        NewsDAO newsDAO = new NewsDAO();

        newsDAO.getNewsList(new ResultListener<List<News>>() {
            @Override
            public void finish(List<News> result) {
                listener.finish(result);
            }
        }, feedLink);
    }
}
