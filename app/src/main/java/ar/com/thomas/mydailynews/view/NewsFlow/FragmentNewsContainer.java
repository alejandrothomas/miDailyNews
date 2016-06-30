package ar.com.thomas.mydailynews.view.NewsFlow;

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
    public static final String RSS_SOURCE = "rssFeed";
    public static final String POSITION = "position";
    public static final String RSS_FEED = "rssFeed";
    private String selectedNewsRSSFeedID;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(selectedNewsRSSFeedID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_container, container, false);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.viewpager_container);

        Bundle bundle = getArguments();
        selectedNewsRSSFeedID = bundle.getString(RSS_FEED);
        Integer position = bundle.getInt(POSITION);

        FragmentNewsViewPagerAdapter fragmentNewsViewPagerAdapter = new FragmentNewsViewPagerAdapter(getFragmentManager(),getContext(), selectedNewsRSSFeedID);

        viewPager.setAdapter(fragmentNewsViewPagerAdapter);
        viewPager.setCurrentItem(position);

        return view;
    }


}
