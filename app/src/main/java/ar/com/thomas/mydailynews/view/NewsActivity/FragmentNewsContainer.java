package ar.com.thomas.mydailynews.view.NewsActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.thomas.mydailynews.R;


/**
 * Created by alejandrothomas on 6/25/16.
 */
public class FragmentNewsContainer extends Fragment {

    public static final String NEWS_TITLE_ID = "newsTitle";
    public static final String RSS_SOURCE_LINK = "rssFeedLink";
    public static final String RSS_SOURCE = "rssFeed";
    public static final String POSITION = "position";
    public static final String RSS_FEED = "rssFeed";




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_container, container, false);

        Bundle bundle = getArguments();

        String selectedNewsRSSFeedID = bundle.getString(RSS_FEED);
        Integer position = bundle.getInt(POSITION);

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.viewpager_container);

        FragmentNewsViewPagerAdapter fragmentNewsViewPagerAdapter = new FragmentNewsViewPagerAdapter(getFragmentManager(),getContext(), selectedNewsRSSFeedID);

        viewPager.setAdapter(fragmentNewsViewPagerAdapter);
        viewPager.setCurrentItem(position);

        return view;
    }


}
