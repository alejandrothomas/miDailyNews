package ar.com.thomas.mydailynews.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.sql.RowId;
import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.util.DAOException;
import ar.com.thomas.mydailynews.util.HTTPConnectionManager;
import ar.com.thomas.mydailynews.util.ResultListener;

/**
 * Created by alejandrothomas on 6/27/16.
 */
public class NewsDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NewsDB";
    private static final Integer DATABASE_VERSION = 1;
    private static final String TABLE_NEWS = "News";
    private static final String ID = "ID";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_URL = "imageURL";
    private static final String PUB_DATE = "pubDate";
    private static final String RSS_FEED = "rssFeed";
    private static final String TABLE_FAVOURITES = "Favourites";
    private static final String IS_FAVOURITE = "is_favourite";
    private static NewsDAO newsDAO = null;

    private NewsDAO(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    public static NewsDAO getNewsDAO (Context context){
        if(newsDAO == null){
            newsDAO = new NewsDAO(context.getApplicationContext());
        }
        return newsDAO;
    }



    //------------------OFFLINE--------------------//
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NEWS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PUB_DATE + " TEXT, "
                + TITLE + " TEXT, "
                + RSS_FEED + " TEXT, "
                + IMAGE_URL + " TEXT, "
                + DESCRIPTION + " TEXT " + ")";

        db.execSQL(createTable);

        String createTableFavourites = "CREATE TABLE " + TABLE_FAVOURITES + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RSS_FEED + " TEXT, "
                + IS_FAVOURITE + " TEXT " + ")";

        db.execSQL(createTableFavourites);
    }


    public void updateFavouriteListDB (List<String> rssFeedFavouriteList){
        SQLiteDatabase database = getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_FAVOURITES + " SET " + IS_FAVOURITE + " = \'NO\';";
        database.execSQL(updateQuery);
        Log.v("borrado", "se limpio toda la tabla de favs");


        for (String rssFeed:rssFeedFavouriteList){
            this.updateFavourite(rssFeed);
        }
    }

    public void updateFavourite(String rssFeed){
        SQLiteDatabase database = getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_FAVOURITES + " SET " + IS_FAVOURITE + " = \'YES\' WHERE " + RSS_FEED + " == '" + rssFeed + "'";
        database.execSQL(updateQuery);
        Log.v("agregado", rssFeed + "ahora esta marcado como FAVORITO");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addNewsListToDB(List<News> newsList, String rssFeed) {

        for (News news : newsList) {
            if(!checkIfNewsExist(news)) {
                this.addNewsToDB(news,rssFeed);
            }
            if(!checkIfRSSExist(rssFeed)){
                this.addRSSToDB(rssFeed);
            }
        }
    }

    private Boolean checkIfRSSExist(String rssFeed){
        SQLiteDatabase database = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_FAVOURITES
                + " WHERE " + RSS_FEED + "==?";
        Cursor result = database.rawQuery(selectQuery, new String[]{rssFeed});
        Integer count = result.getCount();

        database.close();
        return (count > 0);
    }

    public void addRSSToDB(String rssFeed){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(RSS_FEED,rssFeed);
        database.insert(TABLE_FAVOURITES,null,row);
        database.close();
    }

    private Boolean checkIfNewsExist(News news){

        SQLiteDatabase database = getReadableDatabase();

        String newsTitle = news.getTitle().replaceAll("'","\'");
        newsTitle = news.getTitle().replaceAll("''","\''");

        String selectQuery = "SELECT * FROM " + TABLE_NEWS
                + " WHERE " + TITLE + "==?";

        Cursor result = database.rawQuery(selectQuery, new String[]{newsTitle});
        Integer count = result.getCount();

        database.close();

        return (count > 0);
    }

    public void addNewsToDB(News news, String rssFeed){

        SQLiteDatabase database = getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(TITLE, news.getTitle());
        row.put(DESCRIPTION, news.getDescription());
        row.put(IMAGE_URL, news.getImageUrl());
        row.put(RSS_FEED, rssFeed);
        row.put(PUB_DATE, news.getPubDate());

        database.insert(TABLE_NEWS, null, row);
        database.close();

    }

    public List<News> getNewsListFromDatabase(String rssFeed){

        SQLiteDatabase database = getReadableDatabase();

        rssFeed = rssFeed.replaceAll("'","\'");
        rssFeed = rssFeed.replaceAll("''","\''");

        String selectQuery = "SELECT * FROM " + TABLE_NEWS
                + " WHERE " + RSS_FEED + "==?";
//                + " ORDER BY " + PUB_DATE + " DESC ";

        Cursor cursor = database.rawQuery(selectQuery, new String[]{rssFeed});

        List<News> newsList = new ArrayList<>();

        while(cursor.moveToNext()){

            News news = new News();

            news.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            news.setImageUrl(cursor.getString(cursor.getColumnIndex(IMAGE_URL)));
            news.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
            news.setPubDate(cursor.getString(cursor.getColumnIndex(PUB_DATE)));
            newsList.add(news);
        }
        return newsList;
    }
    public List<String> getFavouritesFromDatabase(){

        SQLiteDatabase database = getReadableDatabase();


        String selectQuery = "SELECT * FROM " + TABLE_FAVOURITES
                + " WHERE " + IS_FAVOURITE + "==?";

        Cursor cursor = database.rawQuery(selectQuery, new String[]{"YES"});

        List<String> favouriteList = new ArrayList<>();

        while(cursor.moveToNext()){
            favouriteList.add(cursor.getString(cursor.getColumnIndex(RSS_FEED)));
        }

        return favouriteList;
    }

    public void clearNewsDB(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_NEWS);
    }


    //---------------------ONLINE------------------//

    public void getNewsList(ResultListener<List<News>> listener, String feedLink, String rssFeed) {

        RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(listener, feedLink, rssFeed);
        retrieveFeedTask.execute();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, List<News>> {

        private ResultListener<List<News>> listener;
        private String feedLink;
        private String rssFeed;

        public RetrieveFeedTask(ResultListener<List<News>> listener, String feedLink, String rssFeed) {
            this.listener = listener;
            this.feedLink = feedLink;
            this.rssFeed = rssFeed;
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

                            if (parser.getName().equals("item")) {
                                aNews = new News();
                            }
                            if (aNews != null) {
                                if (parser.getName().equals("title")) {
                                    aNews.setTitle(parser.nextText());
                                } else if (parser.getName().equals("link")) {
                                    aNews.setLink(parser.nextText());
                                } else if (parser.getName().equals("description")) {
                                    aNews.setDescription(parser.nextText());
                                } else if (parser.getName().equals("pubDate")) {
                                    aNews.setPubDate(parser.nextText());
                                } else if (parser.getName().equals("author")) {

                                    aNews.setAuthor(parser.nextText());
                                } else if (parser.getName().equals("enclosure")) {

                                    aNews.setImageUrl(parser.getAttributeValue(null, "url"));
                                }
                            }

                            break;
                        case XmlPullParser.END_TAG:

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
            addNewsListToDB(input,rssFeed);
            this.listener.finish(input);
        }
    }
}

