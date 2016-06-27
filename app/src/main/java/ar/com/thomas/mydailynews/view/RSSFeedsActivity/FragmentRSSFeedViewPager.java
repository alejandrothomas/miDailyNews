package ar.com.thomas.mydailynews.view.RSSFeedsActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.controller.NewsController;
import ar.com.thomas.mydailynews.model.News;
import ar.com.thomas.mydailynews.model.NewsAdapter;

/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentRSSFeedViewPager extends Fragment {

    public static final String RSS_FEED = "RSSFeed";
    private List<News> newsList;
    private RecyclerView recyclerView;
    private String rssFeed;
    FragmentCalls fragmentCalls;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rssfeed_viewpager,container,false);
        NewsController newsController = new NewsController();
        Bundle bundle = getArguments();
        rssFeed = bundle.getString(RSS_FEED);

        newsList=newsController.getNewsList(getContext(),rssFeed);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        NewsAdapter newsAdapter = new NewsAdapter(newsList);
        NewsListener newsListener = new NewsListener();
        newsAdapter.setOnClickListener(newsListener);

        recyclerView.setAdapter(newsAdapter);


        return view;
    }

    public static FragmentRSSFeedViewPager generateFragment (String rssFeed){

        FragmentRSSFeedViewPager fragmentRSSFeedViewPager = new FragmentRSSFeedViewPager();

        Bundle arguments = new Bundle();
        arguments.putString(RSS_FEED, rssFeed);
        fragmentRSSFeedViewPager.setArguments(arguments);
        fragmentRSSFeedViewPager.setRssFeed(rssFeed);
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
        public void getNotifications(News selectedNews, Integer newsPosition, List<News> newsList);
    }

    private class NewsListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Integer itemPosition = recyclerView.getChildAdapterPosition(v);
            News itemClicked = newsList.get(itemPosition);
            fragmentCalls.getNotifications(itemClicked,itemPosition,newsList);
        }
    }


}
