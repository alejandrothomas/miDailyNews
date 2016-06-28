package ar.com.thomas.mydailynews.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.util.DAOException;
import ar.com.thomas.mydailynews.util.HTTPConnectionManager;
import ar.com.thomas.mydailynews.util.ResultListener;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class NewsDAO extends GenericDAO {

    public void getNewsList(ResultListener<List<News>> listener, String feedLink) {
        RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(listener, feedLink);
        retrieveFeedTask.execute();
    }
}

class RetrieveFeedTask extends AsyncTask<String, Void, List<News>> {

    private ResultListener<List<News>> listener;

    private String feedLink;

    public RetrieveFeedTask(ResultListener<List<News>> listener, String feedLink) {
        this.listener = listener;
        this.feedLink = feedLink;
    }

    @Override
    protected List<News> doInBackground(String... params) {

        HTTPConnectionManager connectionManager = new HTTPConnectionManager();
        InputStream input = null;

        try {
            input = connectionManager.getRequestStream(feedLink);
        } catch (DAOException e) {
            e.printStackTrace();
        }
        List<News> result = new ArrayList<>();
        News aNews = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(input, null);
            Integer status = parser.getEventType();
            while (status != XmlPullParser.END_DOCUMENT) {
                switch (status) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        Log.d("TAG", parser.getName());
                        if (parser.getName().equals("item")) {
                            aNews = new News();
                        }
                        if (aNews != null) {
                            if (parser.getName().equals("title")) {
                                aNews.setTitle(parser.nextText());
                            } else if (parser.getName().equals("link")) {
                                aNews.setLink(parser.nextText());
                            } else if (parser.getName().equals("description")) {
                                Log.d("TAG", parser.getName());
                                aNews.setDescription(parser.nextText());
                            } else if (parser.getName().equals("pubDate")) {
                                Log.d("TAG", parser.getName());
                                aNews.setPubDate(parser.nextText());
                            } else if (parser.getName().equals("author")) {
                                Log.d("TAG", parser.getName());
                                aNews.setAuthor(parser.nextText());
                            } else if (parser.getName().equals("enclosure")) {
                                Log.d("TAG", parser.getName());
                                aNews.setImageUrl(parser.getAttributeValue(null, "url"));
                            }
                        }
                        Log.v("INFO", parser.getName());
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d("TAG", parser.getName());
                        if (parser.getName().equals("item")) {
                            result.add(aNews);
                            aNews = null;
                        }
                        break;
                }
                status = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<News> input) {
        this.listener.finish(input);
    }
}