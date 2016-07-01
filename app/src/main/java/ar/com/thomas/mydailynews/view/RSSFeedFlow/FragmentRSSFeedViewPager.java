package ar.com.thomas.mydailynews.view.RSSFeedFlow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.ListenerStubs;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentRSSFeedViewPager extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String RSS_FEED = "RSSFeed";
    public static final String RSS_FEED_OBJECTID = "RSSFeed";
    public static final String RSS_FEED_LINK = "rssFeedLink";
    private RecyclerView recyclerView;
    private Context context;
    private String rssFeed;
    private FragmentCalls fragmentCalls;
    SwipeRefreshLayout swipeRefreshLayout;
    NewsAdapter newsAdapter;
    String rssFeedLink;
    NewsController newsController;
    String rssFeedObjectID;
    View view;

    private final List<News> newsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rssfeed_viewpager,container,false);
        context = getActivity();


        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        int top_to_padding=50;
        swipeRefreshLayout.setProgressViewOffset(false, 0,top_to_padding);

        newsController = new NewsController();

        Bundle bundle = getArguments();
        rssFeed = bundle.getString(RSS_FEED);
        rssFeedLink = bundle.getString(RSS_FEED_LINK);
        rssFeedObjectID = bundle.getString(RSS_FEED_OBJECTID);

        onRefresh();

        return view;
    }

    void update(){


        swipeRefreshLayout.setRefreshing(true);
        newsController.getNews(new ResultListener<List<News>>() {
            @Override
            public void finish(List<News> result) {

                newsList.addAll(result);

                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                OverScrollDecoratorHelper.setUpOverScroll(recyclerView,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
                newsAdapter = new NewsAdapter(result, context);
                recyclerView.setAdapter(newsAdapter);

                NewsListener newsListener = new NewsListener();
                newsAdapter.setOnClickListener(newsListener);
                newsAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);




            }
        }, rssFeedLink, getActivity(), rssFeedObjectID);
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

    @Override
    public void onRefresh() {
        update();
    }

}
