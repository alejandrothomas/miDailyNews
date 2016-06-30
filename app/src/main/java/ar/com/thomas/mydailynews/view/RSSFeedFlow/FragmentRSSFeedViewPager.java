package ar.com.thomas.mydailynews.view.RSSFeedFlow;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.model.NewsAdapter;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.util.ResultListener;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentRSSFeedViewPager extends Fragment {

    public static final String RSS_FEED = "RSSFeed";
    public static final String RSS_FEED_OBJECTID = "RSSFeed";
    public static final String RSS_FEED_LINK = "rssFeedLink";
    private RecyclerView recyclerView;
    private String rssFeed;
    private FragmentCalls fragmentCalls;
    NewsAdapter newsAdapter;
    String rssFeedObjectID;
    private final List<News> newsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_rssfeed_viewpager,container,false);
        NewsController newsController = new NewsController();

        Bundle bundle = getArguments();
        rssFeed = bundle.getString(RSS_FEED);
        String rssFeedLink = bundle.getString(RSS_FEED_LINK);
        rssFeedObjectID = bundle.getString(RSS_FEED_OBJECTID);


        newsController.getNews(new ResultListener<List<News>>() {
            @Override
            public void finish(List<News> result) {

                newsList.addAll(result);

                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                newsAdapter = new NewsAdapter(result);
                recyclerView.setAdapter(newsAdapter);

                NewsListener newsListener = new NewsListener();
                newsAdapter.setOnClickListener(newsListener);



            }
        }, rssFeedLink, getActivity(), rssFeedObjectID);

        return view;
    }

    public static FragmentRSSFeedViewPager generateFragment (RSSFeed rssFeed){

        FragmentRSSFeedViewPager fragmentRSSFeedViewPager = new FragmentRSSFeedViewPager();

        Bundle arguments = new Bundle();
        arguments.putString(RSS_FEED, rssFeed.getTitle());
        arguments.putString(RSS_FEED_LINK, rssFeed.getFeedLink());
        fragmentRSSFeedViewPager.setArguments(arguments);
        fragmentRSSFeedViewPager.setRssFeed(rssFeed.getTitle());
        return fragmentRSSFeedViewPager;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCalls = (FragmentCalls) activity;
    }

    public String getRssFeed() {
        return rssFeed;
    }

    public void setRssFeed(String rssFeed) {
        this.rssFeed = rssFeed;
    }

    public interface FragmentCalls{
        public void getNotifications(String newsClicked, Integer itemPosition, String rssFeedID);
    }

    private class NewsListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Integer itemPosition = recyclerView.getChildAdapterPosition(v);
            String itemClicked = newsAdapter.selectedNewsID(itemPosition);

            fragmentCalls.getNotifications(itemClicked, itemPosition, rssFeedObjectID);
        }
    }


}
