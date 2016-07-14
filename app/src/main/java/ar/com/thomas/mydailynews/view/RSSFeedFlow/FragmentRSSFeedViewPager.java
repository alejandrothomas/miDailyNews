package ar.com.thomas.mydailynews.view.RSSFeedFlow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.model.RSSFeed;
import ar.com.thomas.mydailynews.util.ResultListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class FragmentRSSFeedViewPager extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String RSS_FEED = "RSSFeed";
    public static final String RSS_FEED_OBJECTID = "RSSFeed";
    public static final String RSS_FEED_LINK = "rssFeedLink";
    private final List<News> newsList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsAdapter newsAdapter;
    private String rssFeedLink;
    private NewsController newsController;
    private String rssFeedObjectID;
    private View view;
    private RecyclerView recyclerView;
    private Context context;
    private String rssFeed;
    private FragmentCalls fragmentCalls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rssfeed_viewpager, container, false);
        context = getActivity();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        int top_to_padding = 56;
        swipeRefreshLayout.setProgressViewOffset(false, 0, top_to_padding);

        Bundle bundle = getArguments();
        rssFeed = bundle.getString(RSS_FEED);
        rssFeedLink = bundle.getString(RSS_FEED_LINK);
        rssFeedObjectID = bundle.getString(RSS_FEED_OBJECTID);

        newsController = new NewsController();
        newsAdapter = new NewsAdapter(newsList,context);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(newsAdapter);

        NewsListener newsListener = new NewsListener();
        newsAdapter.setOnClickListener(newsListener);

        if(newsList.size()<1){
            newsList.addAll(newsController.getNewsListFromDB(rssFeed,context));
        }

        if(newsList.size()<1){
            onRefresh();
        }

        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        recyclerView.setNestedScrollingEnabled(false);

        return view;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        if(newsList.size()>0){
            newsList.clear();
            newsController.clearSelectedRSSNewsFromDB(context,rssFeed);
        }

        newsController.getNews(new ResultListener<List<News>>() {
            @Override
            public void finish(List<News> result) {

                List<News> fromDB = newsController.getNewsListFromDB(rssFeed,context);

                for (News news: fromDB){
                    if(!newsList.contains(news)){
                        newsList.add(news);
                    }
                }
                newsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        }, rssFeedLink, getActivity(), rssFeedObjectID);

    }

    public String getRssFeed() {
        return rssFeed;
    }

    public void setRssFeed(String rssFeed) {
        this.rssFeed = rssFeed;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCalls = (FragmentCalls) activity;
    }

    public interface FragmentCalls {
        void getNotifications(String newsClicked, Integer itemPosition, String rssFeedID);
    }

    private class NewsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Integer itemPosition = recyclerView.getChildAdapterPosition(v);
            String itemClicked = newsAdapter.selectedNewsID(itemPosition);

            NewsController newsController = new NewsController();
            newsController.addHistory(context,newsAdapter.getNews(itemPosition));

            fragmentCalls.getNotifications(itemClicked, itemPosition, rssFeedObjectID);
        }
    }

    public static FragmentRSSFeedViewPager generateFragment(RSSFeed rssFeed) {

        FragmentRSSFeedViewPager fragmentRSSFeedViewPager = new FragmentRSSFeedViewPager();

        Bundle arguments = new Bundle();
        arguments.putString(RSS_FEED, rssFeed.getTitle());
        arguments.putString(RSS_FEED_LINK, rssFeed.getFeedLink());
        fragmentRSSFeedViewPager.setArguments(arguments);
        fragmentRSSFeedViewPager.setRssFeed(rssFeed.getTitle());
        return fragmentRSSFeedViewPager;
    }

}
