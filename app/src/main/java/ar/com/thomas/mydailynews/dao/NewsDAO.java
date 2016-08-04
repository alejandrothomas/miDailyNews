package ar.com.thomas.mydailynews.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private static final String TABLE_BOOKMARKS = "Bookmarks";
    private static final String TABLE_FAVOURITES = "Favourites";
    private static final String TABLE_HISTORY = "History";
    private static final String ID = "ID";
    private static final String TITLE = "title";
    private static final String ENCODED = "encoded";
    private static final String DESCRIPTION = "description";
    private static final String CONTENT = "content";
    private static final String IMAGE_URL = "imageURL";
    private static final String RSS_FEED_LINK = "rssFeedLink";
    private static final String LINK = "link";
    private static final String PUB_DATE = "pubDate";
    private static final String RSS_FEED = "rssFeed";
    private static final String IS_FAVOURITE = "is_favourite";
    private static NewsDAO newsDAO = null;
    private Context context;

    private NewsDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static NewsDAO getNewsDAO(Context context) {
        if (newsDAO == null) {
            newsDAO = new NewsDAO(context.getApplicationContext());
        }
        return newsDAO;
    }

    //------------------OFFLINE--------------------//

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NEWS + "("
                + ID + " TEXT PRIMARY KEY, "
                + PUB_DATE + " TEXT, "
                + TITLE + " TEXT, "
                + LINK + " TEXT, "
                + ENCODED + " TEXT, "
                + CONTENT + " TEXT, "
                + RSS_FEED + " TEXT, "
                + RSS_FEED_LINK + " TEXT, "
                + IMAGE_URL + " TEXT, "
                + DESCRIPTION + " TEXT " + ")";

        db.execSQL(createTable);

        String createTableBookmarks = "CREATE TABLE " + TABLE_BOOKMARKS + "("
                + ID + " TEXT PRIMARY KEY, "
                + PUB_DATE + " TEXT, "
                + TITLE + " TEXT, "
                + RSS_FEED + " TEXT, "
                + CONTENT + " TEXT, "
                + ENCODED + " TEXT, "
                + LINK + " TEXT, "
                + IMAGE_URL + " TEXT, "
                + DESCRIPTION + " TEXT " + ")";

        db.execSQL(createTableBookmarks);

        String createTableFavourites = "CREATE TABLE " + TABLE_FAVOURITES + "("
                + ID + " TEXT PRIMARY KEY, "
                + RSS_FEED + " TEXT, "
                + RSS_FEED_LINK + " TEXT, "
                + IS_FAVOURITE + " TEXT " + ")";

        db.execSQL(createTableFavourites);

        String createTableHistory = "CREATE TABLE " + TABLE_HISTORY + "("
                + ID + " TEXT PRIMARY KEY, "
                + PUB_DATE + " TEXT, "
                + TITLE + " TEXT, "
                + LINK + " TEXT, "
                + CONTENT + " TEXT, "
                + RSS_FEED + " TEXT, "
                + ENCODED + " TEXT, "
                + IMAGE_URL + " TEXT, "
                + DESCRIPTION + " TEXT " + ")";

        db.execSQL(createTableHistory);
    }

    public void updateFavouriteListDB(List<String> rssFeedFavouriteList) {
        SQLiteDatabase database = getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_FAVOURITES + " SET " + IS_FAVOURITE + " = \'NO\';";
        database.execSQL(updateQuery);

        for (String rssFeed : rssFeedFavouriteList) {
            this.updateFavourite(rssFeed);
        }
    }

    public void updateFavourite(String rssFeed) {
        rssFeed = rssFeed.replaceAll("'", "''");
        SQLiteDatabase database = getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_FAVOURITES + " SET " + IS_FAVOURITE + " = \'YES\' WHERE " + RSS_FEED + " == '" + rssFeed + "'";
        database.execSQL(updateQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addNewsListToDB(List<News> newsList, String rssFeed, String rssFeedLink) {

        for (News news : newsList) {

            if (!checkIfNewsExist(news)) {
                this.addNewsToDB(news, rssFeed);
            }
            if (!checkIfRSSExist(rssFeed)) {
                this.addRSSToDB(rssFeed, rssFeedLink);
            }
        }
    }

    private Boolean checkIfRSSExist(String rssFeed) {
        SQLiteDatabase database = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_FAVOURITES
                + " WHERE " + RSS_FEED + "==?";
        Cursor result = database.rawQuery(selectQuery, new String[]{rssFeed});
        Integer count = result.getCount();

        database.close();
        return (count > 0);
    }

    public void addRSSToDB(String rssFeed, String rssFeedLink) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(RSS_FEED, rssFeed);
        row.put(RSS_FEED_LINK, rssFeedLink);
        database.insert(TABLE_FAVOURITES, null, row);
        database.close();


    }

    private Boolean checkIfNewsExist(News news) {

        SQLiteDatabase database = getReadableDatabase();

        String newsTitle = "";

        if (news.getTitle() != null) {
            newsTitle = news.getTitle();
            newsTitle = newsTitle.replaceAll("'", "\\'");
            newsTitle = newsTitle.replaceAll("\"", "&quote;");
        }


        String selectQuery = "SELECT * FROM " + TABLE_NEWS
                + " WHERE " + TITLE + "==?";

        Cursor result = database.rawQuery(selectQuery, new String[]{newsTitle});
        Integer count = result.getCount();

        database.close();

        return (count > 0);
    }

    public void addNewsToDB(News news, String rssFeed) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues row = new ContentValues();

        news.setNewsID(news.getLink());

        String imgFromDescription = news.getDescription();
        String imgFromEncoded = news.getEncoded();

        if (news.getImageUrl() != null) {
            row.put(IMAGE_URL, news.getImageUrl());
        }

        if (news.getImageUrl() == null && imgFromDescription != null) {
            Pattern titleFinder = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher regexMatcher = titleFinder.matcher(imgFromDescription);
            while (regexMatcher.find()) {
                imgFromDescription = regexMatcher.group(1);
                if (imgFromDescription.substring(imgFromDescription.lastIndexOf(".") + 1, imgFromDescription.length()).equals("jpg") || imgFromDescription.substring(imgFromDescription.lastIndexOf(".") + 1, imgFromDescription.length()).equals("png")) {

                    news.setImageUrl(imgFromDescription);
                    row.put(IMAGE_URL, imgFromDescription);
                    break;
                }
            }
        }

        if (news.getImageUrl() == null && imgFromEncoded != null) {

            Pattern titleFinder = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher regexMatcher = titleFinder.matcher(imgFromEncoded);
            while (regexMatcher.find()) {
                imgFromEncoded = regexMatcher.group(1);
                if (imgFromEncoded.substring(imgFromEncoded.lastIndexOf(".") + 1, imgFromEncoded.length()).equals("jpg") || imgFromEncoded.substring(imgFromEncoded.lastIndexOf(".") + 1, imgFromEncoded.length()).equals("png")) {

                    news.setImageUrl(imgFromEncoded);
                    row.put(IMAGE_URL, imgFromEncoded);
                    break;
                }
            }
        }

        row.put(ID, news.getLink());
        row.put(ENCODED, news.getEncoded());
        row.put(LINK, news.getLink());
        row.put(TITLE, news.getTitle());
        row.put(DESCRIPTION, news.getDescription());
        row.put(CONTENT, news.getContent());
        row.put(RSS_FEED, rssFeed);
        row.put(PUB_DATE, news.getPubDate());

        database.insert(TABLE_NEWS, null, row);
        database.close();
    }

    public void addBookmark(News news) {

        if (!checkIfBookmarkExist(news.getNewsID())) {
            SQLiteDatabase database = getWritableDatabase();
            ContentValues row = new ContentValues();

            putNews(row, news);

            database.insert(TABLE_BOOKMARKS, null, row);
            database.close();
        }
    }

    private Boolean checkIfBookmarkExist(String newsID) {
        SQLiteDatabase database = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKMARKS
                + " WHERE " + ID + "==?";
        Cursor result = database.rawQuery(selectQuery, new String[]{newsID});
        Integer count = result.getCount();

        database.close();
        return (count > 0);
    }

    public void addHistory(News news) {

        if (!checkIfHistoryExist(news.getNewsID())) {
            SQLiteDatabase database = getWritableDatabase();
            ContentValues row = new ContentValues();

            putNews(row, news);

            database.insert(TABLE_HISTORY, null, row);
            database.close();
        }

    }

    private Boolean checkIfHistoryExist(String newsID) {
        SQLiteDatabase database = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_HISTORY
                + " WHERE " + ID + "==?";
        Cursor result = database.rawQuery(selectQuery, new String[]{newsID});
        Integer count = result.getCount();

        database.close();
        return (count > 0);
    }

    public void removeBookmark(News news) {
        SQLiteDatabase database = getWritableDatabase();

        database.delete(TABLE_BOOKMARKS, ID + "='" + news.getNewsID() + "'", null);
    }

    public void removeHistory(News news) {
        SQLiteDatabase database = getWritableDatabase();
        news.getTitle();

        database.execSQL("DELETE FROM " + TABLE_HISTORY + " WHERE " + ID + " == '" + news.getNewsID() + "'");
    }

    public List<News> getBookmarks() {

        SQLiteDatabase database = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_BOOKMARKS;

        Cursor cursor = database.rawQuery(selectQuery, null);

        List<News> bookmarkNewsList = new ArrayList<>();

        while (cursor.moveToNext()) {

            News news = setNews(cursor);
            bookmarkNewsList.add(news);
        }
        return bookmarkNewsList;
    }

    public List<News> getHistory() {

        SQLiteDatabase database = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_HISTORY;

        Cursor cursor = database.rawQuery(selectQuery, null);

        List<News> historyNewsList = new ArrayList<>();

        while (cursor.moveToNext()) {


            News news = setNews(cursor);
            historyNewsList.add(news);
        }
        return historyNewsList;
    }

    public List<News> getNewsListFromDatabase(String rssFeed) {

        SQLiteDatabase database = getReadableDatabase();

        rssFeed = rssFeed.replaceAll("'", "\\'");
        rssFeed = rssFeed.replaceAll("\"", "&quote;");

        String selectQuery = "SELECT * FROM " + TABLE_NEWS
                + " WHERE " + RSS_FEED + "==?";

        Cursor cursor = database.rawQuery(selectQuery, new String[]{rssFeed});

        List<News> newsList = new ArrayList<>();

        while (cursor.moveToNext()) {


            News news = setNews(cursor);

            newsList.add(news);
        }
        return newsList;
    }

    public List<RSSFeed> getFavouritesFromDatabase() {

        SQLiteDatabase database = getReadableDatabase();


        String selectQuery = "SELECT * FROM " + TABLE_FAVOURITES
                + " WHERE " + IS_FAVOURITE + "==?";

        Cursor cursor = database.rawQuery(selectQuery, new String[]{"YES"});

        List<RSSFeed> favouriteList = new ArrayList<>();

        while (cursor.moveToNext()) {
            RSSFeed rssFeed = new RSSFeed();
            rssFeed.setTitle(cursor.getString(cursor.getColumnIndex(RSS_FEED)));
            rssFeed.setFeedLink(cursor.getString(cursor.getColumnIndex(RSS_FEED_LINK)));
            favouriteList.add(rssFeed);
        }

        return favouriteList;
    }

    public void clearNewsDB() {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_NEWS);
    }

    public void clearSelectedRSSNewsFromDB(String rssFeed) {
        SQLiteDatabase database = getWritableDatabase();

        rssFeed = rssFeed.replaceAll("'", "''");


        database.execSQL("DELETE FROM " + TABLE_NEWS + " WHERE " + RSS_FEED + " == '" + rssFeed + "'");
    }

    public News setNews(Cursor cursor) {

        News news = new News();

        news.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        news.setImageUrl(cursor.getString(cursor.getColumnIndex(IMAGE_URL)));
        news.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
        news.setEncoded(cursor.getString(cursor.getColumnIndex(ENCODED)));
        news.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
        news.setPubDate(cursor.getString(cursor.getColumnIndex(PUB_DATE)));
        news.setRssFeed(cursor.getString(cursor.getColumnIndex(RSS_FEED)));
        news.setLink(cursor.getString(cursor.getColumnIndex(LINK)));
        news.setNewsID(cursor.getString(cursor.getColumnIndex(ID)));

        return news;
    }


    public void putNews(ContentValues row, News news) {
        row.put(TITLE, news.getTitle());
        row.put(ID, news.getNewsID());
        row.put(DESCRIPTION, news.getDescription());
        row.put(CONTENT, news.getContent());
        row.put(ENCODED, news.getEncoded());
        row.put(IMAGE_URL, news.getImageUrl());
        row.put(PUB_DATE, news.getPubDate());
        row.put(LINK, news.getLink());
        row.put(RSS_FEED, news.getRssFeed());
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
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                parser.setInput(input, null);
                Integer status = parser.getEventType();

                while (status != XmlPullParser.END_DOCUMENT && result.size() <= 15) {

                    switch (status) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:

                            if (parser.getName().equals("item")) {
                                aNews = new News();
                            }
                            if (aNews != null) {
                                if (parser.getName().equalsIgnoreCase("title")) {
                                    aNews.setTitle(parser.nextText());
                                } else if (parser.getName().equalsIgnoreCase("link")) {
                                    aNews.setLink(parser.nextText());
                                } else if (parser.getName().equalsIgnoreCase("description")) {
                                    aNews.setDescription(parser.nextText());
                                } else if (parser.getName().equalsIgnoreCase("encoded")) {
                                    aNews.setEncoded(parser.nextText());
                                } else if (parser.getName().equalsIgnoreCase("pubDate")) {
                                    aNews.setPubDate(parser.nextText());
                                } else if (parser.getName().equalsIgnoreCase("content")) {
                                    if (aNews.getImageUrl() == null)
                                        aNews.setImageUrl(parser.getAttributeValue(null, "url"));
                                } else if (parser.getName().equalsIgnoreCase("author")) {
                                    aNews.setAuthor(parser.nextText());
                                } else if (parser.getName().equalsIgnoreCase("thumbnail")) {
                                    if (aNews.getImageUrl() == null)
                                        aNews.setImageUrl(parser.getAttributeValue(null, "url"));
                                } else if (parser.getName().equalsIgnoreCase("enclosure")) {
                                    if (aNews.getImageUrl() == null)
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
            addNewsListToDB(input, rssFeed,feedLink);
            this.listener.finish(input);
        }
    }
}

